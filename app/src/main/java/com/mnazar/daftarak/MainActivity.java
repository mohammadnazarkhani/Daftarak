package com.mnazar.daftarak;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnazar.daftarak.utility.UiExtensions;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusBannerType;
import ir.tapsell.plus.TapsellPlusInitListener;
import ir.tapsell.plus.model.AdNetworkError;
import ir.tapsell.plus.model.AdNetworks;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private NavController navController;
    private String bannerResponseId = null;
    private ViewGroup bannerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
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

        registerNetworkCallback();

        UiExtensions.setTopToolBarColor(getWindow(), this, getColor(R.color.primary_light));
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fab = findViewById(R.id.fab);
        bannerContainer = findViewById(R.id.banner_container);

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

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(label);
                    getSupportActionBar().show();
                }

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

        // Initialize Tapsell
        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY, new TapsellPlusInitListener() {
            @Override
            public void onInitializeSuccess(AdNetworks adNetworks) {
                Log.d("TapsellInit", "Success: " + adNetworks.name());
            }

            @Override
            public void onInitializeFailed(AdNetworks adNetworks, AdNetworkError adNetworkError) {
                Log.e("TapsellInit", "Failed: " + adNetworkError.getErrorMessage());
            }
        });

        if (isNetworkConnected()) {
            requestBannerAd();
        }
    }

    private void registerNetworkCallback() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                runOnUiThread(() -> {
                    Log.d("NetworkStatus", "Connected");
                    requestBannerAd();
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                runOnUiThread(() -> {
                    Log.d("NetworkStatus", "Disconnected");
                    hideBannerAd();
                });
            }
        };

        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        return caps != null && (
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        );
    }

    private void requestBannerAd() {
        TapsellPlus.requestStandardBannerAd(
                this,
                BuildConfig.TAPSELL_STANDARD_BANNER,
                TapsellPlusBannerType.BANNER_320x50,
                new AdRequestCallback() {
                    @Override
                    public void response(TapsellPlusAdModel adModel) {
                        bannerResponseId = adModel.getResponseId();
                        showBannerAd();
                    }

                    @Override
                    public void error(@NonNull String message) {
                        Log.e("TapsellBanner", "Request Error: " + message);
                        hideBannerAd();
                    }
                });
    }

    private void showBannerAd() {
        if (bannerResponseId == null || bannerContainer == null) return;
        bannerContainer.setVisibility(View.VISIBLE);

        TapsellPlus.showStandardBannerAd(
                this,
                bannerResponseId,
                bannerContainer,
                new AdShowListener() {
                    @Override
                    public void onOpened(TapsellPlusAdModel adModel) {
                        Log.d("TapsellBanner", "Banner opened");
                    }

                    @Override
                    public void onError(TapsellPlusErrorModel error) {
                        Log.e("TapsellBanner", "Show error: " + error.getErrorMessage());
                        hideBannerAd();
                    }
                });
    }

    private void hideBannerAd() {
        if (bannerContainer != null) {
            bannerContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bannerResponseId != null && bannerContainer != null) {
            TapsellPlus.destroyStandardBanner(this, bannerResponseId, bannerContainer);
        }
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
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
