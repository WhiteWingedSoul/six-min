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
import com.sphoton.hoangviet.sixmin.models.Vocabulary;
import com.sphoton.hoangviet.sixmin.thirdparties.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 10/13/16.
 */
public class PostVocabularyFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostVocabularyAdapter adapter;
    private Post mPost;

    public static PostVocabularyFragment newInstance(Post post) {
        PostVocabularyFragment fragment = new PostVocabularyFragment();
        Bundle args = new Bundle();
        args.putSerializable(Commons.POST, post);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPost = (Post) getArguments().getSerializable(Commons.POST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        PostVocabularyAdapter adapter = new PostVocabularyAdapter(getActivity());
        adapter.updateAdapter(mPost.getVocabularyList());

        recyclerView.setAdapter(adapter);
    }

    class PostVocabularyAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<Vocabulary> mList = new ArrayList<>();

        public PostVocabularyAdapter(Context context){
            super();
            mContext = context;
        }

        public void updateAdapter(List<Vocabulary> list){
            mList.clear();
            mList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VocabularyViewHolder vh = (VocabularyViewHolder) holder;
            final Vocabulary vocabulary = mList.get(position);
            vh.word.setText(vocabulary.getWord());
            vh.meaning.setText(vocabulary.getMeaning());

            vh.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
            RecyclerView.ViewHolder viewHolder = new VocabularyViewHolder(view);
            return viewHolder;
        }
    }

    class VocabularyViewHolder extends RecyclerView.ViewHolder{
        public FrameLayout frameLayout;
        public TextView word;
        public TextView meaning;

        public VocabularyViewHolder(View view){
            super(view);

            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
            word = (TextView) view.findViewById(R.id.word);
            meaning = (TextView) view.findViewById(R.id.meaning);
        }

    }
}
