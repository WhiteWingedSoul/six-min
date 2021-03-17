package com.sphoton.hoangviet.sixmin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sphoton.hoangviet.sixmin.fragments.VocabularyFragment;
import com.sphoton.hoangviet.sixmin.models.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 10/25/16.
 */

public class VocabularyViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Vocabulary> mDetails;

    public VocabularyViewPagerAdapter(FragmentManager fragmentManager, List<Vocabulary> details) {
        super(fragmentManager);
        mDetails = details;
    }

    @Override
    public Fragment getItem(int position) {
        Vocabulary vocabulary = mDetails.get(position);
        VocabularyFragment fragmentWord = VocabularyFragment.newInstance(vocabulary);

        return fragmentWord;
    }

    public void setmDetails(ArrayList<Vocabulary> mDetails) {
        this.mDetails = mDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDetails.size();
    }
}
