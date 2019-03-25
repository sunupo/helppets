package com.sunupo.helppets.Mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunupo.helppets.R;

import java.util.ArrayList;

class ToApplyAdapter extends RecyclerView.Adapter<ToApplyAdapter.ViewHolder> {

    private  ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userId;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
            userId=itemView.findViewById(R.id.follow_list_item_user_id);
        }
    }

    public ToApplyAdapter(ArrayList<FromApplyDetailBean> fromApplyDetailBeanArrayList) {
        this.fromApplyDetailBeanArrayList=fromApplyDetailBeanArrayList;
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
                FromApplyDetailBean fromApplyDetailBean=fromApplyDetailBeanArrayList.get(position);
            }
        };
        holder.view.setOnClickListener(viewListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        FromApplyDetailBean fromApplyDetailBean=fromApplyDetailBeanArrayList.get(i);

        viewHolder.userId.setText(fromApplyDetailBean.getUserId()+"");


    }

    @Override
    public int getItemCount() {
        return fromApplyDetailBeanArrayList.size();
    }
}
