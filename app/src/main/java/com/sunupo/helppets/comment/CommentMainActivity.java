package com.sunupo.helppets.comment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.FollowCollectFavorite;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.MyApplication;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * by moos on 2018/04/20
 */
public class CommentMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CommentMainActivity";
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";
    private Handler handler;
    int DYNAMIC_USER_ID;
    int DYNAMIC_ID;
    private final String followFlag="已关注";
    private final String collectFlag="已收藏";
    private final String favoriteFlag="已点赞";

    ImageView followImage,collectImage,favoriteImage;
    TextView  collectNum,favoriteNum;

    FollowCollectFavorite followCollectFavorite;

    final String COMMENT_DATA_URL=Constants.httpip + "/getCommentJson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_main);
        this.setTitle("详情");

        Intent intent=getIntent();

        DYNAMIC_USER_ID=intent.getIntExtra("DYNAMIC_USER_ID",-1);
        DYNAMIC_ID=intent.getIntExtra("DYNAMIC_ID",-1);


        followImage=findViewById(R.id.follow_image);
        favoriteImage=findViewById(R.id.favorite_image);
        favoriteNum=findViewById(R.id.favorite_num);
        collectImage=findViewById(R.id.collect_image);
        collectNum=findViewById(R.id.collect_num);

        View.OnClickListener followListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String FOLLOW=Constants.httpip+"/follow";
                switch (followImage.getImageAlpha()){
                    case 255://255完全不透明，代表已关注
                        followImage.setImageResource(R.mipmap.ic_launcher_person);
                        followImage.setImageAlpha(128);
                        sendRequestWithHttpURLConnectionFollow(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"0",FOLLOW);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"取消关注"+DYNAMIC_USER_ID);
                        break;

                    case 128://128代表没有关注

                        followImage.setImageResource(R.mipmap.ic_launcher_people);
                        followImage.setImageAlpha(255);
                        sendRequestWithHttpURLConnectionFollow(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"1",FOLLOW);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"关注"+DYNAMIC_USER_ID);
                        break;
                    default:
                        followImage.setImageResource(R.mipmap.ic_launcher_people);
                        followImage.setImageAlpha(255);
                        sendRequestWithHttpURLConnectionFollow(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"1",FOLLOW);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"关注"+DYNAMIC_USER_ID);
                        break;
                }
            }
        };
        followImage.setOnClickListener(followListener);

        View.OnClickListener collectListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL=Constants.httpip+"/collect";
                switch (collectImage.getImageAlpha()){
                    case 255://255代表收藏

                        collectImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
                        collectImage.setImageAlpha(128);
                        if(Integer.parseInt(collectNum.getText().toString())>0)
                            collectNum.setText((Integer.parseInt(collectNum.getText().toString())-1)+"");
                        sendRequestWithHttpURLConnectionCollect(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"取消收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    case 128://128代表没有收藏

                        collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        collectImage.setImageAlpha(255);
                        collectNum.setText((Integer.parseInt(collectNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionCollect(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    default:
                        collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        collectImage.setImageAlpha(255);
                        collectNum.setText((Integer.parseInt(collectNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionCollect(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                }
            }
        };
        collectImage.setOnClickListener(collectListener);

        View.OnClickListener favoriteListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String URL=Constants.httpip+"/favorite";
                switch (favoriteImage.getImageAlpha()){
                    case 255://255代表点赞

                        favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
                        favoriteImage.setImageAlpha(128);
                        if(Integer.parseInt(favoriteNum.getText().toString())>0)
                            favoriteNum.setText((Integer.parseInt(favoriteNum.getText().toString())-1)+"");
                        sendRequestWithHttpURLConnectionFavorite(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"取消点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    case 128://128代表没有点赞

                        favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        favoriteImage.setImageAlpha(255);
                        favoriteNum.setText((Integer.parseInt(favoriteNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionFavorite(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    default:
                        favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        favoriteImage.setImageAlpha(255);
                        favoriteNum.setText((Integer.parseInt(favoriteNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionFavorite(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+MyApplication.loginUserInfo.getLoginName()+"点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                }
            }
        };
        favoriteImage.setOnClickListener(favoriteListener);


        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        testJson=(String)msg.obj;
                        initView();
                        break;
                    case 2:
                        followCollectFavorite=new Gson().fromJson((String)msg.obj,FollowCollectFavorite.class);
                        Log.d(TAG, "handleMessage:followCollectFavorite= "+followCollectFavorite.toString());
                        initFollowCollectFavorite();
                }
            }
        };
        //todo 根据loginUserId，dynaicUserId,dynamicId去服务器获取数据,得到做新的followflag，collectflag，favoriteflag
        sendRequestWithHttpURLConnectionGetLatestFollowCollectFavorite(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID);
//        根据参数得到评论信息
        sendRequestWithHttpURLConnectionHaveParam(DYNAMIC_USER_ID,DYNAMIC_ID,COMMENT_DATA_URL);

    }

    private void initFollowCollectFavorite(){
        if(followFlag==followCollectFavorite.getFollowFlag()||followFlag.equals(followCollectFavorite.getFollowFlag())){
            followImage.setImageResource(R.mipmap.ic_launcher_people_round);
            followImage.setImageAlpha(255);
            Log.d(TAG, "favoriteImage:=255 ");
        }else {
            followImage.setImageResource(R.mipmap.ic_launcher_person_round);
            followImage.setImageAlpha(128);
            Log.d(TAG, "favoriteImage:=128 ");


        }
        if(collectFlag==followCollectFavorite.getCollectFlag()||collectFlag.equals(followCollectFavorite.getCollectFlag())){
            collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
            collectImage.setImageAlpha(255);
            Log.d(TAG, "collectImage:=255 ");

        }else{
            collectImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
            collectImage.setImageAlpha(128);
            Log.d(TAG, "collectImage:=128 ");

        }
        if(favoriteFlag==followCollectFavorite.getFavoriteFlag()||favoriteFlag.equals(followCollectFavorite.getFavoriteFlag())){
            favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
            favoriteImage.setImageAlpha(255);
            Log.d(TAG, "favoriteImage:=255 ");

        }else{
            favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
            favoriteImage.setImageAlpha(128);
            Log.d(TAG, "favoriteImage:=128 ");

        }
        collectNum.setText(followCollectFavorite.getCollectNum()+"");
        favoriteNum.setText(followCollectFavorite.getFavoriteNum()+"");

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        commentsList = generateTestData();
        initExpandableListView(commentsList);
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(CommentMainActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2018/04/20
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){

        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    // TODO: 3/16/2019 根据评论人的id，名字 ，往数据库写数据，Constants.httpip+"/setCommentData"
                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            " "+calendar.get(Calendar.HOUR_OF_DAY)+
                            ":"+calendar.get(Calendar.MINUTE)+
                            ":"+calendar.get(Calendar.SECOND);
                    CommentDetailBean detailBean = new CommentDetailBean(MyApplication.loginUserInfo.getLoginName(), commentContent,currentTime);
                    adapter.addTheCommentData(detailBean);
                    setCommentData(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,commentContent,currentTime);
                    Toast.makeText(CommentMainActivity.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(CommentMainActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    // TODO: 3/16/2019 回复人的信息 Constants.httpip+"/setCommentReplyData"
                    ReplyDetailBean detailBean = new ReplyDetailBean(MyApplication.loginUserInfo.getLoginName(),replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            " "+calendar.get(Calendar.HOUR_OF_DAY)+
                            ":"+calendar.get(Calendar.MINUTE)+
                            ":"+calendar.get(Calendar.SECOND);
//                    TODO 怎么获取当前的COMMENTID
//                    commentsList.get(position).getId();
                    try{
                        commentsList.get(position).getId();
                    }
                    catch(Exception e){
                        Log.e(TAG, "onClick: commentsList.get(position).getId()"+commentsList.get(position).getId());
                    }

                    setCommentReplyData(MyApplication.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,commentsList.get(position).getId()
                            ,MyApplication.loginUserInfo.getUserId(),replyContent,currentTime);
                    Toast.makeText(CommentMainActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CommentMainActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    //筛选获得动态dynamic的数据
    private void sendRequestWithHttpURLConnectionHaveParam(final int dynaimcUserId ,final int dynamicId,final String commentDataUrl) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("userId",dynaimcUserId+"").add("dynamicId",dynamicId+"").build();//
                    Request request = new Request.Builder().url(commentDataUrl).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);
//                    parseJSONWithJSONObject(responseData);
//                    responseData=new String(responseData.getBytes("iso8859-1"),"utf-8");

                    Message message=Message.obtain(handler,1,2,3,responseData);
                    message.sendToTarget();
                    testJson=responseData;
                    Log.d(TAG, "run:testJson "+testJson);
//                    initView();
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
    private void setCommentData(final int fromUid,final int toUid,final int dynamicId,final String commentContent,final String commentTime){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("fromUid",fromUid+"")
                            .add("toUid",toUid+"")
                            .add("dynamicId",dynamicId+"")
                            .add("commentContent",commentContent+"")
                            .add("commentTime",commentTime+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/setCommentData").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: setCommentData responseData="+responseData);
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

    private void setCommentReplyData(final int fromUid,final int toUid,final int dynamicId
            ,final int commentId,final int replyUid,final String replyContent,final String replyTime){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("fromUid",fromUid+"")
                            .add("toUid",toUid+"")
                            .add("dynamicId",dynamicId+"")
                            .add("commentId",commentId+"")
                            .add("replyUid",replyUid+"")
                            .add("replyContent",replyContent+"")
                            .add("replyTime",replyTime+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/setCommentReply").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: setCommentData responseData="+responseData);
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
    private void sendRequestWithHttpURLConnectionGetLatestFollowCollectFavorite(final int loginUserId ,final int dynamicUserId,final int dynamicId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/getLatestFollowCollectFavorite").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    Message message=Message.obtain(handler,2,5,6,responseData);
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


    private void sendRequestWithHttpURLConnectionFollow(final int loginUserId ,final int dynamicUserId
            ,final String followFlag,final String url) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("followFlag",followFlag)
                            .build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "run:response.code= "+response.code());

                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

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

    private void sendRequestWithHttpURLConnectionCollect(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String collectFlag,final String url) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"")
                            .add("collectFlag",collectFlag).build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

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
    private void sendRequestWithHttpURLConnectionFavorite(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String favoriteFlag,final String url) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"")
                            .add("collectFlag",favoriteFlag).build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

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

}
//    private String testJson = "{\n" +
//            "\t\"code\": 1000,\n" +
//            "\t\"message\": \"查看评论成功\",\n" +
//            "\t\"data\": {\n" +
//            "\t\t\"total\": 3,\n" +
//            "\t\t\"list\": [{\n" +
//            "\t\t\t\t\"id\": 42,\n" +
//            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 1,\n" +
//            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
//            "\t\t\t\t\"replyList\": [{\n" +
//            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
//            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\t\"id\": 40,\n" +
//            "\t\t\t\t\t\"commentId\": \"42\",\n" +
//            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
//            "\t\t\t\t\t\"status\": \"01\",\n" +
//            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
//            "\t\t\t\t}]\n" +
//            "\t\t\t},\n" +
//            "\t\t\t{\n" +
//            "\t\t\t\t\"id\": 41,\n" +
//            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 1,\n" +
//            "\t\t\t\t\"createDate\": \"一天前\",\n" +
//            "\t\t\t\t\"replyList\": [{\n" +
//            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
//            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\t\"commentId\": \"41\",\n" +
//            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
//            "\t\t\t\t\t\"status\": \"01\",\n" +
//            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
//            "\t\t\t\t}]\n" +
//            "\t\t\t},\n" +
//            "\t\t\t{\n" +
//            "\t\t\t\t\"id\": 40,\n" +
//            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
//            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
//            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
//            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
//            "\t\t\t\t\"replyTotal\": 0,\n" +
//            "\t\t\t\t\"createDate\": \"三天前\",\n" +
//            "\t\t\t\t\"replyList\": []\n" +
//            "\t\t\t}\n" +
//            "\t\t]\n" +
//            "\t}\n" +
//            "}";