package com.jw.iii.pocketjw.Activity.Problems;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVUser;
import com.jw.iii.pocketjw.R;

import java.util.ArrayList;

public class ProblemItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent problemItemIntent = getIntent();
        Bundle bundle = problemItemIntent.getExtras();
        problemID = bundle.get("problemID").toString();
        problemTitle = bundle.get("problemTitle").toString();
        problemDesc =  bundle.get("problemDesc").toString();
        problemPublisher = (AVUser)bundle.get("problemPublisher");
        problemImages = (ArrayList<String>)bundle.get("problemImages");

        setTitle(problemTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_problem_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String problemID, problemTitle, problemDesc;
    private AVUser problemPublisher;
    private ArrayList<String> problemImages;
}
