package com.syrovama.notesroom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {
    public static final String TAG = "MySettingsActivity";
    public static final String PREF_TEXT_SIZE = "textResourceId";
    public static final String PREF_SETTING_ID = "currentSettingId";
    private RadioButton mCurrentSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRadioButtons();
        setSaveButton();
    }

    private void setSaveButton() {
        Button saveButton = findViewById(R.id.save_settings_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =
                        getSharedPreferences("Settings", Context.MODE_PRIVATE);
                int textResourceId = R.dimen.medium_text;//Default
                switch (mCurrentSetting.getId()) {
                    case R.id.radio_big:
                        textResourceId = R.dimen.big_text;
                        break;
                    case R.id.radio_medium:
                        break;
                    case R.id.radio_small:
                        textResourceId = R.dimen.small_text;
                        break;
                    default:
                        break;
                }
                sharedPreferences.edit()
                        .putInt(PREF_TEXT_SIZE, textResourceId)
                        .putInt(PREF_SETTING_ID, mCurrentSetting.getId())
                        .apply();
                Log.d(TAG, "New setting saved");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setRadioButtons() {
        SharedPreferences sharedPreferences =
                getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(PREF_SETTING_ID, R.id.radio_medium);
        mCurrentSetting = findViewById(id);
        mCurrentSetting.setChecked(true);
        RadioButton radioButton = findViewById(R.id.radio_big);
        radioButton.setOnClickListener(radioButtonClickListener);
        radioButton = findViewById(R.id.radio_medium);
        radioButton.setOnClickListener(radioButtonClickListener);
        radioButton = findViewById(R.id.radio_small);
        radioButton.setOnClickListener(radioButtonClickListener);
    }

    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCurrentSetting.setChecked(false);
            mCurrentSetting = (RadioButton)view;
            mCurrentSetting.setChecked(true);
        }
    };

}
