package com.sunupo.helppets.Message;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunupo.helppets.R;
import com.sunupo.helppets.bean.UserInfo;
import com.sunupo.helppets.home.CollectionAdapter;
import com.sunupo.helppets.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {

    private ArrayList<UserInfo> userInfoArrayList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView loginName;
        TextView address;//province+city
        ImageView logo;
        TextView userId;

        View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            loginName=itemView.findViewById(R.id.follow_list_item_login_name);
            address=itemView.findViewById(R.id.follow_list_item_province_city);
            logo=itemView.findViewById(R.id.follow_list_item_logo);
            userId=itemView.findViewById(R.id.follow_list_item_user_id);
        }
    }

    public FollowListAdapter(ArrayList<UserInfo> userInfoArrayList) {
        this.userInfoArrayList=userInfoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_follow_list,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        View.OnClickListener viewListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                UserInfo userInfo=userInfoArrayList.get(position);
                RongIM.getInstance().startPrivateChat(v.getContext(), "4", "标题");
//                RongIM.getInstance().startConversation(view.getContext(),Conversation.ConversationType.PRIVATE,userInfo.getUserId()+"","CONVERSATION_TITLE");
//                RongIM.getInstance().startSubConversationList(view.getContext(),Conversation.ConversationType.PRIVATE);
//                Map<String, Boolean> supportedConversation=new HashMap<>();
//                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(),false);
//                RongIM.getInstance().startConversationList(view.getContext(),supportedConversation);
            }
        };
        holder.view.setOnClickListener(viewListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        UserInfo userInfo=userInfoArrayList.get(i);
        viewHolder.logo.setImageResource(R.drawable.keji);
        viewHolder.userId.setText(userInfo.getUserId()+"");
        viewHolder.loginName.setText(userInfo.getLoginName());
        viewHolder.address.setText(userInfo.getProvince()+"-"+userInfo.getCity());

    }

    @Override
    public int getItemCount() {
        return userInfoArrayList.size();
    }
}
