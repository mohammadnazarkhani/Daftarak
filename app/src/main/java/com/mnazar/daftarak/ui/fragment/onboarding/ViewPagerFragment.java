package com.mnazar.daftarak.ui.fragment.onboarding;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mnazar.daftarak.MainActivity;
import com.mnazar.daftarak.R;
import com.mnazar.daftarak.ui.fragment.onboarding.screens.*;

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

        if (getActivity() instanceof MainActivity) {
            MainActivity main = (MainActivity) getActivity();
            main.hideUI(); // method you create to hide FAB and BottomNav
        }

        viewPager = view.findViewById(R.id.viewPager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Screen1Fragment());
        fragments.add(new Screen2Fragment());
        fragments.add(new Screen3Fragment());
        fragments.add(new Screen4Fragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragments, getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
    }

    public void nextPage() {
        if (viewPager.getCurrentItem() < 3) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void finishOnboarding() {
        SharedPreferences prefs = requireContext().getSharedPreferences("onboarding", 0);
        prefs.edit().putBoolean("firstTime", false).apply();

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_viewPagerFragment_to_notebooksFragment);
    }

}
