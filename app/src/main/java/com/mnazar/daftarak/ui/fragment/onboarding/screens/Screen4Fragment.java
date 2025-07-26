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

public class Screen4Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen4, container, false);

        TextView btnFinish = view.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> {
            ViewPagerFragment parent = (ViewPagerFragment) requireParentFragment();
            parent.finishOnboarding();
        });

        return view;
    }
}
