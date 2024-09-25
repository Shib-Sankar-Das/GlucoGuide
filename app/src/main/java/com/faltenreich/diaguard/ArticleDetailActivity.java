package com.faltenreich.diaguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        TextView tvArticleDetailTitle = findViewById(R.id.tvArticleDetailTitle);
        ImageView ivArticleDetailImage = findViewById(R.id.ivArticleDetailImage);
        TextView tvArticleDetailDescription = findViewById(R.id.tvArticleDetailDescription);
        Button btnMore = findViewById(R.id.btnMore);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");

        tvArticleDetailTitle.setText(title);
        tvArticleDetailDescription.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(ivArticleDetailImage);
        } else {
            ivArticleDetailImage.setImageResource(R.drawable.medicine_1);
        }

        btnMore.setOnClickListener(v -> {
            if (link != null && !link.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            } else {
                Toast.makeText(ArticleDetailActivity.this, "No link available" + link, Toast.LENGTH_SHORT).show();
            }
        });
    }
}