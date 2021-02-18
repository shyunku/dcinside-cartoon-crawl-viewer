package com.example.cartooncrawl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ReplyActivity extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";

    private static LinearLayout contentView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replies);

        Intent intent = getIntent();
        PostInfo postInfo = (PostInfo) intent.getSerializableExtra("post_info");
        assert postInfo != null;
        int postId = postInfo.getPostId();

        contentView = (LinearLayout) findViewById(R.id.content_view);

        TextView postTitle = (TextView) findViewById(R.id.content_post_title);
        TextView commentsView = findViewById(R.id.commend_num);
        TextView backBtn = (TextView) findViewById(R.id.back_btn);

        backBtn.setOnClickListener(view -> {
            Intent homeIntent = new Intent(ReplyActivity.this, ContentsActivity.class);
            homeIntent.putExtra("post_info", postInfo);
            ReplyActivity.this.startActivity(homeIntent);
        });

        postTitle.setText(postInfo.getPostTitle());
        commentsView.setText(String.format("댓글 (%d)", postInfo.getReplyNum()));

        new Thread(() -> {
            try{
                String pageUrl = Crawler.getPostPage(postId);

                Connection connection = Jsoup.connect(pageUrl);
                connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");

                Log.d(LOG_TAG, "getting document...");
                Document doc = connection.get();

                Log.d(LOG_TAG, "selecting query...");
                Element mainElement = doc.select("#container .view_comment").first();
                Element commentElement = mainElement.select(".comment_wrap").first();
                Element commentsDiv = commentElement.selectFirst(".num_box em span");

                Log.e(LOG_TAG, commentsDiv.html() + "개");

                Elements commentDivs = commentElement.select("li");
                for(Element commentDiv : commentDivs){
                    if(commentDiv.hasClass("ub-content")){
                        // general comment
                        String nickname = commentDiv.select(".cmt_nickbox .nickname em").html();
                        String ip = commentDiv.select(".cmt_nickbox .nickname .ip").html();
                        String comment = commentDiv.select(".cmt_txtbox .ub-word").text();

                        View commentView = getLayoutInflater().inflate(R.layout.reply_item, null);
                        TextView nicknameView = commentView.findViewById(R.id.nickname);
                        TextView commentTextView = commentView.findViewById(R.id.comment);

                        nicknameView.setText(nickname);
                        commentTextView.setText(comment);

                        new ViewTask(commentView).execute().get();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private class ViewTask extends AsyncTask<Void, Void, Void> {
        View view;

        public ViewTask(View view) {
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void vo) {
            contentView.addView(this.view);
        }
    }
}
