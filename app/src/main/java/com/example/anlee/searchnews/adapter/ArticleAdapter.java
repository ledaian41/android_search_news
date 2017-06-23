package com.example.anlee.searchnews.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.anlee.searchnews.R;
import com.example.anlee.searchnews.model.Article;
import com.example.anlee.searchnews.model.Media;
import com.example.anlee.searchnews.viewholder.NoImageViewHolder;
import com.example.anlee.searchnews.viewholder.NormalViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by An Lee on 6/21/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NO_IMAGE = 0;
    private static final int NORMAL = 1;
    private static final String STATE = "listState";
    private List<Article> articles;
    private final Context context;

    private Listener listener;

    public interface Listener {
        void onLoadMore();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NO_IMAGE) {
            return new NoImageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_article_no_image, parent, false));
        } else {
            return new NormalViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_article, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = articles.get(position);
        if (holder instanceof NoImageViewHolder) {
            bindNoImageViewHolder((NoImageViewHolder) holder, article);
        } else if (holder instanceof NormalViewHolder) {
            bindNormalViewHolder((NormalViewHolder) holder, article);
        }
        if (position == articles.size() - 1 && listener != null) {
            listener.onLoadMore();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchComposeView(article);
            }
        });
    }

    public void launchComposeView(Article article) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        context.startActivity(i);
    }

    private void bindNoImageViewHolder(NoImageViewHolder holder, Article article) {
        holder.tvSnippet.setText(article.getSnippet());
    }

    private void bindNormalViewHolder(NormalViewHolder holder, Article article) {
        holder.tvSnippet.setText(article.getSnippet());
        setImage(holder, article);
    }

    private void setImage(NormalViewHolder holder, Article article) {
        Media media = article.getMultimedia().get(0);
        Glide.with(context)
                .load(media.getUrl())
                .into(holder.ivCover);
        holder.ivCover.getLayoutParams().height = media.getHeight();
        holder.ivCover.getLayoutParams().width = media.getWidth();
    }

    public void setState(Bundle state) {
        state.putParcelableArrayList(STATE, (ArrayList<? extends Parcelable>) articles);
    }

    public List<Article> getStateList(Bundle state) {
        return state.getParcelableArrayList(STATE);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasImageAt(position)) {
            return NORMAL;
        }
        return NO_IMAGE;
    }

    private boolean hasImageAt(int position) {
        Article article = articles.get(position);
        return article.getMultimedia() != null && article.getMultimedia().size() > 0;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setData(List<Article> data) {
        articles.clear();
        articles.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<Article> newArticles) {
        int nextPos = articles.size();
        articles.addAll(nextPos, newArticles);
        notifyItemRangeChanged(nextPos, newArticles.size());
    }
}
