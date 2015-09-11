package com.jw.iii.pocketjw.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jw.iii.pocketjw.Helper.URLImageGetter;
import com.jw.iii.pocketjw.R;

public class NewsItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent newsItemIntent = getIntent();
        newsTitle = newsItemIntent.getStringExtra("newsTitle");
        newsUrl = newsItemIntent.getStringExtra("newsUrl");
        newsID = newsItemIntent.getStringExtra("newsID");
        newsContent = newsItemIntent.getStringExtra("newsContent");

        titleTextView = (TextView)findViewById(R.id.titleTextView);
        contentTextView = (TextView)findViewById(R.id.contentTextView);

        Typeface typeface = Typeface.create("宋体", Typeface.NORMAL);
        contentTextView.setTypeface(typeface);

        contentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());


        parseNews();
    }

    private void parseNews() {
        titleTextView.setText(newsTitle);
        if (!newsContent.isEmpty()) {
        URLImageGetter ReviewImgGetter = new URLImageGetter(NewsItemActivity.this, contentTextView);
        contentTextView.setText(Html.fromHtml(newsContent, ReviewImgGetter, null));
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "敬文书院");
                shareIntent.putExtra(Intent.EXTRA_TEXT, newsTitle + "  " + newsUrl);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareIntent, "分享"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String newsTitle, newsUrl, newsID, newsContent;
    private TextView titleTextView, contentTextView;
}
