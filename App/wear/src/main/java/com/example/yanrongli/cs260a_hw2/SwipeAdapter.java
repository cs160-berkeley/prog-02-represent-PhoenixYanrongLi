package com.example.yanrongli.cs260a_hw2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by yanrongli on 2/28/16.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter{

    String[] NameList;
    String[] PartyList;
    String County;
    String State;
    String ObamaVote;
    String RomneyVote;

    public SwipeAdapter(FragmentManager fm, String[] name_list, String[] party_list, String county, String state, String obama_vote, String romney_vote){
        super(fm);
        NameList = name_list;
        PartyList = party_list;
        County = county;
        State = state;
        ObamaVote = obama_vote;
        RomneyVote = romney_vote;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new SwipeFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("Length", NameList.length);
        bundle.putInt("Position", position);

        if(position < NameList.length) {
            bundle.putString("Name", NameList[position]);
            bundle.putString("Party", PartyList[position]);
        }
        else if(position == NameList.length){
            bundle.putString("County", County);
            bundle.putString("State", State);
            bundle.putString("ObamaVote", ObamaVote);
            bundle.putString("RomneyVote", RomneyVote);
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return NameList.length + 1;
    }
}
