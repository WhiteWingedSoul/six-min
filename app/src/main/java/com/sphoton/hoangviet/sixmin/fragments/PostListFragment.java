package com.sphoton.hoangviet.sixmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.activities.MainActivity;
import com.sphoton.hoangviet.sixmin.managers.APIManager;
import com.sphoton.hoangviet.sixmin.managers.FileManager;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Topic;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hoangviet on 10/13/16.
 */
public class PostListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostPreviewAdapter adapter;

    public static PostListFragment newInstance(Topic topic) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Commons.TOPIC, topic);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        setUpAdapter(recyclerView);
    }

    private void setUpAdapter(RecyclerView recyclerView) {
        final PostPreviewAdapter adapter = new PostPreviewAdapter(getActivity());
        Topic topic = (Topic)getArguments().getSerializable(Commons.TOPIC);
        APIManager.GETPost(topic.getTitle(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                String json = response.body().string().replace("\\/","/")
                        .replace("\"[","[")
                        .replace("]\"","]")
                        .replace("\\\"","\"");
                List<Post> posts = gson.fromJson(json, listType);
                adapter.updateAdapter(posts);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
    }

    class PostPreviewAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<Post> mList = new ArrayList<>();

        public PostPreviewAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<Post> list){
            mList.clear();
            mList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PostPreviewHolder vh = (PostPreviewHolder) holder;
            final Post post = mList.get(position);
            Picasso.with(getActivity()).load("http://"+post.getCoverLink())
                    .fit()
                    .into(vh.background);
            vh.title.setText(post.getTitle());
            vh.description.setText(post.getContent());

            vh.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Commons.POST, post);
                    ((MainActivity)mContext).startActivity(1, bundle);
                }
            });

            if(FileManager.isDownloaded(getActivity(), "http://"+post.getAudioLink())){
                vh.downloadStatus.setVisibility(View.VISIBLE);
            }else{
                vh.downloadStatus.setVisibility(View.GONE);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_preview, parent, false);
            RecyclerView.ViewHolder viewHolder = new PostPreviewHolder(view);
            return viewHolder;
        }
    }

    class PostPreviewHolder extends RecyclerView.ViewHolder{
        public FrameLayout frameLayout;
        public ImageView background;
        public TextView title;
        public TextView description;
        public LinearLayout downloadStatus;

        public PostPreviewHolder(View view){
            super(view);

            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
            background = (ImageView) view.findViewById(R.id.background);
            title = (TextView) view.findViewById(R.id.postTitle);
            description = (TextView) view.findViewById(R.id.postDescription);
            downloadStatus = (LinearLayout) view.findViewById(R.id.downloadStatus);
        }

    }
}
