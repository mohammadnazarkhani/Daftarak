package com.example.daftarak.ui.fragment.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.daftarak.R;
import com.example.daftarak.ui.fragment.onboarding.screens.Screen1Fragment;
import com.example.daftarak.ui.fragment.onboarding.screens.Screen2Fragment;
import com.example.daftarak.ui.fragment.onboarding.screens.Screen3Fragment;
import com.example.daftarak.ui.fragment.onboarding.screens.Screen4Fragment;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Screen1Fragment());
        fragmentList.add(new Screen2Fragment());
        fragmentList.add(new Screen3Fragment());
        fragmentList.add(new Screen4Fragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(
                fragmentList,
                getChildFragmentManager(),
                getLifecycle()
        );

        viewPager.setAdapter(adapter);
    }
}
