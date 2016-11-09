package com.sphoton.hoangviet.sixmin.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fivehundredpx.android.blur.BlurringView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.Utilities;
import com.sphoton.hoangviet.sixmin.fragments.PostContentFragment;
import com.sphoton.hoangviet.sixmin.fragments.PostListFragment;
import com.sphoton.hoangviet.sixmin.fragments.PostVocabularyFragment;
import com.sphoton.hoangviet.sixmin.fragments.VocabularyDialogFragment;
import com.sphoton.hoangviet.sixmin.managers.APIManager;
import com.sphoton.hoangviet.sixmin.managers.AnalyticsTrackers;
import com.sphoton.hoangviet.sixmin.managers.FileManager;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Topic;
import com.sphoton.hoangviet.sixmin.thirdparties.BlurTransform;
import com.sphoton.hoangviet.sixmin.widgets.MediaPlayerService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hoangviet on 10/13/16.
 */
public class DetailActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private Post mPost;
    private static VocabularyDialogFragment dialogFragment;
    private ViewPager viewPager;
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageView background;
    private ImageButton flashCard;
    private CircleIndicator indicator;
    private Toolbar toolbar;
    private ImageView progressCircle;
    private LinearLayout playerInterface;

    private SeekBar songProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;

    private Handler mHandler = new Handler();
    private BroadcastReceiver receiver;
    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;


    private MediaPlayerService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPost = (Post)getIntent().getExtras().getSerializable(Commons.POST);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        createView();


        Intent intent = new Intent(DetailActivity.this, MediaPlayerService.class);
        intent.setAction(Commons.CHANGE_LESSON);
        intent.putExtra(Commons.POST, mPost);
        startService(intent);
    }

    private void createView(){
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        flashCard = (ImageButton) findViewById(R.id.flashCard);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        progressCircle = (ImageView) findViewById(R.id.progressCircle);
        ImageView background = (ImageView) findViewById(R.id.background);
        playerInterface = (LinearLayout) findViewById(R.id.playerInterface);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager );
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mPost.getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(R.drawable.background2).transform(new BlurTransform(this))
                .fit()
                .into(background);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    if (mService.getMediaPlayer().isPlaying()) {
                        if (mService.getMediaPlayer() != null) {
                            mService.getMediaPlayer().pause();
                            btnPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                        }
                    } else {
                        if (mService.getMediaPlayer() != null) {
                            mService.getMediaPlayer().start();
                            btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                        }
                    }
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    int currentPosition = mService.getMediaPlayer().getCurrentPosition();
                    if(currentPosition + seekForwardTime <= mService.getMediaPlayer().getDuration()){
                        mService.getMediaPlayer().seekTo(currentPosition + seekForwardTime);
                    }else{
                        mService.getMediaPlayer().seekTo(mService.getMediaPlayer().getDuration());
                    }
                }
                /*
                Intent intent = new Intent(DetailActivity.this, MediaPlayerService.class);
                intent.setAction(Commons.NEXT_ACTION);
                startService(intent);*/
            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mBound) {
                    int currentPosition = mService.getMediaPlayer().getCurrentPosition();
                    if (currentPosition - seekBackwardTime >= 0) {
                        mService.getMediaPlayer().seekTo(currentPosition - seekBackwardTime);
                    } else {
                        mService.getMediaPlayer().seekTo(0);
                    }
                }

            /*    Intent intent = new Intent(DetailActivity.this, MediaPlayerService.class);
                intent.setAction(Commons.PREV_ACTION);
                startService(intent);*/
            }
        });

        flashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragment = VocabularyDialogFragment.newInstance(mPost);
                dialogFragment.show(getSupportFragmentManager(), null);

                Tracker tracker = AnalyticsTrackers.getTracker(DetailActivity.this);
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("flashCard")
                        .setAction("click-on")
                        .setLabel(mPost.getTitle())
                        .setValue(1)
                        .build());
            }
        });

        songProgressBar.setOnSeekBarChangeListener(this);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getStringExtra("message")){
                    case Commons.ON_STARTED_AUDIO:
                        btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                        progressCircle.setVisibility(View.GONE);
                        playerInterface.setVisibility(View.VISIBLE);
                        updateProgressBar();
                        break;
                }
            }
        };
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mBound) {
                long totalDuration = mService.getMediaPlayer().getDuration();
                long currentDuration = mService.getMediaPlayer().getCurrentPosition();


                songTotalDurationLabel.setText("" + Utilities.milliSecondsToTimer(totalDuration));
                songCurrentDurationLabel.setText("" + Utilities.milliSecondsToTimer(currentDuration));

                int progress = (int) (Utilities.getProgressPercentage(currentDuration, totalDuration));
                songProgressBar.setProgress(progress);

            }
            mHandler.postDelayed(this, 100);
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PostContentFragment.newInstance(mPost));
        adapter.addFragment(PostVocabularyFragment.newInstance(mPost));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        //toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if (toggle.onOptionsItemSelected(item)) {
        //    return true;
        //}
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            mHandler.removeCallbacks(mUpdateTimeTask);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        mHandler.removeCallbacks(mUpdateTimeTask);
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);

        if (mBound) {
            int totalDuration = mService.getMediaPlayer().getDuration();
            int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);

            mService.getMediaPlayer().seekTo(currentPosition);
        }

        updateProgressBar();
    }

    @Override
    protected void onStart() {
        // Bind to LocalService
        Intent intent = new Intent(this, MediaPlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(Commons.UPDATE_FROM_SERVICE)
        );

        super.onStart();
    }

    @Override
    protected void onStop() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        super.onStop();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
