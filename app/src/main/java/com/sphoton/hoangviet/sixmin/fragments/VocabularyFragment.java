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
import com.sphoton.hoangviet.sixmin.models.Vocabulary;

/**
 * Created by hoangviet on 10/25/16.
 */

public class VocabularyFragment extends Fragment {
    private Vocabulary vocabulary;

    public static VocabularyFragment newInstance(Vocabulary vocabulary) {
        VocabularyFragment fragment = new VocabularyFragment();
        Bundle args = new Bundle();
        args.putSerializable(Commons.VOCABULARY, vocabulary);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vocabulary = (Vocabulary) getArguments().getSerializable(Commons.VOCABULARY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView word = (TextView) view.findViewById(R.id.word);
        word.setText(vocabulary.getWord());

        TextView meaning = (TextView) view.findViewById(R.id.meaning);
        meaning.setText(vocabulary.getMeaning());

    }
}
