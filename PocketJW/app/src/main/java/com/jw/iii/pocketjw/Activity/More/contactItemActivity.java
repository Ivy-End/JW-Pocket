package com.jw.iii.pocketjw.Activity.More;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jw.iii.pocketjw.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class contactItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent contactItemIntent = getIntent();

        contactImg = contactItemIntent.getStringExtra("contactImg");
        contactName = contactItemIntent.getStringExtra("contactName");
        contactPhone = contactItemIntent.getStringExtra("contactPhone");
        contactEmail = contactItemIntent.getStringExtra("contactEmail");

        imgImageView = (ImageView)findViewById(R.id.imgImageView);
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        phoneTextView = (TextView)findViewById(R.id.phoneTextView);
        emailTextView = (TextView)findViewById(R.id.emailTextView);
        exportButton = (Button)findViewById(R.id.exportButton);

        phoneTextView.setOnClickListener(phoneListener);
        emailTextView.setOnClickListener(emailListener);
        exportButton.setOnClickListener(exportListener);

        setTitle(contactName);
        ImageLoader.getInstance().displayImage(contactImg, imgImageView);
        nameTextView.setText(contactName);
        phoneTextView.setText(contactPhone);
        emailTextView.setText(contactEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_item, menu);
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
                shareIntent.putExtra(Intent.EXTRA_TEXT, getContactText());
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareIntent, "分享"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getContactText() {
        String contactText = "来自掌上敬文的名片分享：\n";
        contactText += "姓名：" + contactName + "\n";
        contactText += "联系方式：" + contactPhone + "\n";
        contactText += "电子邮箱：" + contactEmail + "\n";
        contactText += "（来自掌上敬文）";
        return contactText;
    }

    private boolean getContact() {
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + contactPhone);
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String [] {"display_name"}, null, null, null);
        return cursor.moveToFirst();
    }

    private void addContact() {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        long contactId = ContentUris.parseId(resolver.insert(uri, values));

        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", contactName);
        resolver.insert(uri, values);

        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", contactPhone);
        resolver.insert(uri, values);

        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        values.put("data2", "2");
        values.put("data1", contactEmail);
        resolver.insert(uri, values);
    }

    View.OnClickListener phoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactPhone));
            startActivity(phoneIntent);
        }
    };

    View.OnClickListener emailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, contactEmail);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject: ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Text: ");
            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getApplicationContext(), "无法发送E-mail", Toast.LENGTH_SHORT).show();
            }

        }
    };

    View.OnClickListener exportListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (!getContact()) {
                    addContact();
                    Toast.makeText(getApplicationContext(), "导出联系人成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "联系人已存在", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private String contactImg, contactName, contactPhone, contactEmail;
    private Button exportButton;
    private ImageView imgImageView;
    private TextView nameTextView, phoneTextView, emailTextView;
}
