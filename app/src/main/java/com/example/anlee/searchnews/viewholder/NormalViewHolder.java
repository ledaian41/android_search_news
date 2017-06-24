package com.example.anlee.searchnews.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.anlee.searchnews.databinding.ItemArticleBinding;
import com.example.anlee.searchnews.model.Article;
import com.example.anlee.searchnews.model.Media;


/**
 * Created by An Lee on 6/21/2017.
 */

public class NormalViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.ivCover)
//    public ImageView ivCover;
//
//    @BindView(R.id.tvSnippet)
//    public TextView tvSnippet;

    public final ItemArticleBinding binding;

    public NormalViewHolder(ItemArticleBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Article article, Context context) {
        binding.setArticle(article);
        Media cover = article.getMultimedia().get(0);
        ViewGroup.LayoutParams layoutParams = binding.ivCover.getLayoutParams();
        layoutParams.height = cover.getHeight();
        layoutParams.width = cover.getWidth();
        binding.ivCover.setLayoutParams(layoutParams);
        Glide.with(context)
                .load(article.getMultimedia().get(0).getUrl())
                .into(binding.ivCover);
        binding.executePendingBindings();
    }
}
