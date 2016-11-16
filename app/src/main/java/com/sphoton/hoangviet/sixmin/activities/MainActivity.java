package com.sphoton.hoangviet.sixmin.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.fragments.PostListFragment;
import com.sphoton.hoangviet.sixmin.managers.APIManager;
import com.sphoton.hoangviet.sixmin.managers.AnimationHelper;
import com.sphoton.hoangviet.sixmin.managers.HostReachability;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Topic;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private FrameLayout noInternetLayout;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        noInternetLayout = (FrameLayout)findViewById(R.id.noInternetLayout);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                supportInvalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_item_review:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@sphoton.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi Earth");
                        i.putExtra(Intent.EXTRA_TEXT   , "Ứng dụng quá tuyệt vời ~^o^~");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.drawer_item_like:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        return true;
                }
                return false;
            }
        });

        initReachabilityService();
    }

    private void initReachabilityService(){
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                try {
                    HostReachability.checkHostReachable(MainActivity.this);
                    handler.obtainMessage(1).sendToTarget();
                }catch (Exception e){
                    e.printStackTrace();
                    handler.obtainMessage(0).sendToTarget();
                    Log.d("Connection","unreachable");
                    HostReachability.setHasConnection(false);
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 2000);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        if (noInternetLayout != null)
                            if (!noInternetLayout.isShown())
                                AnimationHelper.viewVisible(noInternetLayout);
                        break;
                    case 1:
                        if (noInternetLayout != null)
                            if (noInternetLayout.isShown())
                                AnimationHelper.viewGone(noInternetLayout);
                        break;
                }
            }
        };
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        //private boolean doNotifyDataSetChangedOnce = false;
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<Topic> mTopicList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
//            if(doNotifyDataSetChangedOnce){
//                notifyDataSetChanged();
//                doNotifyDataSetChangedOnce = false;
//            }
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, Topic topic) {
            mFragmentList.add(fragment);
            mTopicList.add(topic);
            notifyDataSetChanged();
//            doNotifyDataSetChangedOnce = true;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTopicList.get(position).getTitle();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        APIManager.GETAllTopics(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<String> topics;
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                topics = gson.fromJson(response.body().string(), listType);
                if (topics.size()>0) {
                    for (String topic : topics) {
                        final Topic top = new Topic();
                        top.setTitle(topic);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addFragment(PostListFragment.newInstance(top), top);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startActivity(int type, Bundle bundle){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
