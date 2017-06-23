package com.example.anlee.searchnews.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anlee.searchnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 6/21/2017.
 */

public class NormalViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivCover)
    public ImageView ivCover;

    @BindView(R.id.tvSnippet)
    public TextView tvSnippet;

    public NormalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
