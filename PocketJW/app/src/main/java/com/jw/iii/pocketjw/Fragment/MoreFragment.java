package com.jw.iii.pocketjw.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVUser;
import com.jw.iii.pocketjw.Activity.More.Contact.ContactActivity;
import com.jw.iii.pocketjw.Activity.More.Profile.ProfileActivity;
import com.jw.iii.pocketjw.Activity.More.Setting.SettingActivity;
import com.jw.iii.pocketjw.Activity.News.NewsActivity;
import com.jw.iii.pocketjw.Activity.User.LoginActivity;
import com.jw.iii.pocketjw.R;

/**
 * Created by End on 2015/9/9.
 */
public class MoreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        newsLinearLayout = (LinearLayout)view.findViewById(R.id.newsLinearLayout);
        contactLinearLayout = (LinearLayout)view.findViewById(R.id.contactLinearLayout);
        settingLinearLayout = (LinearLayout)view.findViewById(R.id.settingLinearLayout);
        profileLinearLayout = (LinearLayout)view.findViewById(R.id.profileLinearLayout);
        logoutLinearLayout = (LinearLayout)view.findViewById(R.id.logoutLinearLayout);

        newsLinearLayout.setOnClickListener(newsListner);
        contactLinearLayout.setOnClickListener(contactListener);
        settingLinearLayout.setOnClickListener(settingListener);
        profileLinearLayout.setOnClickListener(profileListener);
        logoutLinearLayout.setOnClickListener(logoutListener);

        return view;
    }

    View.OnClickListener newsListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newsIntent = new Intent(getActivity(), NewsActivity.class);
            getActivity().startActivity(newsIntent);
        }
    };

    View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
            getActivity().startActivity(contactIntent);
        }
    };

    View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
            getActivity().startActivity(settingIntent);
        }
    };

    View.OnClickListener profileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
            getActivity().startActivity(profileIntent);
        }
    };

    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AVUser.logOut();
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(loginIntent);
            getActivity().finish();
        }
    };

    private LinearLayout newsLinearLayout, contactLinearLayout;
    private LinearLayout settingLinearLayout, profileLinearLayout;
    private LinearLayout logoutLinearLayout;
}
