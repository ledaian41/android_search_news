package com.example.anlee.searchnews.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anlee.searchnews.R;
import com.example.anlee.searchnews.databinding.ItemArticleBinding;
import com.example.anlee.searchnews.databinding.ItemArticleNoImageBinding;
import com.example.anlee.searchnews.model.Article;
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == NO_IMAGE) {
            return new NoImageViewHolder((ItemArticleNoImageBinding) DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_article_no_image, parent, false));
        } else {
            return new NormalViewHolder((ItemArticleBinding) DataBindingUtil
                    .inflate(layoutInflater, R.layout.item_article, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = articles.get(position);
        if (holder instanceof NoImageViewHolder) {
            ((NoImageViewHolder) holder).bind(article);
        } else if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).bind(article, context);
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
        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        String url = article.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        setUpShareButton(builder);
        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    private void setUpShareButton(CustomTabsIntent.Builder builder) {
        builder.addDefaultShareMenuItem();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share);
        int requestCode = 100;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.codepath.com");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
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
