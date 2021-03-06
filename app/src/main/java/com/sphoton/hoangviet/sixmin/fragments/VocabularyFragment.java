package com.sphoton.hoangviet.sixmin.fragments;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sphoton.hoangviet.sixmin.Commons;
import com.sphoton.hoangviet.sixmin.R;
import com.sphoton.hoangviet.sixmin.models.Post;
import com.sphoton.hoangviet.sixmin.models.Vocabulary;

import org.w3c.dom.Text;

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

        CardView flashcardLayout = (CardView) view.findViewById(R.id.flashcardLayout);

        final TextView word = (TextView) view.findViewById(R.id.word);
        final TextView wordTitle = (TextView) view.findViewById(R.id.wordTitle);
        word.setText(vocabulary.getWord());

        final TextView meaning = (TextView) view.findViewById(R.id.meaning);
        final TextView meaningTitle = (TextView) view.findViewById(R.id.meaningTitle);
        meaning.setRotationY(180);
        meaning.setAlpha(0);
        meaningTitle.setRotationY(180);
        meaningTitle.setAlpha(0);
        meaning.setText(vocabulary.getMeaning());
        flashcardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.animate().rotationY(180-view.getRotationY()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if(word.getAlpha() == 1) {
                            word.animate().alpha(0).setDuration(200);
                            meaning.animate().setStartDelay(50).alpha(1);
                            wordTitle.animate().alpha(0).setDuration(200);
                            meaningTitle.animate().setStartDelay(50).alpha(1);
                            //word.setVisibility(View.INVISIBLE);
                        }
                        else {
                            //meaning.setVisibility(View.INVISIBLE);
                            meaning.animate().alpha(0).setDuration(200);
                            word.animate().setStartDelay(50).alpha(1);
                            meaningTitle.animate().alpha(0).setDuration(200);
                            wordTitle.animate().setStartDelay(50).alpha(1);
                        }

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
//                        if(word.getVisibility() == View.VISIBLE) {
//                            word.setVisibility(View.GONE);
//                            word.setAlpha(1);
//                            //meaning.setVisibility(View.VISIBLE);
//                        }
//                        else {
//                            meaning.setVisibility(View.GONE);
//                            meaning.setAlpha(1);
//                            //word.setVisibility(View.VISIBLE);
//                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });

    }
}
