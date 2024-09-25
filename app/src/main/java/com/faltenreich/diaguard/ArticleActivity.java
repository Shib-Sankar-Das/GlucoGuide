package com.faltenreich.diaguard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleActivity extends AppCompatActivity {

    private static final String RSS_URL = "https://www.healthshots.com/rss-feeds/daily-health/";
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList = new ArrayList<>();
    private List<Article> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        TextView tvArticles = findViewById(R.id.tvArticles);
        EditText etSearch = findViewById(R.id.etSearch);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticleAdapter(this, filteredList);
        recyclerView.setAdapter(articleAdapter);

        fetchRSSFeed();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterArticles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchRSSFeed() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(RSS_URL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(ArticleActivity.this, "Failed to fetch RSS feed", Toast.LENGTH_SHORT).show());
                Log.e("ArticleActivity", "Failed to fetch RSS feed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rssFeed = response.body().string();
                    parseRSSFeed(rssFeed);
                } else {
                    runOnUiThread(() -> Toast.makeText(ArticleActivity.this, "Failed to fetch RSS feed", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseRSSFeed(String rssFeed) {
        try {
            Document document = Jsoup.parse(rssFeed);
            Elements items = document.select("item");

            for (Element item : items) {
                Article article = new Article();
                article.setTitle(Jsoup.parse(item.select("title").text()).text());
                article.setLink(item.select("link").text());
                article.setDescription(Jsoup.parse(item.select("description").text()).text());
                article.setPubDate(item.select("pubDate").text());

                Elements mediaContent = item.select("media|content");
                if (mediaContent != null && !mediaContent.isEmpty()) {
                    article.setImageUrl(mediaContent.attr("url"));
                }

                articleList.add(article);
            }

            runOnUiThread(() -> {
                filteredList.clear();
                filteredList.addAll(articleList);
                articleAdapter.notifyDataSetChanged();
            });
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(ArticleActivity.this, "Failed to parse RSS feed", Toast.LENGTH_SHORT).show());
            Log.e("ArticleActivity", "Failed to parse RSS feed", e);
        }
    }

    private void filterArticles(String query) {
        filteredList.clear();
        for (Article article : articleList) {
            if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(article);
            }
        }
        articleAdapter.notifyDataSetChanged();
    }
}