package com.example.bmp601_hw.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * مدير التفضيلات
 * يتعامل مع حفظ واسترجاع تفضيلات المستخدم من SharedPreferences
 * خاصة: اختيار الثيم
 */
public class PreferenceManager {
    private static final String PREFERENCE_FILE_NAME = "app_preferences";
    private static final String KEY_THEME = "selected_theme";

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_ACCENT = "accent";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * منشئ PreferenceManager
     * @param context السياق الأندرويدي
     */
    public PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    /**
     * حفظ الثيم المختار
     * @param theme اسم الثيم (THEME_LIGHT, THEME_DARK, أو THEME_ACCENT)
     */
    public void setTheme(String theme) {
        editor.putString(KEY_THEME, theme);
        editor.apply();
    }

    /**
     * الحصول على الثيم المحفوظ
     * @return اسم الثيم (افتراضي: THEME_LIGHT)
     */
    public String getTheme() {
        return preferences.getString(KEY_THEME, THEME_LIGHT);
    }

    /**
     * التحقق من أن الثيم هو Dark Mode
     * @return true إذا كان الثيم مظلم
     */
    public boolean isDarkTheme() {
        return getTheme().equals(THEME_DARK);
    }

    /**
     * التحقق من أن الثيم هو Accent Theme
     * @return true إذا كان الثيم مميز
     */
    public boolean isAccentTheme() {
        return getTheme().equals(THEME_ACCENT);
    }

    /**
     * التحقق من أن الثيم هو Light Mode
     * @return true إذا كان الثيم فاتح
     */
    public boolean isLightTheme() {
        return getTheme().equals(THEME_LIGHT);
    }
}
