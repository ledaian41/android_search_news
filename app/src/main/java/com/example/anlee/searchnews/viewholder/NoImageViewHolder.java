package com.example.anlee.searchnews.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anlee.searchnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by An Lee on 6/21/2017.
 */

public class NoImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvSnippet)
    public TextView tvSnippet;

    public NoImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
