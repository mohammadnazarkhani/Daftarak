package com.example.daftarak;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.daftarak.utility.UiExtensions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private NavController navController; // Store for reuse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        // Apply theme preference before setContentView
        String themePref = prefs.getString("pref_theme", "system");
        switch (themePref) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        setContentView(R.layout.activity_main);

        UiExtensions.setTopToolBarColor(getWindow(), this, getColor(R.color.primary_light));

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fab = findViewById(R.id.fab);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                String label = destination.getLabel() != null ? destination.getLabel().toString() : "unknown";
                Log.d("MainActivity", "Navigated to destination: id=" + id + ", label=" + label);

                // Action bar title
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(label);
                    getSupportActionBar().show();
                }

                // Show/hide bottom UI
                if (id != R.id.splashFragment && id != R.id.viewPagerFragment) {
                    showUI();
                } else {
                    hideUI();
                }

                if (id == R.id.notebooksFragment || id == R.id.notesFragment) {
                    showUI();
                } else {
                    hideFab();
                }
            });
        }

        fab.setOnClickListener(v -> {
            if (navController == null || navController.getCurrentDestination() == null) return;

            int currentDestId = navController.getCurrentDestination().getId();

            if (currentDestId == R.id.notebooksFragment) {
                navController.navigate(R.id.createNotebookDialogFragment);

            } else if (currentDestId == R.id.notesFragment) {
                NavBackStackEntry backStackEntry = navController.getCurrentBackStackEntry();
                if (backStackEntry != null) {
                    Bundle args = backStackEntry.getArguments();
                    if (args != null && args.containsKey("notebook_id")) {
                        int notebookId = args.getInt("notebook_id", -1);
                        if (notebookId != -1) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("notebook_id", notebookId);
                            navController.navigate(R.id.addNoteDialogFragment, bundle);
                        } else {
                            Toast.makeText(this, R.string.please_select_a_notebook_first, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.please_select_a_notebook_first, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.navigation_state_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hideUI() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.GONE);
        hideFab();
        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }

    public void showFab() {
        if (fab != null && fab.getVisibility() != View.VISIBLE) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    public void hideFab() {
        if (fab != null && fab.getVisibility() != View.GONE) {
            fab.setVisibility(View.GONE);
        }
    }

    public void showUI() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.VISIBLE);
        showFab();
        if (getSupportActionBar() != null) getSupportActionBar().show();
        Window window = getWindow();
        UiExtensions.setNavigationBarColor(window, getColor(R.color.secondary));
    }
}
