package com.example.daftarak.ui.fragment.onboarding.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daftarak.R;
import com.example.daftarak.ui.fragment.onboarding.ViewPagerFragment;

public class Screen3Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen3, container, false);

        TextView btnNext3 = view.findViewById(R.id.btnNext3);
        btnNext3.setOnClickListener(v -> {
            ViewPagerFragment parent = (ViewPagerFragment) requireParentFragment();
            parent.nextPage();
        });

        return view;
    }
}
