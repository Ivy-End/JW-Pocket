package com.jw.iii.pocketjw.BaseFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.jw.iii.pocketjw.DataAdapter.News;
import com.jw.iii.pocketjw.IIIApplication;
import com.jw.iii.pocketjw.R;
import com.jw.iii.pocketjw.UI.RefreshableView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by End on 2015/6/19.
 */
public class NewsBaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_news, container, false);

        AVOSCloud.initialize(view.getContext(), "dohthw768v153y9t2eldqiwvtwf9vu07vvyzxv4kjdqbpdsf", "gs5r4j0xg7wg0xkmvjrgdv4gt1hxiaqxpso3jfzani5w8hhk");

        curNewsPage = curAnecdotePage = 0;

        arrayList = new ArrayList<>();

        list = new ArrayList<>();

        refreshableView = (RefreshableView) view.findViewById(R.id.newsView);
        curType = this.getArguments().getInt(TYPE);

        adapter = new SimpleAdapter(view.getContext(), list, R.layout.listviewitem_news,
                new String[] {"tvTitle", "tvDesc"},
                new int[] {R.id.tvTitle, R.id.tvDesc});
        listView = (ListView) view.findViewById(R.id.newsList);
        listView.setAdapter(adapter);

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
                refreshableView.finishRefreshing();
            }
        }, 0);

        getNews();

        return view;
    }

    private void getNews() {
        // TODO: Get Data and put it into arrayList
        if (curType == TYPE_NEWS) {
            if (curNewsPage == 0) {
                arrayList.clear();
            }

            curNewsPage++;
            News news = new News();
            news.setTitle("Test News " + Integer.toString(curNewsPage));
            news.setDescription("Href http://" + Integer.toString(curNewsPage));
            arrayList.add(news);
            /*String urlString = "http://jwsy.suda.edu.cn/Info.asp?PID=B5AD99E5-5C40-4507-9E25-13F76375A072&page=" + Integer.toString(curNewsPage);
            try {
                Document document = Jsoup.connect(urlString).get();
                Elements elements = document.select(".Link03_1");
                for (Element element : elements) {
                    Element href = element.parent().child(0);
                    News news = new News();
                    news.setTitle(href.text());
                    news.setDescription(href.attr("href"));
                    arrayList.add(news);
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage() == null ? "Unknown exception" : e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/
        } else if(curType == TYPE_Anecdote) {
            if (curAnecdotePage == 0) {
                arrayList.clear();
            }

            curAnecdotePage++;
            News news = new News();
            news.setTitle("Test Anecdote " + Integer.toString(curNewsPage));
            news.setDescription("Href http://" + Integer.toString(curNewsPage));
            arrayList.add(news);
        }

        addNews();
    }

    private void addNews() {
        list = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            // hashMap.put("ivThumb", arrayList.get(i).getThumb());
            hashMap.put("tvTitle", arrayList.get(i).getTitle());
            hashMap.put("tvDesc", arrayList.get(i).getDescription());
            list.add(hashMap);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Fragment newInstance(int type){
        Fragment fragment = new NewsBaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String TYPE = "data";
    private static final int TYPE_NEWS = 0;
    private static final int TYPE_Anecdote = 1;

    private int curType;
    private int curNewsPage;
    private int curAnecdotePage;

    private RefreshableView refreshableView;
    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<News> arrayList;

    private List<HashMap<String, Object>> list;

}
