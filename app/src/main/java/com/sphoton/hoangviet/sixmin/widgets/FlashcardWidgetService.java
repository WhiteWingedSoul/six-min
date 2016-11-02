package com.sphoton.hoangviet.sixmin.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.managers.APIManager;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Vocabulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hoangviet on 11/1/16.
 */

public class FlashcardWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FlashcardRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class FlashcardRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private List<Vocabulary> list = new ArrayList<>();
        private Context mContext;
        private int mAppWidgetId;
        private boolean hasData = false;

        public FlashcardRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_flashcard);

            if (position <= getCount()) {
                Vocabulary vocabulary = list.get(position);
                remoteViews.setTextViewText(R.id.word, vocabulary.getWord());
                remoteViews.setTextViewText(R.id.meaning, vocabulary.getMeaning());
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onDataSetChanged() {
            if(!hasData) {
                APIManager.dataForWidget(mContext, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        String json = response.body().string().replace("\\/", "/")
                                .replace(":\"[", ":[")
                                .replace("]\"", "]")
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\")
                                .trim();
                        Post post = gson.fromJson(json, Post.class);
                        list = post.getVocabularyList();
                        hasData = true;

                        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.stackWidgetView);

                    }
                });
            }
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

    }
}
