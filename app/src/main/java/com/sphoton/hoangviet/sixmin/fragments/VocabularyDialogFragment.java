package com.sphoton.hoangviet.sixmin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.adapters.VocabularyViewPagerAdapter;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Vocabulary;

/**
 * Created by hoangviet on 10/25/16.
 */

public class VocabularyDialogFragment extends DialogFragment {
    private Post mPost;
    public static VocabularyDialogFragment newInstance(Post post) {
        VocabularyDialogFragment fragment = new VocabularyDialogFragment();
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
        return inflater.inflate(R.layout.dialog_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        VocabularyViewPagerAdapter vocabularyAdapter = new VocabularyViewPagerAdapter(getChildFragmentManager(), mPost.getVocabularyList());
        viewPager.setAdapter(vocabularyAdapter);

        final TextView count = (TextView) view.findViewById(R.id.count);
        final int total = mPost.getVocabularyList().size();
        count.setText(1+"/"+total);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                count.setText((position+1)+"/"+total);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
