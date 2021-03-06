package com.sunupo.helppets.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.R;
import com.sunupo.helppets.main.MainActivity;
import com.sunupo.helppets.util.GetToken;
import com.sunupo.helppets.util.TextAnalysis;
import com.sunupo.helppets.util.TokenReturnBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunupo.helppets.util.App.loginUserInfo;


public class LoginActivity extends AppCompatActivity
{
	private final static String TAG="LoginActivity";
	static private String uuid;
	static private String uug;
	static private String upsw;

	static String SAVE_PWD="SAVE_PWD";
	static String LOGIN_ACCOUNT="LOGIN_ACCOUNT";
	static String LOGIN_PWD="LOGIN_PWD";

	private CheckBox savePwd;//保存密码
	private SharedPreferences sp=null;

	private String firstLogin="true";

	// TODO: 3/23/2019 首先判断是否第一次登陆
//	如果是第一次登陆，登陆成功过后（用的是原来的api，只是判断用户名密码是否匹配）
// 		把newUserLoginName与laseUserLoginName设为相等，
//		从网络中获取loginUserInfo（新的api，得到当前用户的完整信息）：getUserInfoJson(uid)，再发送message.what=1到handler
// 		从网络中获取token，发送
//

	//   初次在密码验证正确之后调用             GetToken.getUserToken("2","zhangsan","",handler);
//todo 第二次登陆从sp读取token
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what){
        		case 7:
        			if(firstLogin.equals("true")) {
        				//写入token和FIRST_LOGIN="FALSE"
						sp.edit().putString("FIRST_LOGIN", "false")
								.putString("TOKEN",((TokenReturnBean)msg.obj).getToken())
								.putString("LAST_LOGIN_USER_NAME",loginUserInfo.getLoginName())
								.putString("NEW_LOGIN_USER_NAME",loginUserInfo.getLoginName())
								.commit();
						connect(((TokenReturnBean) msg.obj).getToken());
						Log.d(TAG, "handleMessage: ((TokenReturnBean) msg.obj).getToken()="+((TokenReturnBean) msg.obj).getToken());
					}
        			else if(firstLogin.equals("false")){

						sp.edit().putString("NEW_LOGIN_USER_NAME",loginUserInfo.getLoginName()).commit();
//                        sp.edit().putString("TOKEN",((TokenReturnBean)msg.obj).getToken());
        				if(sp.getString("NEW_LOGIN_USER_NAME","").equals(sp.getString("LAST_LOGIN_USER_NAME",""))){

        					Log.d(TAG, "不是首次登陆，与上次登录用户相同="+sp.getString("TOKEN",""));
							connect(sp.getString("TOKEN",""));

						}else if(!sp.getString("NEW_LOGIN_USER_NAME","").equals(sp.getString("LAST_LOGIN_USER_NAME",""))){

							sp.edit().putString("LAST_LOGIN_USER_NAME",sp.getString("NEW_LOGIN_USER_NAME","")).commit();

        					Log.d(TAG, "handleMessage: 本次用户和上次用户不一样，本次用户这也是第一次登陆，需要重新获取token.");
							Log.d(TAG, "handleMessage: 然后通过message.what=1,会输出日志中（不是首次登陆，与上次登录用户相同=）");
                            sp.edit().putString("FIRST_LOGIN", "true");
                            firstLogin="true";
							GetToken.getUserToken(loginUserInfo.getLoginName()+"",loginUserInfo.getLoginName(),loginUserInfo.getLogo(),handler);
						}
					}
//        			else(读取数据库的token);
        			break;
				case 2:

					App.signText=((String)(msg.obj));
					Log.d(TAG, "handleMessage: App.signText"+App.signText);
					break;
        		case 1 :

					Gson gson = new Gson();
					loginUserInfo = gson.fromJson((String)msg.obj, UserInfo.class);

					getSignName();//在得到loginUserInfo之后根据userid获取签名

					sp.edit().putString("USER_LOGO",loginUserInfo.getLogo());

					Log.d(TAG, "handleMessage: loginUserInfo="+loginUserInfo.toString());
					if(loginUserInfo.getSuccessCode()==1){
						try{
							firstLogin=sp.getString("FIRST_LOGIN","true");
							if(firstLogin.equals("true")){
								Log.d(TAG, "run: 第一次登陆，从网路中获取token");
								GetToken.getUserToken(loginUserInfo.getLoginName()+"",loginUserInfo.getLoginName(),Constants.httpip+"/"+loginUserInfo.getLogo(),handler);
							}else if(firstLogin.equals("false")){
								Message message=Message.obtain(handler,7,2,3,"false");
								message.sendToTarget();
							}
						}catch(Exception e){
							e.printStackTrace();
							Log.d(TAG, "onCreate: 读取是第一次登陆失败 FIRST_LOGIN=false");
						}


//                Looper.prepare();
						Log.d(TAG, "handleMessage: 登陆成功,即将进入");
						Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						intent.putExtra("uid",uuid);
						intent.putExtra("ug",uug);
						intent.putExtra("psw",upsw);
						startActivity(intent);
									finish();
//                Looper.loop();
					}
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
							Toast.makeText(LoginActivity.this,Constants.dtArr[i]+"的概率为"+Integer.parseInt(jsonObject2.getString("score"))*100.0+"%\n关键字为"+jsonObject2.getString("hit"),Toast.LENGTH_LONG).show();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;



			}
        }
    };
	private void connect(final String token) {

//		if (getApplicationInfo().packageName.equals(App.getProcessName())) {
			Log.d(TAG, "before connect: ");
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.d(TAG, "onTokenIncorrect: ");
                }

                @Override
                public void onSuccess(String s) {
//                    TODO init()-->connect()-->initConversationList()-->startConversationList()
                    Log.d(TAG, "onSuccess: RongIM.connect");

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d(TAG, "onError: ");
                }
            });
//            不要使用 RongIMClient 实例去调用相关接口，否则会导致 UI 显示异常。
//			Thread thread=new Thread(new Runnable() {
//				@Override
//				public void run() {
//					Log.d(TAG, "begin RongIM.connect thread");
//
//
//				}
//			});
//			thread.start();
//			try{
//				thread.join();
//			}catch (Exception e){
//				e.printStackTrace();
//			}
			Log.d(TAG, " after RongIM.connect");
//            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
//
//                /**
//                 * Token 错误。可以从下面两点检查
//                 * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
//                 *  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
//                 */
//                @Override
//                public void onTokenIncorrect() {
//                    Log.d(TAG, "onTokenIncorrect: ");
//                }
//
//                /**
//                 * 连接融云成功
//                 *  @param userid 当前 token 对应的用户 id
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    Log.d(TAG, "--onSuccess" + userid);
//                    startActivity(new Intent(MainActivity.this, ConversationListActivity.class));
//                    finish();
//                }
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//
//                    Log.d(TAG, "onError: "+errorCode);
//                }
//            });
//		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		setTitle("登陆");

//		detectText("打倒共产党,打倒毛主席，打死你个龟孙");


		ImageView loginLogo=findViewById(R.id.login_logo);
		try{
			Glide.with(this).load(Constants.httpip+"/"+App.loginUserInfo.getLogo());
		}catch(Exception e){
			e.printStackTrace();
			Log.d(TAG, "onCreate: 头像没有缓存");
		}

		sp=this.getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);



		final Button buttonLogin = (Button) findViewById(R.id.button_login);
		Button buttonReg = (Button) findViewById(R.id.button_reg);
		Button buttonReset = (Button) findViewById(R.id.button_forget);
		final EditText editTextAccount = (EditText) findViewById(R.id.account);
		final EditText editTextPassword = (EditText) findViewById(R.id.password);

		savePwd=findViewById(R.id.save_pwd);
//		根据sharedpreferences读取用户名密码
		if(sp.getBoolean(SAVE_PWD,false)){
			savePwd.setChecked(true);
		}else {
			savePwd.setChecked(false);
		}
		editTextAccount.setText(sp.getString(LOGIN_ACCOUNT,""));
		editTextPassword.setText(sp.getString(LOGIN_PWD,""));

		buttonLogin.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager
						.getActiveNetworkInfo();
				if (null == networkInfo || !networkInfo.isConnected())
				{
					Toast.makeText(LoginActivity.this,
							"网络不可用", Toast.LENGTH_SHORT)
							.show();
					return;
				}


				final String uid = editTextAccount.getText().toString();
				final String psw = editTextPassword.getText().toString();

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							if(uid.equals(""))
							{
								Looper.prepare();
								Toast.makeText(LoginActivity.this,"请您输入账号名，然后再试试",Toast.LENGTH_LONG).show();
								Looper.loop();
							}
							else if(psw.equals(""))
							{
								Looper.prepare();
								Toast.makeText(LoginActivity.this,"请您输入密码，然后再试试",Toast.LENGTH_LONG).show();
								Looper.loop();
							}
							else
							{
								if(loginByGet(uid,psw))
								{

									if (savePwd.isChecked()) {
										SharedPreferences.Editor editor = sp.edit();
										//新建一个Editor对象来存储键值对用户名和密码
										editor.putString(LOGIN_ACCOUNT, uid);
										editor.putString(LOGIN_PWD,psw);
										editor.putBoolean(SAVE_PWD, savePwd.isChecked());

										editor.putString(Constants.LOGIN_NAME,uid);//记录全局用户登陆名
										//提交数据
										editor.commit();
									} else {
										sp.edit().putString(Constants.LOGIN_NAME,uid);//记录全局用户登陆名

										sp.edit().remove(LOGIN_ACCOUNT).commit();
										sp.edit().remove(LOGIN_PWD).commit();
										sp.edit().remove(SAVE_PWD);
									}

                                    getUserInfoJson(uid);//需要在初始化handler之后使用，否者在message.sendToTarget()报空指针异
									// TODO: 3/26/2019 更改服务端的代码，在登陆成功时，添加登录次数loginCount



///*                                    Looper.prepare();
//									Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//									Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//									intent.putExtra("uid",uuid);
//									intent.putExtra("ug",uug);
//									intent.putExtra("psw",psw);
//									startActivity(intent);
////									finish();
//									Looper.loop();*/
								}
								else
								{
									Looper.prepare();
									Toast.makeText(LoginActivity.this,"登录失败，请您再试试",Toast.LENGTH_LONG).show();
									Looper.loop();
								}
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		buttonReg.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});

		buttonReset.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(LoginActivity.this,ResetAccountActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public static boolean loginByGet(String uid, String psw)
	{
		Log.d(TAG, "loginByGet: ");
		try
		{

//192.168.0.105";/loginAction?uid="+uid+"&psw="+psw+"&newpsw=")
			URL url = new URL(Constants.httpip+"/loginAction?uid="+uid+"&psw="+psw+"&newpsw=");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");

			connection.getResponseMessage();
			int responseCode = connection.getResponseCode();
			if(responseCode == 200)
			{
				BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String resCode = "";
				String readLine = null;
				while((readLine = bReader.readLine()) != null)
				{
					resCode += readLine;
				}
				if(resCode.equals("1"))
				{
					uuid = uid;
					uug = "0";
                    upsw=psw;
					return true;
				}
				else if(resCode.equals("2"))
				{
					uuid = uid;
					uug = "1";
                    upsw=psw;
                    return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次返回键离开这里", Toast.LENGTH_LONG).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

                    Message message=Message.obtain(handler,1,2,3,responseData);
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

	private  void getSignName(){

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					OkHttpClient client = new OkHttpClient();
					RequestBody requestBody = new FormBody.Builder()
							.add("userId",App.loginUserInfo.getUserId()+"").build();
					Request request = new Request.Builder().url(Constants.httpip + "/getSignName").post(requestBody).build();
					Response response = client.newCall(request).execute();
					String responseData = response.body().string();
					Log.d(TAG, "run: getSignName= "+responseData);

					Message message=Message.obtain(handler,2,2,3,responseData);
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

	// TODO: 3/29/2019 在登录界面测试 关键字屏蔽，自动调用函数，比较方便

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
	{

	}
}
