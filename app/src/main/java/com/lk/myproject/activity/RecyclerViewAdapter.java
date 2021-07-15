package com.lk.myproject.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;

    public RecyclerViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = View.inflate(context, android.R.layout.simple_list_item_1, null);
        view.setBackgroundColor(Color.RED);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int pos) {
        viewHolder.text.setText(String.valueOf(pos));
    }

    @Override
    public int getItemCount() {
        return 30;
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView text;

    public ItemViewHolder(View itemView) {
        super(itemView);
        text = itemView.findViewById(android.R.id.text1);
        text.setTextColor(Color.WHITE);
    }
}
