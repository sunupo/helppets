package com.sunupo.helppets.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.comment.CommentMainActivity;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MineFollowAdapter extends RecyclerView.Adapter<MineFollowAdapter.ViewHolder>{

    private ArrayList<DynamicBean> dynamicBeanArrayList;
    private final String TAG="MineDynamicAdapter";
    SharedPreferences sp=null;
    private int loginUserId=0;
    Handler  handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
//          当服务器返回成功代码.equals("1")后，才继续执行
                case 1:
                    if(((String)(msg.obj)).equals("1")){
                        httpRequest.sendRequest();
                    }
                    break;
                case 2:
                    if(((String)(msg.obj)).equals("1")){
                        httpRequest.sendRequest();
                    }
                    break;
                case 3:
                    if(((String)(msg.obj)).equals("1")){
                        httpRequest.sendRequest();
                    }
                    break;
                case 4:
                    if(((String)(msg.obj)).equals("1")){
                        httpRequest.sendRequest();
                    }
                    break;

            }
        }
    };


    static class ViewHolder extends RecyclerView.ViewHolder{
        View dynamicView;
        ImageView contentImage;
        TextView contentText;
        ImageView logo;
        TextView usernameName;
        TextView displayTime;
        TextView displayCity;
        ImageView dynamicItemIcon;//添加关注imageview

        ImageView collectImage;
        TextView collectNum;
        ImageView favoriteImage;
        TextView favoriteNum;
        ImageView viewsImage;
        TextView viewsNum;

        TextView briefInfo;

        int userId;
        int dynamicId;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            dynamicView=itemView;
            contentImage=itemView.findViewById(R.id.dynamic_item_content_image);
            contentText=itemView.findViewById(R.id.dynamic_item_content_text);
            logo=itemView.findViewById(R.id.dynamic_item_logo);
            usernameName=itemView.findViewById(R.id.dynamic_item_userName);
            displayTime=itemView.findViewById(R.id.dynamic_item_time);
            displayCity=itemView.findViewById(R.id.dynamic_item_city);
            dynamicItemIcon=itemView.findViewById(R.id.dynamic_item_icon);
            collectImage=itemView.findViewById(R.id.collect_image);
            collectNum=itemView.findViewById(R.id.collect_num);
            favoriteImage=itemView.findViewById(R.id.favorite_image);
            favoriteNum=itemView.findViewById(R.id.favorite_num);
            viewsImage=itemView.findViewById(R.id.views_image);
            viewsNum=itemView.findViewById(R.id.views_num);
            briefInfo=itemView.findViewById(R.id.pet_brief_info);
        }
    }

    public MineFollowAdapter(ArrayList<DynamicBean> dynamicBeanArrayList) {
        this.dynamicBeanArrayList = dynamicBeanArrayList;
//        if(dynamicBeanArrayList.size()>0){
//            loginUserId=dynamicBeanArrayList.get(0).getLoginUserId();
//        }
        loginUserId=App.loginUserInfo.getUserId();

    }

    @NonNull
    @Override
    public MineFollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collection,viewGroup,false);
        ViewHolder holder=new ViewHolder(itemView);

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();
                DynamicBean dynamicBean=dynamicBeanArrayList.get(position);
                Intent intent=new Intent(v.getContext(),CommentMainActivity.class);

                final int DYNAMIC_USER_ID=dynamicBean.getUserId();
                final int DYNAMIC_ID=dynamicBean.getDynamicId();
                final String ADD_VIEWS=Constants.httpip+"/addViews";
                // TODO: 3/19/2019 向dynamic表 where DYNAMIC_USER_ID and DYNAMIC_ID，写入views+1
                sendRequestWithHttpURLConnectionAddViews(DYNAMIC_USER_ID,DYNAMIC_ID,ADD_VIEWS);

                intent.putExtra("DYNAMIC_USER_ID",DYNAMIC_USER_ID);
                intent.putExtra("DYNAMIC_ID",DYNAMIC_ID);
                Bundle bundle=new Bundle();
                bundle.putSerializable("DYNAMIC_BEAN",dynamicBean);
                intent.putExtra("BUNDLE",bundle);

                Log.d("MineDynamicAdapter", "onClick:用户= "+dynamicBean.getUserId()+"的动态="+dynamicBean.getDynamicId());

                v.getContext().startActivity(intent);
            }
        };
        holder.contentText.setOnClickListener(onClickListener);
        holder.contentImage.setOnClickListener(onClickListener);

//        添加关注listener
        View.OnClickListener followListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp=v.getContext().getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);
                final String LOGIN_NAME=sp.getString(Constants.LOGIN_NAME,"");
                int position=holder.getAdapterPosition();
                DynamicBean dynamicBean=dynamicBeanArrayList.get(position);

                final int DYNAMIC_USER_ID=dynamicBean.getUserId();
//                final String FOLLOW=Constants.httpip+"/getComment";
                final String FOLLOW=Constants.httpip+"/follow";
                switch (holder.dynamicItemIcon.getImageAlpha()){
//                    在绑定视图的时候，就根据是否关注的情况，就设置好dynamicItemIcon的ImageAlpha
                    case 255://255完全不透明，代表已关注
//Only the original thread that created a view hierarchy can touch its views.
//                        如果创建线程来执行下面两行代码，会报上面的错
                        holder.dynamicItemIcon.setImageResource(R.mipmap.ic_launcher_person);
                        holder.dynamicItemIcon.setImageAlpha(128);
                        sendRequestWithHttpURLConnectionFollow(loginUserId,DYNAMIC_USER_ID,"0",FOLLOW);
//                        holder.dynamicItemIcon.setColorFilter(Color.parseColor("#aaaaaa"));
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"取消关注"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;

                    case 128://128代表没有关注

                        holder.dynamicItemIcon.setImageResource(R.mipmap.ic_launcher_people);
                        holder.dynamicItemIcon.setImageAlpha(255);
                        sendRequestWithHttpURLConnectionFollow(loginUserId,DYNAMIC_USER_ID,"1",FOLLOW);
//                        holder.dynamicItemIcon.setColorFilter(Color.parseColor("#FF5C5C"));
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"关注"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;
//                    default:
//                        holder.dynamicItemIcon.setImageResource(R.mipmap.ic_launcher_people);
//                        holder.dynamicItemIcon.setImageAlpha(255);
//                        sendRequestWithHttpURLConnectionFollow(loginUserId,DYNAMIC_USER_ID,"1",FOLLOW);
////                        holder.dynamicItemIcon.setColorFilter(Color.parseColor("#FF5C5C"));
//                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"关注"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
//                        break;
                }
            }
        };
        holder.dynamicItemIcon.setOnClickListener(followListener);

//        添加收藏listener
        View.OnClickListener collectListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp=v.getContext().getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);
                final String LOGIN_NAME=sp.getString(Constants.LOGIN_NAME,"");

                int position=holder.getAdapterPosition();
                DynamicBean dynamicBean=dynamicBeanArrayList.get(position);
                final int DYNAMIC_USER_ID=dynamicBean.getUserId();
                final int DYNAMIC_ID=dynamicBean.getDynamicId();

                final String URL=Constants.httpip+"/collect";
                switch (holder.collectImage.getImageAlpha()){
                    case 255://255代表收藏

                        holder.collectImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
                        holder.collectImage.setImageAlpha(128);
                        if(Integer.parseInt(holder.collectNum.getText().toString())>0)
                            holder.collectNum.setText((Integer.parseInt(holder.collectNum.getText().toString())-1)+"");
                        sendRequestWithHttpURLConnectionCollect(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"取消收藏"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;
                    case 128://128代表没有收藏

                        holder.collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        holder.collectImage.setImageAlpha(255);
                        holder.collectNum.setText((Integer.parseInt(holder.collectNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionCollect(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"收藏"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;
//                    default:
//                        holder.collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
//                        holder.collectImage.setImageAlpha(255);
//                        holder.collectNum.setText((Integer.parseInt(holder.collectNum.getText().toString())+1)+"");
//                        sendRequestWithHttpURLConnectionCollect(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
//                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"收藏"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
//                        break;
                }
            }
        };
        holder.collectImage.setOnClickListener(collectListener);

//        添加点赞listener
        View.OnClickListener favoriteListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp=v.getContext().getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);
                final String LOGIN_NAME=sp.getString(Constants.LOGIN_NAME,"");

                int position=holder.getAdapterPosition();
                DynamicBean dynamicBean=dynamicBeanArrayList.get(position);
                final int DYNAMIC_USER_ID=dynamicBean.getUserId();
                final int DYNAMIC_ID=dynamicBean.getDynamicId();

                final String URL=Constants.httpip+"/favorite";
                switch (holder.favoriteImage.getImageAlpha()){
                    case 255://255代表点赞

                        holder.favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
                        holder.favoriteImage.setImageAlpha(128);
                        if(Integer.parseInt(holder.favoriteNum.getText().toString())>0)
                            holder.favoriteNum.setText((Integer.parseInt(holder.favoriteNum.getText().toString())-1)+"");
                        sendRequestWithHttpURLConnectionFavorite(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"0",URL);
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"取消点赞"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;
                    case 128://128代表没有点赞

                        holder.favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
                        holder.favoriteImage.setImageAlpha(255);
                        holder.favoriteNum.setText((Integer.parseInt(holder.favoriteNum.getText().toString())+1)+"");
                        sendRequestWithHttpURLConnectionFavorite(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"点赞"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
                        break;
//                    default:
//                        holder.favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
//                        holder.favoriteImage.setImageAlpha(255);
//                        holder.favoriteNum.setText((Integer.parseInt(holder.favoriteNum.getText().toString())+1)+"");
//                        sendRequestWithHttpURLConnectionFavorite(loginUserId,DYNAMIC_USER_ID,DYNAMIC_ID,"1",URL);
//                        Log.d("MineDynamicAdapter", "onClick: "+LOGIN_NAME+"点赞"+dynamicBean.getUserId()+"-"+dynamicBean.getDynamicId());
//                        break;
                }
            }
        };
        holder.favoriteImage.setOnClickListener(favoriteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MineFollowAdapter.ViewHolder viewHolder, int i) {

        DynamicBean dynamicBean=dynamicBeanArrayList.get(i);
        String[] array=dynamicBean.getCreateTime().split("-");
        viewHolder.displayTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);
        viewHolder.displayCity.setText(dynamicBean.getCity());
        viewHolder.usernameName.setText(dynamicBean.getLoginName());
        viewHolder.contentText.setText(dynamicBean.getContent());
//        new DownloadImageTask(viewHolder.logo).execute(Constants.httpip+"/"+dynamicBean.getLogo());
//        new DownloadImageTask(viewHolder.contentImage).execute(Constants.httpip+"/"+dynamicBean.getPicture());

        Glide.with(viewHolder.itemView).load(Constants.httpip+"/"+dynamicBean.getLogo()).into(viewHolder.logo);
        Glide.with(viewHolder.itemView).load(Constants.httpip+"/"+dynamicBean.getPicture()).into(viewHolder.contentImage);


        viewHolder.briefInfo.setText("("+dynamicBean.getIsSend()+")"+dynamicBean.getType3()+"-"+dynamicBean.getType5()+"-"+dynamicBean.getType6()+"岁");
        viewHolder.userId=dynamicBean.getUserId();
        viewHolder.dynamicId=dynamicBean.getDynamicId();
        viewHolder.dynamicItemIcon.setVisibility(View.INVISIBLE);


        // TODO: 3/19/2019 通过loginName， dynamicUserId，查询是否关注
//        final String FOLLOW=Constants.httpip+"/follow";
//        sp=viewHolder.dynamicView.getContext().getSharedPreferences(Constants.LoginInfo,MODE_PRIVATE);
//        final String LOGIN_NAME=sp.getString(Constants.LOGIN_NAME,"");
//        sendRequestWithHttpURLConnectionFollow(LOGIN_NAME,dynamicBean.getUserId(),"0","1",FOLLOW);
//        下面用一个异步栈来返回查询关注的结果，若已关注dynamicItemIcon.alpha设为255，没有关注设为128
//        final String loginName ,final int dynamicUserId,final String followFlag,final String actionFlag,final String url
//        new QueryFollowFlagTask(viewHolder.dynamicItemIcon).execute(LOGIN_NAME,dynamicBean.getUserId()+"","0","0",FOLLOW);

        viewHolder.collectNum.setText(dynamicBean.getCollect()+"");
        viewHolder.favoriteNum.setText(dynamicBean.getFavorite()+"");
        viewHolder.viewsNum.setText(dynamicBean.getViews()+"");

        if(dynamicBean.getFollowFlag().equals("已关注")){
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getFollowFlag());
            viewHolder.dynamicItemIcon.setImageAlpha(255);
            viewHolder.dynamicItemIcon.setImageResource(R.mipmap.ic_launcher_people);
        }else{
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getFollowFlag());
            viewHolder.dynamicItemIcon.setImageAlpha(128);
            viewHolder.dynamicItemIcon.setImageResource(R.mipmap.ic_launcher_person);
        }

        if(dynamicBean.getCollectFlag().equals("已收藏")){
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getCollectFlag());
            viewHolder.collectImage.setImageAlpha(255);
            viewHolder.collectImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
        }else{
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getCollectFlag());
            viewHolder.collectImage.setImageAlpha(128);
            viewHolder.collectImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
        }

        if(dynamicBean.getFavoriteFlag().equals("已点赞")){
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getFavoriteFlag());
            viewHolder.favoriteImage.setImageAlpha(255);
            viewHolder.favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_DARK));
        }else{
            Log.d(TAG, "onBindViewHolder: "+"i="+i+dynamicBean.getFavoriteFlag());
            viewHolder.favoriteImage.setImageAlpha(128);
            viewHolder.favoriteImage.setColorFilter(Color.parseColor(Constants.COLOR_LIGHT));
        }


    }

    @Override
    public int getItemCount() {
        return dynamicBeanArrayList.size();
    }

    private void sendRequestWithHttpURLConnectionAddViews(final int dynaimcUserId ,final int dynamicId,final String commentDataUrl) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userId",dynaimcUserId+"")
                            .add("dynamicId",dynamicId+"").build();//
                    Request request = new Request.Builder().url(commentDataUrl).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

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
    }
    /**
     * actionFlag=0，查询query，是否关注，没查询到记录，说明未关注,此时不关注followFlag的值
     * actionFlag=1，更改update，若是followFlag=1关注，则插入一条数据写入关注；若是followFlag=0取消关注，则删除记录
     */

    private void sendRequestWithHttpURLConnectionFollow(final int loginUserId ,final int dynamicUserId
            ,final String followFlag,final String url) {
        Log.d(TAG, "sendRequestWithHttpURLConnectionFollow: "+"loginUserId="+loginUserId);
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
    private void sendRequestWithHttpURLConnectionCollect(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String collectFlag,final String url) {
        Log.d(TAG, "sendRequestWithHttpURLConnectionCollect: "+"loginUserId="+loginUserId);
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
                    Log.d(TAG, "run: collect.responseData= "+responseData);

                    Message message=Message.obtain(handler,3,2,3,responseData);
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
    private void sendRequestWithHttpURLConnectionFavorite(final int loginUserId ,final int dynamicUserId,final int dynamicId,final String favoriteFlag,final String url) {
        Log.d(TAG, "sendRequestWithHttpURLConnectionFavorite: "+"loginUserId="+loginUserId);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserId",loginUserId+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"")
                            .add("favoriteFlag",favoriteFlag).build();//
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    Message message=Message.obtain(handler,4,2,3,responseData);
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
    //    适配器中定义的接口，在活动中实现
    public interface HttpRequest{
        void sendRequest();
    }
    public void setHttpRequest(HttpRequest httpRequest){
        this.httpRequest=httpRequest;
    }
    private HttpRequest httpRequest;

}