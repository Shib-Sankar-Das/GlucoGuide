package com.faltenreich.diaguard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article> articleList;

    public ArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.tvArticleTitle.setText(article.getTitle());
        holder.tvArticlePubDate.setText(article.getPubDate());

        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            Glide.with(context).load(article.getImageUrl()).into(holder.ivArticleImage);
        } else {
            holder.ivArticleImage.setImageResource(R.drawable.medicine_1);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("imageUrl", article.getImageUrl());
            intent.putExtra("description", article.getDescription());
            intent.putExtra("link", article.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvArticleTitle, tvArticlePubDate;
        ImageView ivArticleImage;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArticleTitle = itemView.findViewById(R.id.tvArticleTitle);
            tvArticlePubDate = itemView.findViewById(R.id.tvArticlePubDate);
            ivArticleImage = itemView.findViewById(R.id.ivArticleImage);
        }
    }
}