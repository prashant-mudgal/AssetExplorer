package com.assetexplorer.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assetexplorer.R;
import com.assetexplorer.controller.AssetExplorerApi;
import com.assetexplorer.interfaces.AssetExplorerListener;
import com.assetexplorer.model.ApiPojo;
import com.assetexplorer.model.FieldNameValuePojo;
import com.assetexplorer.model.NamePojo;
import com.assetexplorer.model.QrCodeDetails;
import com.assetexplorer.utility.Constants;
import com.assetexplorer.utility.LogHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssociateDetailsActivity extends BaseActivity implements AssetExplorerListener {

    private static final String TAG = AssociateDetailsActivity.class.getSimpleName();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    @BindView(R.id.ivassociatephoto)
    SimpleDraweeView ivassociatephoto;

    @BindView(R.id.layoutouter)
    LinearLayout layoutouter;

    private AssetExplorerApi assetExplorerApi;

    @BindView(R.id.tvresponse)
    TextView tvresponse;

    @BindView(R.id.dbv_barcode)
    DecoratedBarcodeView dbvBarcode;
    private Toolbar toolbar;
    private SearchView searchView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate_details);
        ButterKnife.bind(this);
        assetExplorerApi = new AssetExplorerApi(this, this);
        toolbar = setCustomToolbar(false);
        setDecoratedBarCodeView();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("AssetTracking");
        this.mDatabase.setValue("Asset Tracking");

    }

    private void setDecoratedBarCodeView() {
        dbvBarcode.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                layoutouter.setVisibility(View.GONE);
                if (result.getText().toString() != null && result.getText().toString().length() != 0) {
                    assetExplorerApi.callAssetExplorer(result.getText().toString());
                }
                beepSound();
                dbvBarcode.pause();
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // possible result points
            }
        });
    }

    protected void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            LogHelper.e(TAG, e.toString());
        }
    }

    @Override
    public void setResult(ApiPojo result) {

        List<String> fieldValuesList = null;
        List<NamePojo> fieldNameList = null;
        if (result.getAPI().getResponse().getOperation().getDetails().getFieldValues().getRecord() != null) {
            fieldValuesList = result.getAPI().getResponse().getOperation().getDetails().getFieldValues().getRecord().getValue();
        }
        if (result.getAPI().getResponse().getOperation().getDetails().getFieldNames() != null) {
            fieldNameList = result.getAPI().getResponse().getOperation().getDetails().getFieldNames().getName();
        }
        if (fieldNameList != null && !fieldNameList.isEmpty() &&
                fieldValuesList != null && !fieldValuesList.isEmpty()) {
            Iterator<String> valuesIterator = fieldValuesList.iterator();
            Iterator<NamePojo> namesIterator = fieldNameList.iterator();
            Map<String, FieldNameValuePojo> map = new HashMap<>();
            while (valuesIterator.hasNext() && namesIterator.hasNext()) {
                NamePojo pojo = namesIterator.next();
                map.put(pojo.getContent(), new FieldNameValuePojo(pojo.getContent(), valuesIterator.next(), pojo.getType()));
            }
            StringBuilder stringBuilder = new StringBuilder();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                FieldNameValuePojo fieldNameValuePojo = (FieldNameValuePojo) pair.getValue();
                if (fieldNameValuePojo.getFieldValue() != null && !fieldNameValuePojo.getFieldValue().equalsIgnoreCase("(null)")) {
                    if (fieldNameValuePojo.getFieldType().equalsIgnoreCase("Date")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(fieldNameValuePojo.getFieldValue()));
                        fieldNameValuePojo.setFieldValue(simpleDateFormat.format(calendar.getTime()));
                    }
                    stringBuilder.append(fieldNameValuePojo.getFieldName() + " : " + fieldNameValuePojo.getFieldValue() + "\n");
                }
            }
            tvresponse.setText(stringBuilder.toString());

            if (map.containsKey("Employee ID") && map.containsKey("User")) {
                FieldNameValuePojo empIdPojo = map.get("Employee ID");
                FieldNameValuePojo empNamePojo = map.get("User");
                saveScannedData(map.get(Constants.BARCODE).getFieldValue(), empIdPojo.getFieldValue(), empNamePojo.getFieldValue());
                onKeyMetric(empIdPojo.getFieldValue(), empNamePojo.getFieldValue(), map.get(Constants.BARCODE).getFieldValue(),
                        this.simpleDateFormat.format(Calendar.getInstance().getTime()));
                HashMap<String, String> firebaseParams = new HashMap<>();
                firebaseParams.put("BarCode",map.get(Constants.BARCODE).getFieldValue());
                firebaseParams.put("EmployeeId",empIdPojo.getFieldValue());
                firebaseParams.put("EmployeeName",empNamePojo.getFieldValue());
                logFirebaseEvent(this, "QRCodeScan", firebaseParams);
            }
            setAssociatePhoto(map);

        } else {
            layoutouter.setVisibility(View.GONE);
            tvresponse.setText("No Record found.");
        }
        resumeBarcodeAfterDelay();

    }
    public void logFirebaseEvent(Context context, String eventName, HashMap params) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            bundle.putString(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }
    private void resumeBarcodeAfterDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //resume  barcode scanning after a delay
                dbvBarcode.resume();
            }
        }, 2000);
    }

    private void setOuterBorder(Map<String, FieldNameValuePojo> map) {
        layoutouter.setVisibility(View.VISIBLE);
        if (compareDate(map.get("Lease End").getFieldValue())) {
            layoutouter.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        } else {
            layoutouter.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    private void setAssociatePhoto(Map<String, FieldNameValuePojo> map) {
        FieldNameValuePojo empIdPojo = map.get("Employee ID");
        if (!empIdPojo.getFieldValue().equalsIgnoreCase("(null)")) {
            StringBuilder imageUrlBuilder = new StringBuilder(Constants.IMAGE_SERVER_HOST_NAME);
            imageUrlBuilder.append("Asso_ID_" + empIdPojo.getFieldValue() + ".jpg");
            Uri uri = Uri.parse(imageUrlBuilder.toString());
            LogHelper.d(TAG, "Associate Image URL : "+imageUrlBuilder.toString());
            ivassociatephoto.setImageURI(uri);
            setOuterBorder(map);
        } else {
            layoutouter.setVisibility(View.GONE);
        }
    }

    private boolean compareDate(final String endDateString) {
        try {
            Calendar currentCalendarInstance = Calendar.getInstance();
            Date currentDate = currentCalendarInstance.getTime();
            Date endDate = simpleDateFormat.parse(endDateString);
            return endDate.after(currentDate);
        } catch (ParseException e) {
            LogHelper.e(TAG, e.toString());
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!dbvBarcode.isActivated()) {
            dbvBarcode.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbvBarcode.pause();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.app_menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new QueryListener());
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        searchView.setQueryHint(getResources().getString(R.string.enter_qrcode_value));
        return super.onCreateOptionsMenu(menu);
    }

    class QueryListener implements SearchView.OnQueryTextListener {

        public boolean onQueryTextChange(String newText) {
            // this is your adapter that will be filtered
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            assetExplorerApi.callAssetExplorer(query.toUpperCase());
            searchView.clearFocus();
            return true;
        }
    }

    private void saveScannedData(String qrCodeValue, String associateId, String associateName) {
        if (TextUtils.isEmpty(associateId)) {
            associateId = this.mDatabase.push().getKey();
        }
        this.mDatabase.child(associateId).setValue(new QrCodeDetails(qrCodeValue, associateId, associateName, this.simpleDateFormat.format(Calendar.getInstance().getTime())));
        trackEvent();
    }

    private void trackEvent() {
        trackEvent("QRCode", "Scan", "QRCodeScanning");
    }

}
