package com.sunupo.helppets.ReleaseDynamic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.sunupo.helppets.Mine.setting.ChangeUserInfoActivity;
import com.sunupo.helppets.R;
import com.sunupo.helppets.comment.CommentMainActivity;
import com.sunupo.helppets.test.ImageDownloadActivity;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.CityBaseActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.InputTextTypeUtil;
import com.sunupo.helppets.util.UploadImage;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.alipay.sdk.app.statistic.c.v;

/**
 * @function  拍照取图相册取图 上传动态，有一个宠物类型选择器，资源文件在assets
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
    TextView type_text,clearContent;

    private Button releaseDynamicButton;

    private final String Separatior="-";

    SharedPreferences sp=null;
    private Uri photoUri1;
    TextView attentionImage;

    Uri  imageUri;
    String mTempPhotoPath;

    Handler handler;

    boolean illegal=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("发布动态");
        setContentView(R.layout.activity_get_local_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        requestPermission();
//        if (ContextCompat.checkSelfPermission(GetLocalPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(GetLocalPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }

        if (ContextCompat.checkSelfPermission(GetLocalPhotoActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            PermissionGen.needPermission(this, 200, Manifest.permission.CAMERA);
        } else {
            // 有权限了，直接使用相机。

        }


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UploadImage.updata(photoBitmap,loginName);
                            }
                        }).start();
                        break;
                    case 33:
                        try {
                            JSONObject jsonObject=new JSONObject(((String)(msg.obj)));
                            String log_id=jsonObject.getString("log_id");
                            String result=jsonObject.getString("result");
                            JSONObject jsonObject1=new JSONObject(result);
                            String spam=jsonObject1.getString("spam");

                            String reject=jsonObject1.getString("reject");
                            JSONArray jsonArray=new JSONArray(reject);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                Log.e("detectText", Constants.dtArr[i]+"label="+jsonObject2.getString("label")+"score="+jsonObject2.getString("score")+"hint="+jsonObject2.getString("hit") );
                                Toast.makeText(GetLocalPhotoActivity.this,"内容包含该"+Constants.dtArr[i]+"敏感词的概率为"+Float.parseFloat(jsonObject2.getString("score"))*100.0+"%\n"+jsonObject2.getString("hit"),Toast.LENGTH_LONG).show();
                                illegal=true;

                                return;
                            }
                            Toast.makeText(GetLocalPhotoActivity.this,"文本内容正常",Toast.LENGTH_SHORT).show();
                            illegal=false;
                            releaseDynamicButton.callOnClick();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        dynamicContentText=findViewById(R.id.release_dynamic_content_text);
        sendAdoptSelector=findViewById(R.id.send_adopt_animal_selector);
        animalWeight=findViewById(R.id.animal_weight);
        animalColor=findViewById(R.id.animal_color);
        animalAge=findViewById(R.id.animal_age);
        attentionImage=findViewById(R.id.attention_image);
        attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attentionImage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.callOnClick();
            }
        });

        sp=this.getSharedPreferences("LoginInfo",MODE_PRIVATE);
        loginName=sp.getString(Constants.LOGIN_NAME
                ,Constants.LOGIN_NAME);
        //获取ImageView控件
        imageView = (ImageView) findViewById(R.id.takePhoto_imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             AlertDialog.Builder builder = new AlertDialog.Builder(GetLocalPhotoActivity.this);
                                             builder.setTitle("选择方式");
                                             builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     dialog.dismiss();
////                                                     Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");// 判断存储卡是否可以用，可用进行存储
//                                                     Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// 判断存储卡是否可以用，可用进行存储
//
////                        if (hasSdcard()){
//                                                     SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
////                        }
//                                                     String filename = timeStampFormat.format(new Date());
//                                                     ContentValues values = new ContentValues();
//                                                     values.put(MediaStore.Audio.Media.TITLE, filename);
//                                                     photoUri1 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//                                                     ;
//                                                     intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri1);
//                                                     startActivityForResult(intent, TAKE_PHOTO);


                                                     Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                     startActivityForResult(it,Activity.DEFAULT_KEYS_DIALER);

//                                                     Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                                     // 指定照片存储位置为sd卡本目录下
//                                                     // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
//                                                     // File.separator为系统自带的分隔符 是一个固定的常量
//                                                     mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
//                                                     // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
//                                                     /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/
//                                                    imageUri = FileProvider.getUriForFile(GetLocalPhotoActivity.this,
//                                                             GetLocalPhotoActivity.this.getApplicationContext().getPackageName() +".my.provider",
//                                                             new File(mTempPhotoPath));
//                                                     //下面这句指定调用相机拍照后的照片存储的路径
//                                                     intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                                                     startActivityForResult(intentToTakePhoto, TAKE_PHOTO);
//

                                                 }
                                             });
                                             builder.setNeutralButton("相册", new DialogInterface.OnClickListener(){
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which){
                                                     dialog.dismiss();
                                                     Intent intent = new Intent(Intent.ACTION_PICK);
                                                     intent.setType("image/*");
                                                     startActivityForResult(intent, GET_PHOTO);
                                                 }
                                             });
                                             builder.create().show();

                                         }
                                     });

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
        type_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_type_selecter.callOnClick();
            }
        });
        clearContent=findViewById(R.id.clear_content);
        clearContent.setOnLongClickListener(new  View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dynamicContentText.setText("");
                return true;

            }
        });

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

                if(illegal){
                    detectText(dynamicContentText.getText().toString());//输入内容敏感词检测
                    return;
                }
                illegal=true;

                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(GetLocalPhotoActivity.this,"您暂时不能发言，请联系挂管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                try{
//            有可能在选择类型的时候，只选择了两个层级，array大小为2
                    Log.d(TAG, "typeArray[2]=: "+typeArray[2]);
                }catch(Exception e){
                    Toast.makeText(GetLocalPhotoActivity.this,"请选择完整的宠物类型^_^",Toast.LENGTH_SHORT).show();
                    return;
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
                if(InputTextTypeUtil.isNumric(WEIGHT)&&InputTextTypeUtil.isNumric(AGE)){
//                    Toast.makeText(GetLocalPhotoActivity.this, "请输入整数的体重（kg）或宠物年龄（月）", Toast.LENGTH_LONG).show();
                }




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
                Bitmap bitmap;


//                if(data==null){
//                    Log.d(TAG, "onActivityResult: data==null");
//                    return;
//                }else{
//                    Log.d(TAG, "onActivityResult:data!=null ");
//                }
//                Bundle extras=null ;
//                try{extras= data.getExtras();}catch(Exception e){
//                    e.printStackTrace();
//                }
//                if(null!=extras){
//                    Log.i("bb","isNull:"+(null==extras));
//                    bitmap = (Bitmap) extras.get("data");
//                    Log.d(TAG,"拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                    Matrix matrix = new Matrix();
//                    matrix.postScale((float) 288 / bitmap.getWidth(), (float) 288/ bitmap.getWidth()); // 长和宽放大缩小的比例
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
//                    Log.d(TAG,"裁剪后拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                    imageView.setImageBitmap(bitmap);
//                    photoBitmap=bitmap;
//
//                }else{
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        bitmap = BitmapFactory.decodeFile(uri.getPath());
//                        Log.d(TAG,"拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                        Matrix matrix = new Matrix();
//                        matrix.postScale((float) 288 / bitmap.getWidth(), (float) 288/ bitmap.getWidth()); // 长和宽放大缩小的比例
//                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
//                        Log.d(TAG,"裁剪后拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                        imageView.setImageBitmap(bitmap);
//                        photoBitmap=bitmap;
//
//                    }
//                }


                Bundle bundle = data.getExtras();   //获取data数据集合
                bitmap = (Bitmap) bundle.get("data");        //获得data数据

//                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                    Log.d(TAG,"拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                    Matrix matrix = new Matrix();
//                    matrix.postScale((float) 288 / bitmap.getWidth(), (float) 288/ bitmap.getWidth()); // 长和宽放大缩小的比例
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
//                    Log.d(TAG,"裁剪后拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
//                    imageView.setImageBitmap(bitmap);
//                    photoBitmap=bitmap;
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "onActivityResult: 拍照错误" );
//                }


                Log.d(TAG,"拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
                Matrix matrix = new Matrix();
                matrix.postScale((float) 288 / bitmap.getWidth(), (float) 288/ bitmap.getWidth()); // 长和宽放大缩小的比例
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
                Log.d(TAG,"裁剪后拍照,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
                imageView.setImageBitmap(bitmap);
                photoBitmap=bitmap;

            }

            if (requestCode == GET_PHOTO) {     //相册取图

                if(data!=null){
                    Uri photoUri=data.getData();
                    Log.d(TAG, "onActivityResult: "+photoUri.toString()+"\n"+photoUri.getPath());
                    String photoPath=photoUri.getPath();
                }

                ContentResolver contentResolver = getContentResolver();
                try {

                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                    Log.d(TAG,"相册,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) 288 / bitmap.getWidth(), (float) 288/ bitmap.getWidth()); // 长和宽放大缩小的比例
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
                    Log.d(TAG,"裁剪后相册,宽="+bitmap.getWidth()+"高="+bitmap.getHeight());
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GetLocalPhotoActivity.this,"发布成功！",Toast.LENGTH_SHORT).show();
//                            GetLocalPhotoActivity.this.finish();
                        }
                    });
                    Message message=Message.obtain(handler,1,2,3,1);
                    message.sendToTarget();
                    return 1;
                }
                else if(resCode.equals("-2"))//禁言
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GetLocalPhotoActivity.this,"你暂时不能说话，请联系管理员！",Toast.LENGTH_SHORT).show();

                        }
                    });
                    return -2;
                }
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GetLocalPhotoActivity.this,"发布失败！",Toast.LENGTH_SHORT).show();

                    }
                });
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.release_dynamic,menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }


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
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    敏感词检测
    public void detectText(String str){
        final String URL="https://aip.baidubce.com/rest/2.0/antispam/v2/spam";
        final String access_token="24.32dfbb589c567874163b3fa464b938e1.2592000.1556384670.282335-15876386";
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("access_token", access_token)
                            .add("content", str)
                            .build();
                    Request request = new Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url(URL).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    System.out.println(""+responseData);
                    Log.e("detectText", "run: "+ responseData);

                    Message message=Message.obtain(handler,33,33,33,responseData);
                    message.sendToTarget();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }





    int CAMERA_REQUEST_CODE=22;
//    申请权限
private void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
        // 向用户解释为什么需要这个权限
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setMessage("申请相机权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //申请相机权限
                            ActivityCompat.requestPermissions(GetLocalPhotoActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            //申请相机权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    } else {

    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                tvPermissionStatus.setTextColor(Color.GREEN);
//                tvPermissionStatus.setText("相机权限已申请");
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}
