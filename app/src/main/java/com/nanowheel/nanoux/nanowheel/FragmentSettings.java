package com.nanowheel.nanoux.nanowheel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class FragmentSettings extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtil test = SharedPreferencesUtil.getPrefs(getActivity());
        test.registerListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(com.nanowheel.nanoux.nanowheel.R.xml.preferences);

        Preference button = getPreferenceManager().findPreference("buttonUpdate");
        if (SharedPreferencesUtil.getPrefs(getContext()).isUpdateAvailable()){
            button.setTitle("\uD83D\uDEA8 UPDATE IS AVAILABLE! \uD83D\uDEA8");
            button.setSummary("Click to download new version");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SharedPreferencesUtil.getPrefs(getContext()).getLastUpdateURL()));
                    startActivity(intent);
                    return true;
                }
            });
        }else{
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nanoux/Onewave"));
                    startActivity(intent);
                    return true;
                }
            });
        }

        Preference button2 = getPreferenceManager().findPreference("buttonDisconnect");
        button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                BluetoothService.killService(getActivity(),false);
                return true;
            }
        });
/*
        Preference button2 = getPreferenceManager().findPreference("buttonRate");  //RIP
        button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (BuildConfig.FLAVOR == "pro") {
                    intent.setData(Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.nanowheel.nanoux.nanowheel.pro"));
                }else{
                    intent.setData(Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.nanowheel.nanoux.nanowheel"));
                }
                intent.setPackage("com.android.vending");
                startActivity(intent);
                return true;
            }
        });*/

        Preference button3 = getPreferenceManager().findPreference("buttonEmail");
        button3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"shenanouxgans@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Onewave Feedback");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            }
        });

        Preference button4 = getPreferenceManager().findPreference("buttonpOne");
        button4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:/github.com/ponewheel/android-ponewheel"));
                startActivity(intent);
                return true;
            }
        });

        Preference button5 = getPreferenceManager().findPreference("buttonProg");
        if (button5 != null) {
            button5.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:/github.com/lzyzsd/CircleProgress"));
                    startActivity(intent);
                    return true;
                }
            });
        }

        Preference button6 = getPreferenceManager().findPreference("buttonChart");
        if (button6 != null) {
            button6.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:/github.com/PhilJay/MPAndroidChart"));
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SharedPreferencesUtil.LOG_TRIPS:
                SwitchPreference pref = (SwitchPreference)findPreference(SharedPreferencesUtil.LOG_TRIPS);
                pref.setChecked(SharedPreferencesUtil.getPrefs(getContext()).isMap());
                break;
            case SharedPreferencesUtil.FLOAT_STAT:
                SwitchPreference pref2 = (SwitchPreference)findPreference(SharedPreferencesUtil.FLOAT_STAT);
                pref2.setChecked(SharedPreferencesUtil.getPrefs(getContext()).isFloatStat());
                break;
        }

    }

}
