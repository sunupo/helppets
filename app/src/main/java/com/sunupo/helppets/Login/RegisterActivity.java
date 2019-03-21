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

public class RegisterActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		final EditText editTextUid = (EditText) findViewById(R.id.uid);
		final EditText editTextPsw = (EditText) findViewById(R.id.psw);
		final EditText editTextPswc = (EditText) findViewById(R.id.pswC);

		Button buttonChange = (Button) findViewById(R.id.register);

		buttonChange.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				final String uid = editTextUid.getText().toString();
				final String psw = editTextPsw.getText().toString();
				final String pswc = editTextPswc.getText().toString();

				if(uid.equals("")||psw.equals("")||pswc.equals(""))
				{
					Toast.makeText(RegisterActivity.this,"完整的输入信息，是一次新的生命旅程",Toast.LENGTH_LONG).show();
				}
				else if (psw.equals(pswc))
				{
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							switch (regUser(uid, psw))
							{
								case 1:
									Looper.prepare();
									Toast.makeText(RegisterActivity.this, "注册成功，欢迎您的到来", Toast.LENGTH_LONG).show();
									Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
									startActivity(intent1);
									finish();
									Looper.loop();
									break;
								case -2:
									Looper.prepare();
									Toast.makeText(RegisterActivity.this, "帐号已经有人了，您可以另辟蹊径", Toast.LENGTH_LONG).show();
									Looper.loop();
									break;
								case -1:
									Looper.prepare();
									Toast.makeText(RegisterActivity.this, "网络突然抖动了一下，您可以再试试", Toast.LENGTH_LONG).show();
									Looper.loop();
									break;
							}
						}
					}).start();
				} else if (!(psw.equals(pswc)))
				{
					Toast.makeText(RegisterActivity.this, "密码，一个就够了", Toast.LENGTH_LONG).show();
				} else
				{
					Toast.makeText(RegisterActivity.this, "网络突然抖动了一下，您可以再试试", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public int regUser(String uid, String psw)
	{
		try
		{
			URL url = new URL(Constants.httpip + "/getRegister?uid=" + uid + "&psw=" + psw);
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
				if (resCode.equals("1"))
				{
					return 1;
				} else if (resCode.equals("-2"))
				{
					return -2;
				} else
				{
					return -1;
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
		Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
