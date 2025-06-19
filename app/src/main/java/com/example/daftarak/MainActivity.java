package com.example.daftarak;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.daftarak.R;
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
            });
        }

        fab.setOnClickListener(v -> {
            if (navController != null &&
                    navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == R.id.notebooksFragment) {

                navController.navigate(R.id.createNotebookDialogFragment);
            }
        });
    }

    public void hideUI() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.GONE);
        if (fab != null) fab.setVisibility(View.GONE);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }

    public void showUI() {
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.VISIBLE);
        if (fab != null) fab.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) getSupportActionBar().show();
        Window window = getWindow();
        UiExtensions.setNavigationBarColor(window, getColor(R.color.secondary));
    }
}
