package com.example.cartooncrawl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;

public class ContentsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";

    private static LinearLayout contentView;

    @SuppressLint({"DefaultLocale", "ResourceAsColor"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        TextView postTitle = (TextView) findViewById(R.id.content_post_title);
        TextView replyBtn = (TextView) findViewById(R.id.reply_view);
        TextView backBtn = (TextView) findViewById(R.id.back_btn);
        contentView = (LinearLayout) findViewById(R.id.content_view);

        Intent intent = getIntent();
        PostInfo postInfo = (PostInfo) intent.getSerializableExtra("post_info");
        assert postInfo != null;
        int postId = postInfo.getPostId();

        replyBtn.setText(String.format("댓글 (%d)", postInfo.getReplyNum()));
        if(postInfo.getReplyNum() > 0){
            replyBtn.setTextColor(this.getColor(R.color.colorAccent));
        }

        backBtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(ContentsActivity.this, MainActivity.class);
            ContentsActivity.this.startActivity(homeIntent);
        });

        replyBtn.setOnClickListener(view -> {
            Intent replyIntent = new Intent(ContentsActivity.this, ReplyActivity.class);
            replyIntent.putExtra("post_info", postInfo);
            ContentsActivity.this.startActivity(replyIntent);
        });

        new Thread(() -> {
            try{
                String pageUrl = Crawler.getPostPage(postId);

                Connection connection = Jsoup.connect(pageUrl);

                Log.d(LOG_TAG, "getting document...");
                Document doc = connection.get();

                Log.d(LOG_TAG, "cleaning document...");
                doc.select("em").remove();

                Log.d(LOG_TAG, "selecting query...");
                Element mainElement = doc.select("#container .view_content_wrap").first();
                Element writeElement = mainElement.selectFirst(".writing_view_box .write_div");

                String title = mainElement.select(".title_subject").first().html();
                postTitle.setText(title);

                Elements postSegments = writeElement.children();
                for(Element segment : postSegments){
                    Elements imageElements = segment.select("img");
                    Log.d(LOG_TAG, imageElements.html());
                    if(!imageElements.isEmpty()){
                        String imageSrc = imageElements.first().attr("src").replace("https", "http");
                        ImageView imageView = new ImageView(this);
                        imageView.setAdjustViewBounds(true);
                        new CustomTask(imageView).execute(imageSrc).get();
                    }else{
                        String texts = segment.wholeText();
                        TextView textView = new TextView(this);
                        textView.setText(texts);
                        textView.setTextColor(this.getColor(R.color.white));
                        new TextTask(textView).execute().get();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private class CustomTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;

        public CustomTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            contentView.addView(this.imageView);
        }
    }

    private class TextTask extends AsyncTask<Void, Void, Void>{
        TextView textView;

        public TextTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void vo) {
            contentView.addView(this.textView);
        }
    }
}
