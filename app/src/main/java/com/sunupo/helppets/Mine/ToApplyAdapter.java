package com.sunupo.helppets.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.comment.CommentMainActivity;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;

import java.io.IOException;
import java.util.ArrayList;

import io.rong.message.HasReceivedNotificationMessage;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class ToApplyAdapter extends RecyclerView.Adapter<ToApplyAdapter.ViewHolder> {

    private final String TAG="ToApplyAdapter";
    private  ArrayList<ToApplyDetailBean> toApplyDetailBeanArrayList;
    Handler handler;

    Handler handlerLocalUse;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView logo,image;
        TextView loginName,dynamicTime,content;

        TextView applyTime,applyContent,responseTime,responseContent,
                applyingStatus,successStatus,failureStatus;
        Button cancelApplyButton;
        LinearLayoutCompat responseLayout;
        View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;


            logo=itemView.findViewById(R.id.to_user_logo);
            loginName=itemView.findViewById(R.id.to_user_name);
            dynamicTime=itemView.findViewById(R.id.dynamic_time);
            content=itemView.findViewById(R.id.dynamic_content);
            image=itemView.findViewById(R.id.dynamic_image);

            loginName=itemView.findViewById(R.id.to_user_name);
            loginName=itemView.findViewById(R.id.to_user_name);


            applyTime=itemView.findViewById(R.id.apply_time);
            applyContent=itemView.findViewById(R.id.apply_content);
            responseTime=itemView.findViewById(R.id.response_time);
            responseContent=itemView.findViewById(R.id.response_content);

            applyingStatus=itemView.findViewById(R.id.applying_status);
            successStatus=itemView.findViewById(R.id.success_status);
            failureStatus=itemView.findViewById(R.id.failure_status);
            cancelApplyButton=itemView.findViewById(R.id.cancel_apply_button);
            responseLayout=itemView.findViewById(R.id.response_layout);
        }
    }

    public ToApplyAdapter(ArrayList<ToApplyDetailBean> toApplyDetailBeanArrayList) {
        this.toApplyDetailBeanArrayList=toApplyDetailBeanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_to_apply,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        DynamicBean dynamicBean=(DynamicBean)(msg.obj);
                        Intent intent =new Intent(view.getContext(),CommentMainActivity.class);
                        final int DYNAMIC_USER_ID=dynamicBean.getUserId();
                        final int DYNAMIC_ID=dynamicBean.getDynamicId();

                        intent.putExtra("DYNAMIC_USER_ID",DYNAMIC_USER_ID);
                        intent.putExtra("DYNAMIC_ID",DYNAMIC_ID);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("DYNAMIC_BEAN",dynamicBean);
                        intent.putExtra("BUNDLE",bundle);

                        view.getContext().startActivity(intent);
                        break;
                    case 2:
                        if(msg.arg1==2){//同意

                        }else if(msg.arg1==3){
                            holder.failureStatus.setVisibility(View.VISIBLE);
                        }

                }
            }
        };

//        点击进入详情页，可要可不要
        View.OnClickListener viewListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/26/2019 点击进入原来动态的评论主页
                int position=holder.getAdapterPosition();
                ToApplyDetailBean toApplyDetailBean=toApplyDetailBeanArrayList.get(position);
                getDynamicBean(toApplyDetailBean.getUserId(),toApplyDetailBean.getDynamicId());

            }
        };
        holder.view.setOnClickListener(viewListener);

//        取消申请按钮
        View.OnClickListener cancelListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/26/2019 取消申请，添加提示，取消后不能再申请 
                
            }
        };
        holder.cancelApplyButton.setOnClickListener(cancelListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //            v        ImageView logo,image;
//        TextView loginName,dynamicTime,content;
        ToApplyDetailBean toApplyDetailBean=toApplyDetailBeanArrayList.get(i);
        String[] array;
        new DownloadImageTask(viewHolder.logo).execute(Constants.httpip+"/"+toApplyDetailBean.getLogo());
        viewHolder.loginName.setText(toApplyDetailBean.getLoginName());

        array=toApplyDetailBean.getCreatetime().split("-");
        viewHolder.dynamicTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);

        viewHolder.content.setText(toApplyDetailBean.getContent());
        new DownloadImageTask(viewHolder.image).execute(Constants.httpip+"/"+toApplyDetailBean.getPicture());


        array=toApplyDetailBean.getApplyTime().split("-");
        viewHolder.applyTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);
        viewHolder.applyContent.setText(toApplyDetailBean.getApplyContent());

        array=toApplyDetailBean.getApplyTime().split("-");
        viewHolder.responseTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);
        viewHolder.responseContent.setText(toApplyDetailBean.getResponseContent());

        switch(toApplyDetailBean.getApplyStatus()){
            case 1:
                viewHolder.applyingStatus.setVisibility(View.VISIBLE);
                viewHolder.successStatus.setVisibility(View.GONE);
                viewHolder.failureStatus.setVisibility(View.GONE);
                viewHolder.cancelApplyButton.setVisibility(View.VISIBLE);

                viewHolder.responseLayout.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.applyingStatus.setVisibility(View.GONE);
                viewHolder.successStatus.setVisibility(View.VISIBLE);
                viewHolder.failureStatus.setVisibility(View.GONE);
                viewHolder.cancelApplyButton.setVisibility(View.GONE);

                viewHolder.responseLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                viewHolder.applyingStatus.setVisibility(View.GONE);
                viewHolder.successStatus.setVisibility(View.GONE);
                viewHolder.failureStatus.setVisibility(View.VISIBLE);
                viewHolder.cancelApplyButton.setVisibility(View.GONE);

                viewHolder.responseLayout.setVisibility(View.VISIBLE);
                break;
                default://case=1
                    viewHolder.applyingStatus.setVisibility(View.VISIBLE);
                    viewHolder.successStatus.setVisibility(View.GONE);
                    viewHolder.failureStatus.setVisibility(View.GONE);
                    viewHolder.cancelApplyButton.setVisibility(View.VISIBLE);

                    viewHolder.responseLayout.setVisibility(View.GONE);
                    break;

        }

    }

    @Override
    public int getItemCount() {
        return toApplyDetailBeanArrayList.size();
    }

    //点击原来动态内容是，需要进入下一页，所以需要先请求网络得到dynamicBean（即调用下面这一个函数），再在handler接受消息里面打开下一个activity
    private void getDynamicBean(final int dynamicUserId,final int dynamicId){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("loginUserName", App.loginUserInfo.getLoginName()+"")
                            .add("dynamicUserId",dynamicUserId+"")
                            .add("dynamicId",dynamicId+"").build();
                    Request request = new Request.Builder().url(Constants.httpip + "/getDynamicJsonWithDynamicUserIdDynamicId").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    DynamicBeanData dynamicBeanData;
                    Gson gson = new Gson();
                    dynamicBeanData = gson.fromJson(responseData, DynamicBeanData.class);
                    //因为是点击收到的申请的一个recyclerview项，触发的事件，
                    // 所以在数据库中肯定能查找到包含这条原来的动态的这一条数据构成的dynamicBean
                    ArrayList<DynamicBean> dynamicBeanArrayList = dynamicBeanData.getData();

                    Message message=Message.obtain(handler,1,2,3,dynamicBeanArrayList.get(0));
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
}
