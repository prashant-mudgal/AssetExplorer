package com.assetexplorer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.assetexplorer.R;
import com.assetexplorer.utility.AssetExplorerApplication;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.analytics.HitBuilders;


/**
 * Created by prashant.mudgal on 05/18/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    protected void showProgressDialog(final String message, Context context) {
        if (mProgressDialog != null) {
            return;
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected Toolbar setCustomToolbar(boolean isLaunch) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (!isLaunch) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_previous_item));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return mToolbar;
    }

    public void trackEvent(String category, String action, String label) {
        AssetExplorerApplication.tracker().send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
    public void onKeyMetric(String associateId,String associateName,String qrCodeValue,String time) {
        Answers.getInstance().logCustom((new CustomEvent("Asset Explorer").putCustomAttribute("QR Code Scan", "")).
                putCustomAttribute("associateId", associateId)
                .putCustomAttribute("associateName", associateName)
                .putCustomAttribute("qrCodeValue", qrCodeValue)
                .putCustomAttribute("time", time));
    }

}
