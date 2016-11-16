package com.sphoton.hoangviet.sixmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Sentence;
import com.sphoton.hoangviet.sixmin.models.Vocabulary;
import com.sphoton.hoangviet.sixmin.thirdparties.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 10/18/16.
 */

public class PostContentFragment extends Fragment{
    public static PostContentFragment newInstance(Post post) {
        PostContentFragment fragment = new PostContentFragment();
        Bundle args = new Bundle();
        args.putSerializable(Commons.POST, post);
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
        return inflater.inflate(R.layout.fragment_post_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Post post = (Post)getArguments().getSerializable(Commons.POST);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        PostContentAdapter adapter = new PostContentAdapter(getActivity());
        adapter.updateAdapter(post.getContent());

        recyclerView.setAdapter(adapter);

        //postContent.setText(post.getContent());
    }

    class PostContentAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<Sentence> mList = new ArrayList<>();

        public PostContentAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<Sentence> list){
            mList.clear();
            mList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SentenceViewHolder vh = (SentenceViewHolder) holder;
            final Sentence sentence = mList.get(position);
            vh.word.setText(sentence.getSpeaker());
            vh.meaning.setText(sentence.getSentence());

            vh.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
            RecyclerView.ViewHolder viewHolder = new SentenceViewHolder(view);
            return viewHolder;
        }
    }

    class SentenceViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout frameLayout;
        public TextView word;
        public TextView meaning;

        public SentenceViewHolder(View view){
            super(view);

            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
            word = (TextView) view.findViewById(R.id.word);
            meaning = (TextView) view.findViewById(R.id.meaning);
        }

    }
}
