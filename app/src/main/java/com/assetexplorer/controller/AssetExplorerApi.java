package com.assetexplorer.controller;

import android.content.Context;

import com.assetexplorer.core.ApiClient;
import com.assetexplorer.core.ApiResult;
import com.assetexplorer.interfaces.AssetExplorerListener;
import com.assetexplorer.model.ApiPojo;
import com.assetexplorer.utility.AssetExplorerApplication;
import com.assetexplorer.utility.Constants;
import com.google.gson.Gson;

/**
 * Created by Prashant Mudgal on 5/18/2017.
 */

public class AssetExplorerApi implements ApiClient.ApiResponse<ApiResult> {
    private final AssetExplorerListener resultListener;
    private final Context context;
    private String webServiceCall;

    public void callAssetExplorer(String displayValue) {
        webServiceCall = "ASSETEXPLORER";
        String requestBody = "<API version=\"1.0\">\n" +
                "    <citype>\n" +
                "        <name>Workstation</name>\n" +
                "        <criterias>\n" +
                "            <criteria>\n" +
                "                <parameter>\n" +
                "                    <name compOperator=\"IS\">Barcode</name>\n" +
                "                    <value>" + displayValue + "</value>\n" +
                "                </parameter>\n" +
                "            </criteria>\n" +
                "        </criterias>\n" +
                "        <returnFields>\n" +
                "            <!-- Syntax to return all available column(s). <name>*</name> -->\n" +
                "            <name>CI Name</name>\n" +
                "            <name>Serial Number</name>\n" +
                "            <name>Barcode</name>\n" +
                "            <name>User</name>\n" +
                "            <name>Lease Start</name>\n" +
                "            <name>Lease End</name>\n" +
                "            <name>Employee ID</name>\n" +
                "        </returnFields>\n" +
                "        <sortFields sortOrder=\"desc\">\n" +
                "            <name>Product Name</name>\n" +
                "        </sortFields>\n" +
                "    </citype>\n" +
                "</API>\n";

        StringBuilder stringBuilder = new StringBuilder(Constants.SERVER_HOST_NAME);
        stringBuilder.append("?OPERATION_NAME=" + Constants.OPERATION_NAME);
        stringBuilder.append("&TECHNICIAN_KEY=" + Constants.TECHNICIAN_KEY);
        ApiClient.getInstance().requestStringApi(stringBuilder.toString(), this, context, requestBody);
    }

    private enum ViewType {
        ASSETEXPLORER
    }

    public AssetExplorerApi(Context context, AssetExplorerListener resultListener) {
        this.context = context;
        this.resultListener = resultListener;
    }

    @Override
    public void onCompletion(ApiResult result) {
        if (result.getSuccess()) {
            if (ViewType.valueOf(webServiceCall.toUpperCase()) == ViewType.ASSETEXPLORER) {
                parseAssetExplorerResponse(result);
            }
        } else {
            result.getError();
        }
    }

    private void parseAssetExplorerResponse(ApiResult result) {
        Gson gson = AssetExplorerApplication.getInstance().getGson();
        ApiPojo pojo = gson.fromJson(result.getData().toString(), ApiPojo.class);
        resultListener.setResult(pojo);
    }
}