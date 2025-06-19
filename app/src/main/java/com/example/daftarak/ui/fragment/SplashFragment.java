package com.example.daftarak.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daftarak.MainActivity;
import com.example.daftarak.R;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.post(() -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isAdded()) return;

                boolean isFirstTime = requireContext()
                        .getSharedPreferences("onboarding", 0)
                        .getBoolean("firstTime", true);

                if (isFirstTime) {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_splashFragment_to_viewPagerFragment);
                } else {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_splashFragment_to_notebooksFragment);
                }
            }, 2000);
        });
    }


}