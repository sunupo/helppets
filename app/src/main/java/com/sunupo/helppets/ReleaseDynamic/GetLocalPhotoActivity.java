package com.sunupo.helppets.ReleaseDynamic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sunupo.helppets.Login.LoginActivity;
import com.sunupo.helppets.R;
import com.sunupo.helppets.test.ImageDownloadActivity;
import com.sunupo.helppets.util.CityBaseActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.UploadImage;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

/**
 * @address BeiJing
 * @author LiXufei
 * @function  拍照取图相册取图
 */
public class GetLocalPhotoActivity extends CityBaseActivity{

    private final String TAG="GetLocalPhotoActivity";
    private String loginName="";
    private EditText dynamicContentText;
    private RadioGroup sendAdoptSelector;
    private EditText animalWeight;
    private EditText animalColor;
    private EditText animalAge;
    private ImageView imageView;
    private int TAKE_PHOTO=1;   //拍照
    private int GET_PHOTO=2;    //取照片

    private Bitmap photoBitmap;//记录拍照或者相册照片的bitmap

    private Button show_type_selecter;
    PickerView pickerView;
    TextView type_text;

    private Button releaseDynamicButton;

    private final String Separatior="-";

    SharedPreferences sp=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("发布动态");
        setContentView(R.layout.activity_get_local_photo);
        dynamicContentText=findViewById(R.id.release_dynamic_content_text);
        sendAdoptSelector=findViewById(R.id.send_adopt_animal_selector);
        animalWeight=findViewById(R.id.animal_weight);
        animalColor=findViewById(R.id.animal_color);
        animalAge=findViewById(R.id.animal_age);

        sp=this.getSharedPreferences("LoginInfo",MODE_PRIVATE);
        loginName=sp.getString(Constants.LOGIN_NAME
                ,Constants.LOGIN_NAME);
        //获取ImageView控件
        imageView = (ImageView) findViewById(R.id.takePhoto_imageView);

        /*
         * 拍照取图
         */
        findViewById(R.id.takePhoto_bt1).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //设置拍照意图
                Intent mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(mIntent, TAKE_PHOTO);

            }
        });

        /*
         * 相册取图
         */
        findViewById(R.id.takePhoto_bt2).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_PHOTO);

            }
        });

        initProvinceDatas();
        show_type_selecter= (Button) findViewById(R.id.show_type_selecter);
        type_text= (TextView) findViewById(R.id.type_text);
        //选择器数据实体类封装
        PickerData data=new PickerData();
        //设置数据，有多少层级自己确定
        data.setFirstDatas(mProvinceDatas);
        data.setSecondDatas(mCitisDatasMap);
        data.setThirdDatas(mDistrictDatasMap);
        data.setFourthDatas(new HashMap<String, String[]>());
        //设置初始化默认显示的三级菜单(此方法可以选择传参数量1到4个)
//        data.setInitSelectText("河北省","石家庄市","平山县");
        //初始化选择器
        pickerView=new PickerView(this,data);
        show_type_selecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示选择器
                pickerView.show(show_type_selecter);
            }
        });
        //选择器完成三级选择后点击回调
        pickerView.setOnPickerClickListener(new OnPickerClickListener() {
            //选择列表时触发的事件
            @Override
            public void OnPickerClick(PickerData pickerData) {
                //想获取单个选择项 PickerData内也有方法（弹出框手动关闭）
                type_text.setText(pickerData.getFirstText()+Separatior+pickerData.getSecondText()+Separatior+pickerData.getThirdText());
                pickerView.dismiss();//关闭选择器
            }
            //点击确定按钮触发的事件（自动关闭）
            @Override
            public void OnPickerConfirmClick(PickerData pickerData) {
                type_text.setText(pickerData.getFirstText()+Separatior+pickerData.getSecondText()+Separatior+pickerData.getThirdText());
            }
        });

        releaseDynamicButton=findViewById(R.id.release_dynamic_button);
        View.OnClickListener releaseListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();
                if (null == networkInfo || !networkInfo.isConnected())
                {
                    Toast.makeText(GetLocalPhotoActivity.this,
                            "网络不可用", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                String sendAdopt="送养";
                switch (sendAdoptSelector.getCheckedRadioButtonId()){
                    case R.id.send_animal:
                        sendAdopt="送养";
                        break;
                    case R.id.adopt_animal:
                        sendAdopt="收养";
                        break;
                    default:
                        sendAdopt="送养";
                        break;
                }
                final String sendAdoptType=sendAdopt;
                final String contentText=dynamicContentText.getText().toString();
                final String WEIGHT=animalWeight.getText().toString();
                final String COLOR=animalColor.getText().toString();
                final String AGE=animalAge.getText().toString();
                String[] typeArray=new String[3];
                if(type_text.getText().toString()!=""&&!type_text.getText().toString().equals("")){
                    typeArray=type_text.getText().toString().split("-");
                }
                final String[] types={typeArray[0],typeArray[1],typeArray[2]};
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//                // 获取当前时间
//                Date date = new Date(System.currentTimeMillis());
//                final String currentTime=simpleDateFormat.format(date);
//                Time t=new Time("GMT+8");
//                final String currentTime=t.year+"-"+(t.month+1)+"-"+t.monthDay+"-"+t.hour+"-"+t.minute+"-"+t.second;
                Calendar calendar = Calendar.getInstance();
                final String currentTime=calendar.get(Calendar.YEAR)
                        +"-"+(calendar.get(Calendar.MONTH)+1)
                        +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                        "-"+calendar.get(Calendar.HOUR_OF_DAY)+
                        "-"+calendar.get(Calendar.MINUTE)+
                        "-"+calendar.get(Calendar.SECOND);
//                信息不全，不能发布
                if(contentText==""||contentText.equals("")
                        ||type_text.getText().toString()==""||type_text.getText().toString().equals("")
                        ||WEIGHT==""||WEIGHT.equals("")
                        ||COLOR==""||COLOR.equals("")
                        ||AGE==""||AGE.equals("")
                        ||photoBitmap==null){
                    Toast.makeText(GetLocalPhotoActivity.this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UploadImage.updata(photoBitmap,loginName);
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: 等于1代表成功？"+postUp(loginName,sendAdoptType,contentText
                                ,types[0],types[1],types[2],WEIGHT,COLOR,AGE,currentTime));
                    }
                }).start();
            }
        };
        releaseDynamicButton.setOnClickListener(releaseListener);

    }
    //接受回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {  //回传值接受成功

            if (requestCode == TAKE_PHOTO) {    //拍照取图

                Bundle bundle = data.getExtras();   //获取data数据集合
                Bitmap bitmap = (Bitmap) bundle.get("data");        //获得data数据
                Log.i("TAG", "拍照回传bitmap："+bitmap);
                imageView.setImageBitmap(bitmap);
                photoBitmap=bitmap;

            }

            if (requestCode == GET_PHOTO) {     //相册取图

                ContentResolver contentResolver = getContentResolver();
                try {

                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                    Log.i("TAG", "从相册回传bitmap："+bitmap);
                    imageView.setImageBitmap(bitmap);
                    photoBitmap=bitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }

    }
    private int postUp(
            String loginName
            ,String isSend
            ,String contentText
            ,String type1
            ,String type2
            ,String type3
            ,String type4
            ,String type5
            ,String type6
            ,String time)
    {
        Log.d(TAG, "postUp: ");
        try
        {
            URL url = new URL(Constants.httpip+"/releaseDynamic?userId="+"2"
                    +"&loginName="+loginName
                    +"&isSend="+isSend
                    +"&contentText="+contentText
                    +"&type1="+type1
                    +"&type2="+type2
                    +"&type3="+type3
                    +"&type4="+type4
                    +"&type5="+type5
                    +"&type6="+type6
                    +"&time="+time);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            // TODO: 3/16/2019 连接失败
            Log.d(TAG, "postUp: responseCode="+responseCode);
            if(responseCode == 200)
            {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String resCode = "";
                String readLine = null;
                while ((readLine = bReader.readLine()) != null)
                {
                    resCode += readLine;
                }
                if(resCode.equals("1"))//成功
                {
                    return 1;
                }
                else if(resCode.equals("-2"))//禁言
                {
                    return -2;
                }
            }
            else
            {
                return -1;//失败
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.release_dynamic,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.menu_release_dynamic:
                releaseDynamicButton.callOnClick();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
