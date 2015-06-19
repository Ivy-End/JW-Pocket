package com.jw.iii.pocketjw.BaseFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jw.iii.pocketjw.R;

import java.lang.reflect.Field;

/**
 * Created by End on 2015/6/19.
 */
public class NewsBaseFragment extends Fragment {
    private static final String DATA = "data";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_news, container, false);
        TextView textView = (TextView)view.findViewById(R.id.text);
        textView.setText("Fragment#" + getArguments().getInt(DATA,0));
        return textView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Fragment newInstance(int type){
        Fragment fragment = new NewsBaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA,type);
        fragment.setArguments(bundle);
        return fragment;
    }


}
