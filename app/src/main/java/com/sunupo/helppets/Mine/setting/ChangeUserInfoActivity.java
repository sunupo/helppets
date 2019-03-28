package com.sunupo.helppets.Mine.setting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.ReleaseDynamic.GetLocalPhotoActivity;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Base64Coder;
import com.sunupo.helppets.util.Constants;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.utils.StringUtils;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunupo.helppets.util.App.loginUserInfo;

public class ChangeUserInfoActivity extends AddressBaseActivity {

    private final String TAG="ChangeUserInfoActivity";
    private final String Separatior="-";
    private final String Separatior2=" ";

    
    ImageView imageView;
    TextView username,loginName,birthday,address,phone,qq,weChat;
    String addressFormatStr="",birthdayFormatStr="";
    RadioGroup sexRadioGroup,workRadioGroup,salaryRadioGroup,educationRadioGroup,marryRadioGroup,childrenRadioGroup;

    Button submit;

    DatePickerDialog datePickerDialog;
    PickerView pickerView;
    PickerData data;

    private Bitmap photoBitmap;//记录拍照或者相册照片的bitmap
    private int TAKE_PHOTO=1;   //拍照
    private int GET_PHOTO=2;    //取照片
    private Uri photoUri1;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Toast.makeText(ChangeUserInfoActivity.this,"信息修改成功^_^",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -1:
                    Toast.makeText(ChangeUserInfoActivity.this,"信息修改失败(灬ꈍ ꈍ灬)",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(ChangeUserInfoActivity.this,"头像上传成功^_^",Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(ChangeUserInfoActivity.this,"头像修改失败(灬ꈍ ꈍ灬)",Toast.LENGTH_SHORT).show();
                    break;
                case 99:

                    Gson gson = new Gson();
                    loginUserInfo = gson.fromJson((String)msg.obj, UserInfo.class);
                    break;

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        //        requestPermission();
//        if (ContextCompat.checkSelfPermission(ChangeUserInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(ChangeUserInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }

        if (ContextCompat.checkSelfPermission(ChangeUserInfoActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限。
            PermissionGen.needPermission(this, 200, Manifest.permission.CAMERA);
        } else {
            // 有权限了，直接使用相机。

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("修改资料");
        allBind();
        initCityPickViewDatePichView();
        allSet();

    }

    private void allSet(){
        Glide.with(this).load(Constants.httpip+"/"+App.loginUserInfo.getLogo()).into(imageView);
        loginName.setText(App.loginUserInfo.getLoginName());
        String[] temp=App.loginUserInfo.getBirthday().split(Separatior);
        birthday.setText(temp[0]+"年"+temp[1]+"月"+temp[2]+"日");
        birthdayFormatStr=App.loginUserInfo.getBirthday();

        address.setText(App.loginUserInfo.getProvince()+Separatior+App.loginUserInfo.getCity());
        addressFormatStr=App.loginUserInfo.getProvince()+Separatior+App.loginUserInfo.getCity();

        phone.setText(App.loginUserInfo.getPhone());
        qq.setText(App.loginUserInfo.getQq());
        weChat.setText(App.loginUserInfo.getWechat());
        if ((App.loginUserInfo.getSex().equals("男"))) {
            sexRadioGroup.check(R.id.sex_male);
        } else {
            sexRadioGroup.check(R.id.sex_female);
        }

        if ((App.loginUserInfo.getWorking().equals("上班"))) {
            workRadioGroup.check(R.id.work_ing);
        } else if((App.loginUserInfo.getWorking().equals("待就业"))) {
            workRadioGroup.check(R.id.work_to);
        }else{
            workRadioGroup.check(R.id.work_ed);
        }


        switch(App.loginUserInfo.getSalary()){
            case 3000:
                salaryRadioGroup.check(R.id.low_salary);
                break;
            case 5000:
                salaryRadioGroup.check(R.id.some_low_salary);
                break;
            case 8000:
                salaryRadioGroup.check(R.id.some_high_salary);
                break;
            case 10000:
                salaryRadioGroup.check(R.id.high_salary);
                break;
        }

        switch(App.loginUserInfo.getEducation()){
            case "小学":
                educationRadioGroup.check(R.id.primary);
                break;
            case "中学":
                educationRadioGroup.check(R.id.middle);
                break;
            case "大学":
                educationRadioGroup.check(R.id.college);
                break;
        }

        switch(App.loginUserInfo.getMarried()){
            case "已婚":
                marryRadioGroup.check(R.id.married);
                break;
            case "未婚":
                marryRadioGroup.check(R.id.unmarried);
                break;
        }

        switch(App.loginUserInfo.getHasChildren()){
            case "有":
                childrenRadioGroup.check(R.id.has_children);
                break;
            case "未婚":
                childrenRadioGroup.check(R.id.no_children);
                break;
        }
    }
    private void allBind(){
        initProvinceDatas();
        imageView=findViewById(R.id.logo);
        username=findViewById(R.id.user_name);
        loginName=findViewById(R.id.login_name);
        birthday=findViewById(R.id.birthday);
        address=findViewById(R.id.address);

        phone=findViewById(R.id.phone);
        qq=findViewById(R.id.qq);
        weChat=findViewById(R.id.we_chat);

//        phone.clearFocus();
//        phone.setSelected(false);
//        qq.clearFocus();
//        qq.setSelected(false);
//        weChat.clearFocus();
//        weChat.setSelected(false);
        sexRadioGroup=findViewById(R.id.sex_radio_group);
        workRadioGroup=findViewById(R.id.work_radio_group);
        salaryRadioGroup=findViewById(R.id.salary_radio_group);
        educationRadioGroup=findViewById(R.id.education_radio_group);
        marryRadioGroup=findViewById(R.id.marry_radio_group);
        childrenRadioGroup=findViewById(R.id.children_radio_group);

        submit=findViewById(R.id.submit);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUserInfoActivity.this);
                builder.setTitle("选择方式");
                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");// 判断存储卡是否可以用，可用进行存储
//                        if (hasSdcard()){
                        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//                        }
                        String filename = timeStampFormat.format(new Date());
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Audio.Media.TITLE, filename);
                        photoUri1 = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri1);
                        startActivityForResult(intent, TAKE_PHOTO);

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

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/27/2019 检查所有项目是否写完，联系方式没写的话，上传一个“无”
                checkGroup();
                getUserInfoJson(App.loginUserInfo.getLoginName());//刷新用户信息
                }
            });
    }


    /**
     * 检查资料的输入情况，然后上传
     */
    private void checkGroup(){
        Log.d(TAG, "checkGroup: ");
        String sexStr="",birthdayStr="",workStr="",salaryStr="",educationStr="",marryStr="",childrenStr="",phoneStr="",qqStr="",weChatStr="";
        String[] addressArray={"北京","北京","北京"};
//        if(photoBitmap==null){
//            Toast.makeText(ChangeUserInfoActivity.this,"请上传头像^_^",Toast.LENGTH_SHORT).show();
//        }
        if(TextUtils.isEmpty(birthday.getText().toString())){
            Toast.makeText(ChangeUserInfoActivity.this,"生日未选择^_^",Toast.LENGTH_SHORT).show();
            return;
        }else{
            birthdayStr=birthdayFormatStr;
        }
        if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(ChangeUserInfoActivity.this,"地址未选择^_^",Toast.LENGTH_SHORT).show();
            return;
        }
            try{
//            有可能在选择地址的时候，只选择了两个层级，array大小为2
                addressArray=addressFormatStr.split(Separatior);
                Log.d(TAG, "checkGroup: "+addressArray[2]);
            }catch(Exception e){
                Toast.makeText(ChangeUserInfoActivity.this,"请选择完整的地址^_^",Toast.LENGTH_SHORT).show();
                return;
            }

        if(TextUtils.isEmpty(phone.getText().toString())){
            phoneStr="无";

        }
        if(TextUtils.isEmpty(qq.getText().toString())){
            qqStr="无";

        }
        if(TextUtils.isEmpty(weChat.getText().toString())){
            weChatStr="无";

        }
        switch(sexRadioGroup.getCheckedRadioButtonId()){
            case R.id.sex_male:
                sexStr="男";
                break;
            case R.id.sex_female:
                sexStr="女";
                break;
        }

        switch(workRadioGroup.getCheckedRadioButtonId()){
            case R.id.work_ing:
                workStr="上班";
                break;
            case R.id.work_ed:
                workStr="退休";
                break;
            case R.id.work_to:
                workStr="待就业";
                break;
        }
        switch(salaryRadioGroup.getCheckedRadioButtonId()){
            case R.id.low_salary:
                salaryStr="3000";
                break;
            case R.id.some_low_salary:
                salaryStr="5000";
                break;
            case R.id.some_high_salary:
                salaryStr="8000";
                break;
            case R.id.high_salary:
                salaryStr="10000";
                break;
        }
        switch(educationRadioGroup.getCheckedRadioButtonId()){
            case R.id.primary:
                educationStr="小学";
                break;
            case R.id.middle:
                educationStr="中学";
                break;
            case R.id.college:
                educationStr="大学";
                break;
        }
        switch(marryRadioGroup.getCheckedRadioButtonId()){
            case R.id.married:
                marryStr="已婚";
                break;
            case R.id.unmarried:
                marryStr="未婚";
                break;
        }
        switch(childrenRadioGroup.getCheckedRadioButtonId()){
            case R.id.has_children:
                childrenStr="有";
                break;
            case R.id.no_children:
                childrenStr="无";
                break;
        }

//        上传资料文本
//         String sexStr,birthdayStr,workStr,salaryStr,educationStr,marryStr,childrenStr,phoneStr,qqStr,weChatStr;
//        String[] addressArray;
        updateUserInfoContent(sexStr,birthdayStr,workStr,salaryStr,educationStr,marryStr,childrenStr,addressArray[0],addressArray[1]+Separatior+addressArray[2],
                phoneStr,qqStr,weChatStr);
//        上传头像
        if(photoBitmap!=null){
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    updateUserInfoLogo(photoBitmap,App.loginUserInfo.getUserId());
                }
            });
        }


    }


    private void initCityPickViewDatePichView(){


        //选择器数据实体类封装
        data=new PickerData();
        //设置数据，有多少层级自己确定
        data.setFirstDatas(mProvinceDatas);
        data.setSecondDatas(mCitisDatasMap);
        data.setThirdDatas(mDistrictDatasMap);
        data.setFourthDatas(new HashMap<String, String[]>());
        //设置初始化默认显示的三级菜单(此方法可以选择传参数量1到4个)
//        data.setInitSelectText("河北省","石家庄市","平山县");
        //初始化选择器
        pickerView=new PickerView(this,data);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示选择器
                pickerView.show(address);
            }
        });
        //选择器完成三级选择后点击回调
        pickerView.setOnPickerClickListener(new OnPickerClickListener() {
            //选择列表时触发的事件
            @Override
            public void OnPickerClick(PickerData pickerData) {
                //想获取单个选择项 PickerData内也有方法（弹出框手动关闭）
                address.setText(pickerData.getFirstText()+Separatior2+pickerData.getSecondText()+Separatior2+pickerData.getThirdText());
                addressFormatStr=pickerData.getFirstText()+Separatior+pickerData.getSecondText()+Separatior+pickerData.getThirdText();

                pickerView.dismiss();//关闭选择器
            }
            //点击确定按钮触发的事件（自动关闭）
            @Override
            public void OnPickerConfirmClick(PickerData pickerData) {
                address.setText(pickerData.getFirstText()+Separatior2+pickerData.getSecondText()+Separatior2+pickerData.getThirdText());
                addressFormatStr=pickerData.getFirstText()+Separatior+pickerData.getSecondText()+Separatior+pickerData.getThirdText();
            }
        });

        datePickerDialog=new DatePickerDialog(this);
        datePickerDialog.setTitle("时间");
        datePickerDialog.setIcon(R.drawable.rc_ext_realtime_default_avatar);
        datePickerDialog.setMessage("请选择出生日期");
        datePickerDialog.setCancelable(false);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday.setText(year+"年"+month+"月"+dayOfMonth+"日");
                birthdayFormatStr=year+Separatior+month+Separatior+dayOfMonth;
                datePickerDialog.dismiss();
            }
        });
    }

    //接受回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {  //回传值接受成功

            if (requestCode == TAKE_PHOTO) {    //拍照取图

                Bundle bundle = data.getExtras();   //获取data数据集合
                Bitmap bitmap = (Bitmap) bundle.get("data");        //获得data数据
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


    /**
     * 上传用户文本内容
     * //        上传资料文本
     * //         String sexStr,birthdayStr,workStr,salaryStr,educationStr,marryStr,childrenStr,phoneStr,qqStr,weChatStr;
     * //        String[] addressArray;
     */
    private void updateUserInfoContent(final String sex,final String birthday,final String work,final String salary,final String education,final String marry
            ,final String children,final String province,final String city,final String phone,final String qq,final String weChat) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("sex", sex+"")
                            .add("birthday", birthday+"")
                            .add("work", work+"")
                            .add("salary", salary+"")
                            .add("education", education+"")
                            .add("marry", marry+"")
                            .add("children", children+"")
                            .add("province", province+"")
                            .add("city", city+"")
                            .add("phone", phone+"")
                            .add("qq", qq+"")
                            .add("weChat", weChat+"")

                            .add("userId", App.loginUserInfo.getUserId()+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/updateUserInfoContent").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);
                    Message message=Message.obtain(handler,1,Integer.parseInt(responseData),3,responseData);
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

    /**
     * 上传用户头像到服务端的根目录下的logo目录，文件名暂时为userId.bmp(eg： 1.bmp)
     * @param upbitmap
     * @param userId
     */
    public void updateUserInfoLogo(Bitmap upbitmap,int userId) {

        Log.d(TAG, "updateUserInfoLogo: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        upbitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] b = stream.toByteArray();
        /*
         * 将图片流以字符串形式存储下来,base64coder是一个常用的编码类， 经常用于各种网络数据传输时用来加密和解密
         * 用，用来确保数据的唯一性
         */
        String logo = new String(Base64Coder.encodeLines(b));
        HttpClient client = new DefaultHttpClient();
        // 设置上传参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("logo", logo));
        HttpPost post = new HttpPost(Constants.httpip+"/updateUserInfoLogo?userId="+userId);
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept", "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            // 加消息报头，进行gzip压缩，提高网络开销
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
            System.out.println(EntityUtils.toString(e));
            if (200 == response.getStatusLine().getStatusCode()) {
                System.out.println("上传完成");
            } else {
                System.out.println("上传失败");
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUserInfoJson(String loginName){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginName",loginName).build();
                    Request request = new Request.Builder().url(Constants.httpip + "/loginUserInfo").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: loginUserInfo= "+responseData);

                    Message message=Message.obtain(handler,99,2,3,responseData);
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
        return null;
    }
}
