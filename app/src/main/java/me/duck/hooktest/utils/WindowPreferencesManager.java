package me.duck.hooktest.utils;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Window;
import android.view.WindowInsets;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;

import com.google.android.material.internal.EdgeToEdgeUtils;


/**
 * from material-components-android
 */
public class WindowPreferencesManager {
    private final OnApplyWindowInsetsListener listener;

    @SuppressLint("WrongConstant")
    @SuppressWarnings("deprecation")
    public WindowPreferencesManager() {
        listener = (v, insets) -> {
            if (v.getResources().getConfiguration().orientation
                    != Configuration.ORIENTATION_LANDSCAPE) {
                return insets;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                v.setPadding(
                        insets.getInsets(WindowInsets.Type.systemBars()).left,
                        0,
                        insets.getInsets(WindowInsets.Type.systemBars()).right,
                        insets.getInsets(WindowInsets.Type.systemBars()).bottom);
            } else {

                v.setPadding(
                        insets.getStableInsetLeft(),
                        0,
                        insets.getStableInsetRight(),
                        insets.getStableInsetBottom());
            }
            return insets;
        };
    }


    @SuppressLint("RestrictedApi")
    @SuppressWarnings("RestrictTo")
    public void applyEdgeToEdgePreference(Window window) {
        EdgeToEdgeUtils.applyEdgeToEdge(window, true);
        ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), listener);
    }


}
