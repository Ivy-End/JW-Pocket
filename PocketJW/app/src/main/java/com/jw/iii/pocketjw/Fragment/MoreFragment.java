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
import com.jw.iii.pocketjw.Activity.MainActivity;
import com.jw.iii.pocketjw.Activity.More.ContactActivity;
import com.jw.iii.pocketjw.Activity.User.LoginActivity;
import com.jw.iii.pocketjw.R;

/**
 * Created by End on 2015/9/9.
 */
public class MoreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        contactLinearLayout = (LinearLayout)view.findViewById(R.id.contactLinearLayout);
        logoutLinearLayout = (LinearLayout)view.findViewById(R.id.logoutLinearLayout);

        contactLinearLayout.setOnClickListener(contactListener);
        logoutLinearLayout.setOnClickListener(logoutListener);

        return view;
    }

    View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
            getActivity().startActivity(contactIntent);
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

    private LinearLayout contactLinearLayout, logoutLinearLayout;
}
