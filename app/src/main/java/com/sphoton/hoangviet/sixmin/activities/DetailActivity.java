package com.sphoton.hoangviet.sixmin.activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fivehundredpx.android.blur.BlurringView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.Utilities;
import com.sphoton.hoangviet.sixmin.fragments.PostContentFragment;
import com.sphoton.hoangviet.sixmin.fragments.PostListFragment;
import com.sphoton.hoangviet.sixmin.fragments.PostVocabularyFragment;
import com.sphoton.hoangviet.sixmin.managers.APIManager;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Topic;
import com.sphoton.hoangviet.sixmin.thirdparties.BlurTransform;
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
    private MediaPlayer mediaPlayer;
    private Post mPost;
    private ViewPager viewPager;
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageView background;
    private CircleIndicator indicator;
    private Toolbar toolbar;

    private SeekBar songProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;

    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPost = (Post)getIntent().getExtras().getSerializable(Commons.POST);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        initPlayer();

        createView();
    }

    private void createView(){
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        ImageView background = (ImageView) findViewById(R.id.background);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager );
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mPost.getTitle());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load("http://"+mPost.getCoverLink()).transform(new BlurTransform(this))
                .fit()
                .into(background);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                        btnPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    }
                }else{
                    if(mediaPlayer!=null){
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    }
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                }else{
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if(currentPosition - seekBackwardTime >= 0){
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                }else{
                    mediaPlayer.seekTo(0);
                }

            }
        });

        songProgressBar.setOnSeekBarChangeListener(this);
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();


            songTotalDurationLabel.setText(""+ Utilities.milliSecondsToTimer(totalDuration));
            songCurrentDurationLabel.setText(""+Utilities.milliSecondsToTimer(currentDuration));

            int progress = (int)(Utilities.getProgressPercentage(currentDuration, totalDuration));
            songProgressBar.setProgress(progress);

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

    private void initPlayer(){
        mediaPlayer = new MediaPlayer();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mediaPlayer.setDataSource("http://" + mPost.getAudioLink());
                    mediaPlayer.prepare();
                } catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                updateProgressBar();
            }
        }.execute();
    }

    private void destroyPlayer(){
        mHandler.removeCallbacks(mUpdateTimeTask);
        mediaPlayer.stop();
        mediaPlayer.release();

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
            destroyPlayer();
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        destroyPlayer();
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
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        mediaPlayer.seekTo(currentPosition);

        updateProgressBar();
    }
}
