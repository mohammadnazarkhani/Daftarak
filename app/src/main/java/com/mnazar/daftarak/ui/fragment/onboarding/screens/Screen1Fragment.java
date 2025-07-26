package com.mnazar.daftarak.ui.fragment.onboarding.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mnazar.daftarak.R;
import com.mnazar.daftarak.ui.fragment.onboarding.ViewPagerFragment;

public class Screen1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen1, container, false);

        TextView btnNext1 = view.findViewById(R.id.btnNext1);
        btnNext1.setOnClickListener(v -> {
            ViewPagerFragment parent = (ViewPagerFragment) requireParentFragment();
            parent.nextPage();
        });

        return view;
    }
}
