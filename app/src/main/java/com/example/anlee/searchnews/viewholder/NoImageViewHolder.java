package com.example.anlee.searchnews.viewholder;

import android.support.v7.widget.RecyclerView;
import com.example.anlee.searchnews.databinding.ItemArticleNoImageBinding;
import com.example.anlee.searchnews.model.Article;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by An Lee on 6/21/2017.
 */

public class NoImageViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.tvSnippet)
//    public TextView tvSnippet;

    public final ItemArticleNoImageBinding binding;

    public NoImageViewHolder(ItemArticleNoImageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Article article){
        binding.setArticle(article);
        binding.executePendingBindings();
    }
}
