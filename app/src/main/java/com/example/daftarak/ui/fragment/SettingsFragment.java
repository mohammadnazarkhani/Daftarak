package com.example.daftarak.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.daftarak.R;
import com.example.daftarak.utility.LocaleHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "app_prefs";
    private static final String PREF_THEME = "pref_theme";
    private static final String PREF_LANGUAGE = "pref_language";

    private Spinner themeSpinner, languageSpinner;
    private View rootView;  // Store fragment root view for Snackbar anchor

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view;  // save root view for Snackbar

        themeSpinner = view.findViewById(R.id.spinner_theme);
        languageSpinner = view.findViewById(R.id.spinner_language);

        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.theme_options,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.language_options,
                android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String themePref = prefs.getString(PREF_THEME, "system");
        String langPref = prefs.getString(PREF_LANGUAGE, "system");

        themeSpinner.setSelection(getThemeIndex(themePref));
        languageSpinner.setSelection(getLanguageIndex(langPref));

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isInitialSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSelection) {
                    isInitialSelection = false;
                    return; // Ignore initial callback
                }

                String selected = parent.getItemAtPosition(position).toString().toLowerCase(Locale.ROOT);
                if (selected.equals(themePref)) return; // no change

                prefs.edit().putString(PREF_THEME, selected).apply();

                // Apply theme immediately for better UX
                switch (selected) {
                    case "light":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case "dark":
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    default:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }

                Snackbar.make(rootView, "Theme updated", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean isInitialSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitialSelection) {
                    isInitialSelection = false;
                    return; // Ignore initial callback
                }

                String selected = parent.getItemAtPosition(position).toString().toLowerCase(Locale.ROOT);
                if (selected.equals(langPref)) return; // no change

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(PREF_LANGUAGE, selected).apply();

                // Apply locale change
                LocaleHelper.setLocale(requireContext(), selected);

                View bottomNav = requireActivity().findViewById(R.id.nav_host_fragment);
                Snackbar.make(bottomNav, "Language changed. Please restart the app to apply.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private int getThemeIndex(String value) {
        switch (value) {
            case "light": return 1;
            case "dark": return 2;
            default: return 0;
        }
    }

    private int getLanguageIndex(String value) {
        switch (value) {
            case "english": return 1;
            case "persian": return 2;
            default: return 0;
        }
    }
}
