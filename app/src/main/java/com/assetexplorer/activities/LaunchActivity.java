package com.assetexplorer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.assetexplorer.R;
import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class LaunchActivity extends BaseActivity {

    @BindView(R.id.read_barcode)
    Button readBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        setCustomToolbar(true);
        requestPermission();
    }

    /**
     * Called when a view has been clicked.
     */
    @OnClick(R.id.read_barcode)
    public void onClickReadBarcode() {
        logButtonClickEvent();
        startActivity(new Intent(LaunchActivity.this, AssociateDetailsActivity.class));
    }

    private void logButtonClickEvent() {
        trackEvent("Click", "Launch Click", "LaunchScreen");
    }

    private void logUser() {
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length < 1) {
            requestPermission();
        }
    }


}
