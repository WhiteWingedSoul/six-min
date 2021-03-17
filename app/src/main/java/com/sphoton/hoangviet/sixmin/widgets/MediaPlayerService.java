package com.sphoton.hoangviet.sixmin.widgets;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.RemoteViews;

import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.activities.DetailActivity;
import com.sphoton.hoangviet.sixmin.activities.MainActivity;
import com.sphoton.hoangviet.sixmin.managers.FileManager;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by hoangviet on 11/8/16.
 */

public class MediaPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private int seekForwardTime = 5000;
    private int seekBackwardTime = 5000;
    private RemoteViews views;
    private RemoteViews bigViews;
    private LocalBroadcastManager broadcaster;
    private Post currentPost = new Post();
    private Notification status;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()){
            case Commons.PREV_ACTION:
                backward();
                break;

            case Commons.NEXT_ACTION:
                forward();
                break;

            case Commons.PLAY_ACTION:
                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                        if(views!=null){
                            views.setImageViewResource(R.id.status_bar_play,
                                    R.drawable.ic_play_arrow_white_48dp);
                            bigViews.setImageViewResource(R.id.status_bar_play,
                                    R.drawable.ic_play_arrow_white_48dp);

                            startForeground(Commons.NOTIFICATION_ID, status);
                        }
                    }
                }else{
                    if(mediaPlayer!=null){
                        mediaPlayer.start();
                        if(views!=null){
                            views.setImageViewResource(R.id.status_bar_play,
                                    R.drawable.ic_pause_white_48dp);
                            bigViews.setImageViewResource(R.id.status_bar_play,
                                    R.drawable.ic_pause_white_48dp);

                            status.contentView = views;
                            status.bigContentView = bigViews;

                            startForeground(Commons.NOTIFICATION_ID, status);
                        }
                    }
                }
                break;

            case Commons.CHANGE_LESSON:
                Post post = (Post) intent.getSerializableExtra(Commons.POST);
                if(currentPost.getId() != post.getId()) {
                    currentPost = post;
                    playSong(post.getAudioLink());
                }else {
                    sendBroadcast(Commons.ON_STARTED_AUDIO);
                }
                buildNotification(post);
                break;

            case Commons.CLOSE_ACTION:

                stopForeground(true);
                stopSelf();
                break;

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void buildNotification(Post post){
        views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, MediaPlayerService.class);
        previousIntent.setAction(Commons.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, MediaPlayerService.class);
        playIntent.setAction(Commons.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, MediaPlayerService.class);
        nextIntent.setAction(Commons.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, MediaPlayerService.class);
        closeIntent.setAction(Commons.CLOSE_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setTextViewText(R.id.status_bar_track_name, post.getTitle());
        bigViews.setTextViewText(R.id.status_bar_track_name, post.getTitle());

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_white_48dp);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_white_48dp);

        //bigViews.setTextViewText(R.id.status_bar_album_name, post.getDescription());

        if(status == null)
             status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.icon;
        status.contentIntent = pendingIntent;
        startForeground(Commons.NOTIFICATION_ID, status);

        Picasso.with(getApplicationContext()).load("http://"+post.getCoverLink()).into(views, R.id.status_bar_album_art, Commons.NOTIFICATION_ID, status);
        Picasso.with(getApplicationContext()).load("http://"+post.getCoverLink()).into(bigViews, R.id.status_bar_album_art, Commons.NOTIFICATION_ID, status);
    }

    private void initPlayer(){
        mediaPlayer = new MediaPlayer();
    }

    private void backward(){
        int currentPosition = mediaPlayer.getCurrentPosition();
        if(currentPosition - seekBackwardTime >= 0){
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        }else{
            mediaPlayer.seekTo(0);
        }
    }

    private void forward(){
        int currentPosition = mediaPlayer.getCurrentPosition();
        if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
            mediaPlayer.seekTo(currentPosition + seekForwardTime);
        }else{
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }
    }

    private void playSong(final String link){
        mediaPlayer.reset();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mediaPlayer.setDataSource(FileManager.getAudioFilePath(getApplicationContext(),"http://"+ link));
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

                sendBroadcast(Commons.ON_STARTED_AUDIO);

            }
        }.execute();
    }

    private void sendBroadcast(String action){
        Intent intent = new Intent(Commons.UPDATE_FROM_SERVICE);
        intent.putExtra("message", action);

        broadcaster.sendBroadcast(intent);

    }

    private void destroyPlayer(){
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    @Override
    public void onDestroy() {
        destroyPlayer();
        super.onDestroy();
    }
}
