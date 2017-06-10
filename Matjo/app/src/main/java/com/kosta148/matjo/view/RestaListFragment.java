package com.kosta148.matjo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosta148.matjo.R;

/**
 * Created by Daehee on 2017-04-26.
 */

public class RestaListFragment extends Fragment {
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_resta_list, container, false);
        mainActivity = (MainActivity) getActivity();

        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), ScrollingActivity.class);
                mainActivity.startActivity(intent);
            }
        });

        return v;
    } // end of onCreateView
}
