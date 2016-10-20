package com.sphoton.hoangviet.sixmin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.models.Post;

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

        TextView postContent = (TextView) view.findViewById(R.id.postContent);
        postContent.setText(post.getContent());
    }
}
