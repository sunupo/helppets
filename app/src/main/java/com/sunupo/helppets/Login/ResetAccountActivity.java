package com.sunupo.helppets.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResetAccountActivity extends AppCompatActivity
{
	static private String random ="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_account);

		final EditText editTextUid = (EditText) findViewById(R.id.uid);
		final EditText editTextCode = (EditText) findViewById(R.id.code);
		final EditText editTextPsw = (EditText) findViewById(R.id.psw);
		final EditText editTextPswc = (EditText) findViewById(R.id.pswC);

		Button buttonGetcode = (Button) findViewById(R.id.getcode);
		Button buttonReset = (Button) findViewById(R.id.reset);

		buttonGetcode.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							URL url = new URL(Constants.httpip + "/getRandom");
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setConnectTimeout(5000);
							connection.setRequestMethod("GET");
							int responseCode = connection.getResponseCode();
							if (responseCode == 200)
							{
								BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
								String resCode = "";
								String readLine = null;
								while ((readLine = bReader.readLine()) != null)
								{
									resCode += readLine;
								}
								random = resCode;
								Looper.prepare();
								Toast.makeText(ResetAccountActivity.this, "风中吹来了验证码："+random, Toast.LENGTH_LONG).show();
								Looper.loop();
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

		buttonReset.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				final String uid = editTextUid.getText().toString();
				final String code = editTextCode.getText().toString();
				final String psw = editTextPsw.getText().toString();
				final String pswc = editTextPswc.getText().toString();

				if(uid.equals("")||code.equals("")||psw.equals("")||pswc.equals(""))
				{
					Toast.makeText(ResetAccountActivity.this,"完整的输入信息，是一次新的生命旅程",Toast.LENGTH_LONG).show();
				}
				if(!(code.equals(random)))
				{
					Toast.makeText(ResetAccountActivity.this,"验证码被风吹散了吗",Toast.LENGTH_LONG).show();
				}
				else
				{
					if (psw.equals(pswc))
					{
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								switch (resetUser(uid, psw))
								{
									case 1:
										Looper.prepare();
										Toast.makeText(ResetAccountActivity.this, "重设完成，欢迎您回家", Toast.LENGTH_LONG).show();
										Intent intent1 = new Intent(ResetAccountActivity.this, LoginActivity.class);
										startActivity(intent1);
										finish();
										Looper.loop();
										break;
									case -2:
										Looper.prepare();
										Toast.makeText(ResetAccountActivity.this, "帐号不存在，请您检查", Toast.LENGTH_LONG).show();
										Looper.loop();
										break;
									case -1:
										Looper.prepare();
										Toast.makeText(ResetAccountActivity.this, "网络突然抖动了一下，您可以再试试", Toast.LENGTH_LONG).show();
										Looper.loop();
										break;
								}
							}
						}).start();
					}
					else if (!(psw.equals(pswc)))
					{
						Toast.makeText(ResetAccountActivity.this, "密码，一个就够了", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(ResetAccountActivity.this, "网络突然抖动了一下，您可以再试试", Toast.LENGTH_LONG).show();
					}
				}

			}
		});
	}

	public int resetUser(String uid, String psw)
	{
		try
		{
			URL url = new URL(Constants.httpip + "/resetAccount?uid=" + uid + "&psw=" + psw);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if (responseCode == 200)
			{
				BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String resCode = "";
				String readLine = null;
				while ((readLine = bReader.readLine()) != null)
				{
					resCode += readLine;
				}
				if(resCode.equals("1"))
				{
					return 1;
				}
				else if(resCode.equals("-2"))
				{
					return  -2;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(ResetAccountActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}

}
