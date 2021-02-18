package com.example.cartooncrawl;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private static final String LOG_TAG = "LOG_TAG";

    private static Handler handler = new Handler(){
        public void handleMessage(Message msg){
            MainActivity.recyclerAdapter.notifyDataSetChanged();
        }
    };

    @SuppressLint("SimpleDateFormat")
    public static void appendPostElements(boolean isHit, int page){
        new Thread(() -> {
            try {
                String pageUrl = getGalleryPage(!isHit, page);

                Log.d(LOG_TAG, "connecting to homepage...");
                Connection connection = Jsoup.connect(pageUrl);

                Log.d(LOG_TAG, "getting document...");
                Document doc = connection.get();

                Log.d(LOG_TAG, "cleaning document...");
                doc.select("em").remove();

                Log.d(LOG_TAG, "selecting query...");

                Elements headline = doc.select(".gall_list tbody .ub-content.us-post");

                Log.d(LOG_TAG, "해당 페이지에 게시글 정보 " + headline.size() + "개 발견됨");

                for(Element element : headline){
                    int gallNum = Integer.parseInt(element.getElementsByClass("gall_num").html());
                    String postName = element.select(".gall_tit a").textNodes().get(0).text();
                    String postDate = element.select(".gall_date").attr("title");
                    String rawDateStr = element.select(".gall_date").html();

                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(postDate);
                    String rearrangedDate = new SimpleDateFormat("yy.MM.dd HH:mm").format(date);

                    int watchedNum = Integer.parseInt(element.select(".gall_count").text());
                    int recommendNum = Integer.parseInt(element.select(".gall_recommend").text());
                    String replyNumStr = element.select(".reply_num").text().replaceAll("[^0-9]", "");
                    int replyNum = replyNumStr.isEmpty() ? 0 : Integer.parseInt(replyNumStr);

                    PostInfo postInfo = new PostInfo(gallNum, postName, rawDateStr, watchedNum, recommendNum, replyNum);
                    MainActivity.infoList.add(postInfo);

                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getGalleryPage(boolean isHome, int page){
        return "http://gall.dcinside.com/board/lists?id=cartoon" + (isHome ? "" : "&exception_mode=gallhit") + "&page=" + page;
    }

    public static String getPostPage(int postId){
        return "http://gall.dcinside.com/board/view/?id=cartoon&no=" + postId;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
