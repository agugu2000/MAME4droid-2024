package com.seleuco.mame4droid.prefs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.seleuco.mame4droid.Emulator;
import com.seleuco.mame4droid.R;

import java.util.Locale;

public class LanguageSwitchActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference customLanguagePref;
    private ListPreference languageSelectionPref;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);

        customLanguagePref = (CheckBoxPreference) findPreference("custom_language");
        languageSelectionPref = (ListPreference) findPreference("language_selection");
        context = this;

        customLanguagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showCheckBoxConfirmationDialog();
                return true;
            }
        });

        languageSelectionPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                showListPreferenceConfirmationDialog();
                return true;
            }
        });

        updateLanguage(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        updateLanguage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateLanguage(this);
    }

    public static void updateLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean customLanguage = prefs.getBoolean("custom_language", false);
        String language = prefs.getString("language_selection", "en");

        //so we can use "zh-CN" "zh_TW" "en" :)
        language = language.replace("-", "_");
        String[] languageparts = language.split("_");

        Locale locale;
        if (customLanguage) {
            if (languageparts.length == 2) {
                locale = new Locale(languageparts[0], languageparts[1]);
            } else {
                locale = new Locale(language);
            }
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        Locale currentLocale = context.getResources().getConfiguration().locale;
        if (!currentLocale.equals(locale)) {
            Locale.setDefault(locale);
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }

    private void showCheckBoxConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.checkboxprefwithwarn_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        customLanguagePref.setChecked(!customLanguagePref.isChecked());
                        Emulator.setNeedRestart(true);
                    }
                })
                .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showListPreferenceConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.checkboxprefwithwarn_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Emulator.setNeedRestart(true);
                    }
                })
                .setNegativeButton(R.string.confirm_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}