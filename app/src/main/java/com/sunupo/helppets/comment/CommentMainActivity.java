package com.sunupo.helppets.comment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.bean.FollowCollectFavorite;
import com.sunupo.helppets.user.UserMainPageActivity;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.DownloadImageTask;


import java.io.IOException;
import java.util.ArrayList;
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
    private TextView bt_comment,deleteDynamic;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private String testJson=Constants.TEST_JSON;

    int DYNAMIC_USER_ID;
    int DYNAMIC_ID;
    private final String followFlag="已关注";
    private final String collectFlag="已收藏";
    private final String favoriteFlag="已点赞";

    ImageView dynamicImage,followImage,collectImage,favoriteImage,dynamicUserLogo;
    TextView  dynamicUserName,dynamictTime,dynamicAddress,ifSend,dynamicContentText,collectNum,favoriteNum,petBriefInfo;



    FollowCollectFavorite followCollectFavorite;
    DynamicBean dynamicBean;

    final String COMMENT_DATA_URL=Constants.httpip + "/getCommentJson";

    private Handler handler=new Handler(){
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
                    break;
                case 99:
                    Log.d(TAG, "handleMessage: 99");
                    dynamicBean=new Gson().fromJson((String)msg.obj,DynamicBean.class);
                    Log.d(TAG, "handleMessage:pureDynamic= "+dynamicBean.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initDynamicContent();//运行在ui线程
                        }
                    });
                    break;
                case 10:
                    switch (msg.arg1){
                        case 1:
                            Toast.makeText(CommentMainActivity.this,"你应经申请了，直接联系他，或者等待回复吧",Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Toast.makeText(CommentMainActivity.this,"你上次已经申请成功了!",Toast.LENGTH_SHORT).show();
                            break;
                        case 11:
                            Toast.makeText(CommentMainActivity.this,"其他人正在申请中",Toast.LENGTH_LONG).show();
                            break;
                        case 12:
                            Toast.makeText(CommentMainActivity.this,"其他人已经申请成功了!",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(CommentMainActivity.this,"你上次上次申请过了，没有通过!",Toast.LENGTH_SHORT).show();
                            break;
                        case -2:
                            Toast.makeText(CommentMainActivity.this,"您暂时不能说话",Toast.LENGTH_SHORT).show();
                            break;
                        case -1:
                            Toast.makeText(CommentMainActivity.this,"提交失败,请重试",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;

            }
        }
    };

    // TODO: 3/23/2019  
    private void initDynamicContent(){
//        new DownloadImageTask(dynamicUserLogo).execute(Constants.httpip+"/"+dynamicBean.getLogo());
//        new DownloadImageTask(dynamicImage).execute(Constants.httpip+"/"+dynamicBean.getPicture());

//        用glide加载，有缓存策略，更流畅
        Glide.with(this).load(Constants.httpip+"/"+dynamicBean.getLogo()).into(dynamicUserLogo);
        Glide.with(this).load(Constants.httpip+"/"+dynamicBean.getPicture()).into(dynamicImage);

        dynamicUserName.setText(dynamicBean.getLoginName()+"");
        String[] array=dynamicBean.getCreateTime().split("-");
        dynamictTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);
        dynamicAddress.setText(dynamicBean.getProvince()+"-"+dynamicBean.getCity());
        dynamicContentText.setText(dynamicBean.getContent());
        ifSend.setText(dynamicBean.getIsSend()+dynamicBean.getType5()+"的"+dynamicBean.getType3());
        petBriefInfo.setText("("+dynamicBean.getIsSend()+")"+dynamicBean.getType3()+"-"+dynamicBean.getType5()+"-"+dynamicBean.getType6()+"岁");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_main);
        this.setTitle("Detail");

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("BUNDLE");
        dynamicBean=(DynamicBean) bundle.getSerializable("DYNAMIC_BEAN");


        DYNAMIC_USER_ID=intent.getIntExtra("DYNAMIC_USER_ID",-1);
        DYNAMIC_ID=intent.getIntExtra("DYNAMIC_ID",-1);

        dynamicUserLogo=findViewById(R.id.dynamic_user_logo);
        dynamicUserName=findViewById(R.id.dynamic_user_name);
        dynamicImage=findViewById(R.id.dynamic_image);
        dynamictTime=findViewById(R.id.dynamic_time);
        dynamicAddress=findViewById(R.id.dynamic_address);
        ifSend=findViewById(R.id.if_send);
        dynamicContentText=findViewById(R.id.dynamic_content_text);

        followImage=findViewById(R.id.follow_image);
        favoriteImage=findViewById(R.id.favorite_image);
        favoriteNum=findViewById(R.id.favorite_num);
        collectImage=findViewById(R.id.collect_image);
        collectNum=findViewById(R.id.collect_num);
        petBriefInfo=findViewById(R.id.pet_brief_info);

        //点击某个用户头像，查看他的主页
        View.OnClickListener detailListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int DYNAMIC_USER_ID=dynamicBean.getUserId();
                final int DYNAMIC_ID=dynamicBean.getDynamicId();
                Intent intent=new Intent(v.getContext(),UserMainPageActivity.class);
                intent.putExtra("DYNAMIC_USER_ID",DYNAMIC_USER_ID);
                intent.putExtra("DYNAMIC_ID",DYNAMIC_ID);
                Bundle bundle=new Bundle();
                bundle.putSerializable("DYNAMIC_BEAN",dynamicBean);
                intent.putExtra("BUNDLE",bundle);

                v.getContext().startActivity(intent);

            }
        };
        dynamicUserLogo.setOnClickListener(detailListener);
        dynamicUserName.setOnClickListener(detailListener);
        dynamictTime.setOnClickListener(detailListener);
        dynamicAddress.setOnClickListener(detailListener);

        initDynamicContent();

//        getOneDynamicContent(App.loginUserInfo.getLoginName(),DYNAMIC_USER_ID,DYNAMIC_ID,Constants.httpip+"/getDynamic");

        View.OnClickListener followListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String FOLLOW=Constants.httpip+"/follow";
                switch (followImage.getImageAlpha()){
                    case 255://255完全不透明，代表已关注
                        followImage.setImageResource(R.mipmap.ic_launcher_person);
                        followImage.setImageAlpha(128);
                        Follow(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"0",FOLLOW);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"取消关注"+DYNAMIC_USER_ID);
                        break;

                    case 128://128代表没有关注

                        followImage.setImageResource(R.mipmap.ic_launcher_people);
                        followImage.setImageAlpha(255);
                        Follow(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"1",FOLLOW);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"关注"+DYNAMIC_USER_ID);
                        break;
                    default:
                        followImage.setImageResource(R.mipmap.ic_launcher_people);
                        followImage.setImageAlpha(255);
                        Follow(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,"1",FOLLOW);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"关注"+DYNAMIC_USER_ID);
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
                        Collect(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"取消收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    case 128://128代表没有收藏

                        collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        collectImage.setImageAlpha(255);
                        collectNum.setText((Integer.parseInt(collectNum.getText().toString())+1)+"");
                        Collect(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    default:
                        collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        collectImage.setImageAlpha(255);
                        collectNum.setText((Integer.parseInt(collectNum.getText().toString())+1)+"");
                        Collect(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"收藏"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
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
                        sendFavorite(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"取消点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    case 128://128代表没有点赞

                        favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        favoriteImage.setImageAlpha(255);
                        favoriteNum.setText((Integer.parseInt(favoriteNum.getText().toString())+1)+"");
                        sendFavorite(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                    default:
                        favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        favoriteImage.setImageAlpha(255);
                        favoriteNum.setText((Integer.parseInt(favoriteNum.getText().toString())+1)+"");
                        sendFavorite(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d(TAG, "onClick: "+App.loginUserInfo.getLoginName()+"点赞"+DYNAMIC_USER_ID+"-"+DYNAMIC_ID);
                        break;
                }
            }
        };
        favoriteImage.setOnClickListener(favoriteListener);



        //todo 根据loginUserId，dynaicUserId,dynamicId去服务器获取数据,得到做新的followflag，collectflag，favoriteflag
        getLatestFollowCollectFavorite(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID);
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

        if(App.loginUserInfo.getIsAdmin().equals("是")||App.loginUserInfo.getLoginName().equals(dynamicBean.getLoginName())){
            Log.d(TAG, "initView:  deleteDynamic.setOnClickListener");
            deleteDynamic=findViewById(R.id.delete_dynamic);
            deleteDynamic.setVisibility(View.VISIBLE);
            deleteDynamic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 3/26/2019  删除这一条动态
                    AlertDialog.Builder builder=new AlertDialog.Builder(CommentMainActivity.this);
                    builder.setTitle("确定删除该条动态吗？不可恢复");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteDynamic(dynamicBean.getUserId(),dynamicBean.getDynamicId());
                                    Toast.makeText(CommentMainActivity.this,"你已经删除了，等待刷新",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(CommentMainActivity.this,"你已取消删除",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }
            });
        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        commentsList = generateCommentReplyData();
        initExpandableListView(commentsList);
    }

    private void deleteDynamic(int userId, int dynamicId) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",userId+"")
                            .add("dynamicId",dynamicId+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/deleteDynamic").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);
                    // TODO: 3/26/2019 创建message 返回服务器删除的结果

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
     * func:生成显示的评论回复数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateCommentReplyData(){

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
        if(item.getItemId()==R.id.menu_apply){
            // TODO: 3/24/2019 发送申请宠物的申请数据到数据库
            showApplyDialog();
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
                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(CommentMainActivity.this,"您暂时不能发言，请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    // TODO: 3/16/2019 根据评论人的id，名字 ，往数据库写数据，Constants.httpip+"/setCommentData"
                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            "_"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "-"+calendar.get(Calendar.MINUTE)+
                            "-"+calendar.get(Calendar.SECOND);
                    CommentDetailBean detailBean = new CommentDetailBean(App.loginUserInfo.getLoginName(), commentContent,currentTime);
                    adapter.addTheCommentData(detailBean);
                    setCommentData(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,commentContent,currentTime);
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
     * @time 2019/03/20
     * func:弹出回复框
     * @param position
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
                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(CommentMainActivity.this,"您暂时不能发言，请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    // TODO: 3/16/2019 回复人的信息 Constants.httpip+"/setCommentReplyData"
                    ReplyDetailBean detailBean = new ReplyDetailBean(App.loginUserInfo.getLoginName(),replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            "-"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "-"+calendar.get(Calendar.MINUTE)+
                            "-"+calendar.get(Calendar.SECOND);
//                    TODO 怎么获取当前的COMMENTID
//                    commentsList.get(position).getId();
                    try{
                        commentsList.get(position).getId();
                    }
                    catch(Exception e){
                        Log.e(TAG, "onClick: commentsList.get(position).getId()"+commentsList.get(position).getId());
                    }

                    setCommentReplyData(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,commentsList.get(position).getId()
                            ,App.loginUserInfo.getUserId(),replyContent,currentTime);
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

    /**
     * func:弹出申请框
     */
    private void showApplyDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_apply_layout,null);
        final EditText applyText = (EditText) commentView.findViewById(R.id.dialog_apply_et);
        final Button bt_apply = (Button) commentView.findViewById(R.id.dialog_apply_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(CommentMainActivity.this,"您暂时不能发言，请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.loginUserInfo.getUserId()==DYNAMIC_USER_ID){
                    Toast.makeText(CommentMainActivity.this,"这是您自己发表的内容",Toast.LENGTH_SHORT).show();
                    return;

                }
                String applyContent = applyText.getText().toString().trim();
                if(!TextUtils.isEmpty(applyContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    // TODO: 3/16/2019 根据申请内容 ，往数据库写数据，Constants.httpip+"/apply"
                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            "-"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "-"+calendar.get(Calendar.MINUTE)+
                            "-"+calendar.get(Calendar.SECOND);

                    setApplyData(App.loginUserInfo.getUserId(),DYNAMIC_USER_ID,DYNAMIC_ID,applyContent,currentTime);

                }else {
                    Toast.makeText(CommentMainActivity.this,"申请内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        applyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_apply.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_apply.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }



    /**
     * 从数据库请求评论数据
     * @param dynaimcUserId
     * @param dynamicId
     * @param commentDataUrl
     */

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

    /**
     * 把评论数据写到数据库
     * @param fromUid
     * @param toUid
     * @param dynamicId
     * @param commentContent
     * @param commentTime
     */
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

    /**
     * 把回复数据写到数据库
     * @param fromUid
     * @param toUid
     * @param dynamicId
     * @param commentId
     * @param replyUid
     * @param replyContent
     * @param replyTime
     */
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

    /**
     * 把申请内容写到数据库
     * @param applyUid
     * @param toUid
     * @param dynamicId
     * @param applyContent
     * @param applyTime
     */
    private void setApplyData(final int applyUid,final int toUid,final int dynamicId,final String applyContent,final String applyTime){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("applyUid",applyUid+"")
                            .add("toUid",toUid+"")
                            .add("dynamicId",dynamicId+"")
                            .add("applyContent",applyContent+"")
                            .add("applyTime",applyTime+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/setApplyData").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    int code=Integer.parseInt(responseData);
                    Log.d(TAG, "run: code="+code);
                    Log.d(TAG, "run: setApplyData responseData="+responseData);
                    Message message=Message.obtain(handler,10,code,3,responseData);
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
     * 由于在上个界面可能操作了点赞，收藏关注，是直接发送请求到数据库
     * 而上个界面我初次加载的数据没有更新
     * @param loginUserId
     * @param dynamicUserId
     * @param dynamicId
     */
    private void getLatestFollowCollectFavorite(final int loginUserId ,final int dynamicUserId,final int dynamicId) {
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

    /**
     * 发送取消关注/关注请求
     * @param loginUserId
     * @param dynamicUserId
     * @param followFlag
     * @param url
     */
    private void Follow(final int loginUserId ,final int dynamicUserId
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

    /**
     * 发送收藏，取消收藏请求
     * @param loginUserId
     * @param dynamicUserId
     * @param dynamicId
     * @param collectFlag
     * @param url
     */
    private void Collect(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String collectFlag,final String url) {
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


    /**
     * 发送取消收藏/收藏请求
     * @param loginUserId
     * @param dynamicUserId
     * @param dynamicId
     * @param favoriteFlag
     * @param url
     */
    private void sendFavorite(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String favoriteFlag,final String url) {
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
    
    private void getOneDynamicContent(final String loginName,final int dynamicUserId,final int dynamicId,final String url){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginName",loginName)
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"").build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);
                    parseJSONWithJSONObject(responseData);

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

    public void parseJSONWithJSONObject(String responseData){
        try {
            int successCode=0;
            DynamicBeanData dynamicBeanData;
            Gson gson = new Gson();
            dynamicBeanData = gson.fromJson(responseData, DynamicBeanData.class);
            ArrayList<DynamicBean> dynamicBeanArrayList = dynamicBeanData.getData();


            if(dynamicBeanArrayList.get(0).getSuccessCode()==1){
                Message message=Message.obtain(handler,99,successCode,3,dynamicBeanArrayList.get(0));
                message.sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}