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
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.R;
import com.sunupo.helppets.main.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunupo.helppets.util.MyApplication.loginUserInfo;


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

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            loginUserInfo = gson.fromJson((String)msg.obj, UserInfo.class);
            Log.d(TAG, "handleMessage: loginUserInfo="+loginUserInfo.toString());
            if(loginUserInfo.getSuccessCode()==1){
//                Looper.prepare();
				Log.d(TAG, "handleMessage: 登陆成功");
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("uid",uuid);
                intent.putExtra("ug",uug);
                intent.putExtra("psw",upsw);
                startActivity(intent);
//									finish();
//                Looper.loop();
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		setTitle("登陆");

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


                                    getUserInfoJson(uid);//需要在初始化handler之后使用，否者在message.sendToTarget()报空指针异常

/*                                    Looper.prepare();
									Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
									Intent intent = new Intent(LoginActivity.this,MainActivity.class);
									intent.putExtra("uid",uuid);
									intent.putExtra("ug",uug);
									intent.putExtra("psw",psw);
									startActivity(intent);
//									finish();
									Looper.loop();*/
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


    private String getUserInfoJson(String uid){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("uid",uid).build();
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
}
