package com.sunupo.helppets.conversation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.sunupo.helppets.R;

public class ConversationActivity extends FragmentActivity {
//    private TextView mTextView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.conversation);
//        mTextView = (TextView) findViewById(R.id.title);
//        mTextView.setText(getIntent().getData().getQueryParameter("title"));
        Log.e("type", "type is:" + getIntent().getData().getPath());
    }
}
