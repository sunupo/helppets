package com.sunupo.helppets.Mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.google.gson.Gson;
import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.DynamicBean;
import com.sunupo.helppets.bean.DynamicBeanData;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.comment.CommentMainActivity;
import com.sunupo.helppets.comment.ReplyDetailBean;
import com.sunupo.helppets.home.CollectionAdapter;
import com.sunupo.helppets.util.App;
import com.sunupo.helppets.util.Constants;
import com.sunupo.helppets.util.DownloadImageTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import io.rong.imkit.RongIM;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class FromApplyAdapter extends RecyclerView.Adapter<FromApplyAdapter.ViewHolder> {

    private final String TAG="FromApplyAdapter";
    private  ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList;
    private final String regexStr="[;,?|，。；、.]";

//    Handler h=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            String code=(String)(msg.obj);
//            if(code.equals("1")){
//                // TODO: 3/25/2019 操作Activity的网络请求，树新数据
//                httpRequest.sendRequest();
//            }else{
//                // TODO: 3/25/2019 操作Activity的网络请求，树新数据
//                httpRequest.sendRequest();
//            }
//        }
//    };
    Handler handler;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fromApplyUserLogo,dynamicImage;
        TextView fromApplyUserName,applyTime,dynamicContent,responseContent,responseTime;
        Button agreeButton,rejectButton;
        TextView sucessStatus,failureStatus;
        SimpleMarqueeView<String> marqueeView;
        SimpleMF<String> marqueeFactory;
        View view;
        LinearLayoutCompat dynamicFormerLayout,responseLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            fromApplyUserLogo=view.findViewById(R.id.from_apply_user_logo);
            dynamicImage=view.findViewById(R.id.dynamic_image);
            fromApplyUserName=view.findViewById(R.id.from_apply_user_name);
            applyTime=view.findViewById(R.id.apply_time);

            //SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
            marqueeView = (SimpleMarqueeView)view.findViewById(R.id.simpleMarqueeView);
            marqueeFactory = new SimpleMF(view.getContext());

            dynamicContent=view.findViewById(R.id.dynamic_content);
            responseContent=view.findViewById(R.id.response_content);
            responseTime=view.findViewById(R.id.response_time);
            agreeButton=view.findViewById(R.id.agree_button);
            rejectButton=view.findViewById(R.id.reject_button);
            sucessStatus=view.findViewById(R.id.success_status);
            failureStatus=view.findViewById(R.id.failure_status);
            dynamicFormerLayout=view.findViewById(R.id.dynamic_former_content);
            responseLayout=view.findViewById(R.id.response_layout);
        }
    }

    public FromApplyAdapter(ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList) {
        this.fromApplyDetailBeanArrayList=fromApplyDetailBeanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_from_apply,viewGroup,false);
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
                            holder.agreeButton.setVisibility(View.GONE);
                            holder.rejectButton.setVisibility(View.GONE);
                            holder.sucessStatus.setVisibility(View.VISIBLE);
                            holder.failureStatus.setVisibility(View.GONE);
                        }else if(msg.arg1==3){
                            holder.agreeButton.setVisibility(View.GONE);
                            holder.rejectButton.setVisibility(View.GONE);
                            holder.sucessStatus.setVisibility(View.GONE);
                            holder.failureStatus.setVisibility(View.VISIBLE);
                        }

                }
            }
        };

        View.OnClickListener viewListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                FromApplyDetailBean fromApplyDetailBean=fromApplyDetailBeanArrayList.get(position);
                getDynamicBean(fromApplyDetailBean.getUserId(),fromApplyDetailBean.getDynamicId());

            }
        };
        holder.dynamicFormerLayout.setOnClickListener(viewListener);

//        点击头像需要进入他的主页，实现太复杂，这儿直接点击头像与他聊条
        View.OnClickListener userLogoListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                RongIM.getInstance().startPrivateChat(v.getContext()
                        ,fromApplyDetailBeanArrayList.get(i).getLoginName()
                        , App.loginUserInfo.getLoginName()+"与"+fromApplyDetailBeanArrayList.get(i).getLoginName()+"聊天中");
            }
        };
        holder.fromApplyUserLogo.setOnClickListener(userLogoListener);



        //同意按钮监听器
        View.OnClickListener agreeListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                Toast.makeText(v.getContext(),"如果同意该用户，将会拒绝其他用户，请注意！",Toast.LENGTH_LONG).show();
                showAgreeDialog(v.getContext(),new BottomSheetDialog(v.getContext())
                        ,fromApplyDetailBeanArrayList.get(i).getApplyUid(),
                        fromApplyDetailBeanArrayList.get(i).getUserId(),
                        fromApplyDetailBeanArrayList.get(i).getDynamicId());
            }
        };
        holder.agreeButton.setOnClickListener(agreeListener);

//        拒绝按钮监听器
        View.OnClickListener rejectListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                showRejectDialog(v.getContext(),new BottomSheetDialog(v.getContext())
                        ,fromApplyDetailBeanArrayList.get(i).getApplyUid(),
                        fromApplyDetailBeanArrayList.get(i).getUserId(),
                        fromApplyDetailBeanArrayList.get(i).getDynamicId());
            }
        };
        holder.rejectButton.setOnClickListener(rejectListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d(TAG, "onBindViewHolder: i="+i);
        FromApplyDetailBean fromApplyDetailBean=fromApplyDetailBeanArrayList.get(i);

        new DownloadImageTask(viewHolder.fromApplyUserLogo).execute(Constants.httpip+"/"+fromApplyDetailBean.getLogo());
        new DownloadImageTask(viewHolder.dynamicImage).execute(Constants.httpip+"/"+fromApplyDetailBean.getPicture());
        viewHolder.fromApplyUserName.setText(fromApplyDetailBean.getLoginName());

        String[] array=fromApplyDetailBean.getApplyTime().split("-");
        viewHolder.applyTime.setText(array[0]+"-"+array[1]+"-"+array[2]+" "+array[3]+":"+array[4]+":"+array[5]);

        viewHolder.dynamicContent.setText(fromApplyDetailBean.getContent());

//        通过跑马灯效果显示申请内容
        final List<String> datas = Arrays.asList(fromApplyDetailBean.getApplyContent().split(regexStr));
        viewHolder.marqueeFactory.setData(datas);
        viewHolder.marqueeView.setMarqueeFactory(viewHolder.marqueeFactory);
        viewHolder.marqueeView.startFlipping();

        switch (fromApplyDetailBean.getApplyStatus()){
            case 1:
                viewHolder.agreeButton.setVisibility(View.VISIBLE);
                viewHolder.rejectButton.setVisibility(View.VISIBLE);
                viewHolder.sucessStatus.setVisibility(View.GONE);
                viewHolder.failureStatus.setVisibility(View.GONE);

                viewHolder.responseLayout.setVisibility(View.GONE);
                viewHolder.responseContent.setVisibility(View.GONE);
                viewHolder.responseTime.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.agreeButton.setVisibility(View.GONE);
                viewHolder.rejectButton.setVisibility(View.GONE);
                viewHolder.sucessStatus.setVisibility(View.VISIBLE);
                viewHolder.failureStatus.setVisibility(View.GONE);

                viewHolder.responseLayout.setVisibility(View.VISIBLE);
                viewHolder.responseContent.setVisibility(View.VISIBLE);
                viewHolder.responseTime.setVisibility(View.VISIBLE);
                viewHolder.responseContent.setText(fromApplyDetailBean.getResponseContent());
                String[] array1=fromApplyDetailBean.getResponseTime().split("-");
                viewHolder.responseTime.setText(array1[0]+"-"+array1[1]+"-"+array1[2]+" "+array1[3]+":"+array1[4]+":"+array1[5]);
                break;
            case 3:
                viewHolder.agreeButton.setVisibility(View.GONE);
                viewHolder.rejectButton.setVisibility(View.GONE);
                viewHolder.sucessStatus.setVisibility(View.GONE);
                viewHolder.failureStatus.setVisibility(View.VISIBLE);

                viewHolder.responseLayout.setVisibility(View.VISIBLE);
                viewHolder.responseContent.setVisibility(View.VISIBLE);
                viewHolder.responseTime.setVisibility(View.VISIBLE);
                viewHolder.responseContent.setText(fromApplyDetailBean.getResponseContent());
                array1=fromApplyDetailBean.getResponseTime().split("-");
                viewHolder.responseTime.setText(array1[0]+"-"+array1[1]+"-"+array1[2]+" "+array1[3]+":"+array1[4]+":"+array1[5]);
                break;
            default:
                viewHolder.agreeButton.setVisibility(View.VISIBLE);
                viewHolder.rejectButton.setVisibility(View.VISIBLE);
                viewHolder.sucessStatus.setVisibility(View.GONE);
                viewHolder.failureStatus.setVisibility(View.GONE);

                viewHolder.responseLayout.setVisibility(View.GONE);
                viewHolder.responseContent.setVisibility(View.GONE);
                viewHolder.responseTime.setVisibility(View.GONE);
                break;

     }

    }

    @Override
    public int getItemCount() {
        return fromApplyDetailBeanArrayList.size();
    }

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

//    同意，弹出文本框
    private void showAgreeDialog(Context context,BottomSheetDialog dialog,int applyUid,int dynamicUid,int dynamicId ){
        Log.d(TAG, "showAgreeDialog: applyUid="+applyUid+"dynamicUid="+dynamicUid+"dynamicId"+dynamicId);
        View commentView = LayoutInflater.from(context).inflate(R.layout.comment_dialog_layout,null);
        final EditText responseText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        responseText.setHint("请输入回应消息");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(context,"您暂时不能发言，请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                String responseContent = responseText.getText().toString().trim();
                if(!TextUtils.isEmpty(responseContent)){

                    dialog.dismiss();

                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            "-"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "-"+calendar.get(Calendar.MINUTE)+
                            "-"+calendar.get(Calendar.SECOND);
                    // TODO: 3/25/2019 向数据库写入统一的信息
                    responseApply(applyUid,dynamicUid,dynamicId,2,currentTime,responseContent);

                    Toast.makeText(context,"回应成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"回应内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        responseText.addTextChangedListener(new TextWatcher() {
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

//    拒绝，弹出文本框
    private void showRejectDialog(Context context,BottomSheetDialog dialog,int applyUid,int dynamicUid,int dynamicId ){
//        dialog = new BottomSheetDialog(context);
        View commentView = LayoutInflater.from(context).inflate(R.layout.comment_dialog_layout,null);
        final EditText responseText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        responseText.setHint("请输入回应消息");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(App.loginUserInfo.getIsBanned().equals("是")){
                    Toast.makeText(context,"您暂时不能发言，请联系管理员",Toast.LENGTH_SHORT).show();
                    return;
                }
                String responseContent = responseText.getText().toString().trim();
                if(!TextUtils.isEmpty(responseContent)){

                    dialog.dismiss();

                    Calendar calendar = Calendar.getInstance();
                    String currentTime=calendar.get(Calendar.YEAR)
                            +"-"+(calendar.get(Calendar.MONTH)+1)
                            +"-"+calendar.get(Calendar.DAY_OF_MONTH)+
                            "-"+calendar.get(Calendar.HOUR_OF_DAY)+
                            "-"+calendar.get(Calendar.MINUTE)+
                            "-"+calendar.get(Calendar.SECOND);
                    // TODO: 3/25/2019 向数据库写入统一的信息
                    responseApply(applyUid,dynamicUid,dynamicId,3,currentTime,responseContent);


                    Toast.makeText(context,"回应成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"回应内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        responseText.addTextChangedListener(new TextWatcher() {
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

//    不管是同意还是拒绝都调用者个方法，写入数据到数据库
    private void responseApply(final int applyUid,final int toUid,final int dynamicId,final int applyStatus,final String responseTime,final String responseContent){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("applyUid",applyUid+"")
                            .add("toUid",toUid+"")
                            .add("dynamicId",dynamicId+"")
                            .add("applyStatus",applyStatus+"")
                            .add("responseTime",responseTime+"")
                            .add("responseContent",responseContent+"").build();//
                    Request request = new Request.Builder().url(Constants.httpip+"/responseApply").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "run: "+responseData);

                    Message message=Message.obtain(handler,2,applyStatus,4,responseData);
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
    public void setHttpRequest(FromApplyAdapter.HttpRequest httpRequest){
        this.httpRequest=httpRequest;
    }
    private FromApplyAdapter.HttpRequest httpRequest;
}
