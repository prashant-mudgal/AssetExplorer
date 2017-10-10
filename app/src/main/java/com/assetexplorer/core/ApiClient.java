package com.assetexplorer.core;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.assetexplorer.R;
import com.assetexplorer.activities.BaseActivity;
import com.assetexplorer.utility.AssetExplorerApplication;
import com.assetexplorer.utility.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Prashant Mudgal on 05/18/2017.
 */


public class ApiClient extends BaseActivity {
    private static final String TAG = "<<<<Api Response>>>>";
    private static final String REQUEST_TAG = "<<<< Request >>>>";
    private static final int TIME_OUT_MILLIS = 35000;
    private static final int MAX_RETRIES = 0;
    private static ApiClient mInstance = null;
    private Context context;

    //Create a singleton
    public static synchronized ApiClient getInstance() {
        if (null == mInstance)
            mInstance = new ApiClient();
        return mInstance;
    }

    //create an interface with a callback method
    public interface ApiResponse<T> {
        void onCompletion(T result);
    }

    //Create a get request with the url to query, and a callback
    public void requestApiGet(String url, ApiResponse<ApiResult> completion, boolean isAsync, Context context) {
        this.context = context;
        if (isAsync)
            requestApi(Request.Method.GET, url, null, completion);
        else
            requestApi(Request.Method.GET, url, null, completion, false);
    }

    //Create a get request with the url to query, and a callback
    public void requestApiPost(String url, final JSONObject params, ApiResponse<ApiResult> completion, boolean isAsync, Context context) {
        this.context = context;
        if (isAsync)
            requestApi(Request.Method.POST, url, params, completion);
        else
            requestApi(Request.Method.POST, url, params, completion, false);
    }

    private void requestApi(int method, String url, final JSONObject params, final ApiResponse<ApiResult> completion, boolean isAsync) {
        showProgressDialog(context.getResources().getString(R.string.loading), context);
        AssetExplorerApplication.getInstance().getRequestQueue().getCache().remove(url);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, params, future, future) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                return hashMap;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT_MILLIS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AssetExplorerApplication.getInstance().addToRequestQueue(jsonRequest);
        try {
            JSONObject response = future.get();
            dismissProgressDialog();
            parseResponse(response, completion);
        } catch (InterruptedException e) {
            LogHelper.e(TAG, e.toString());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof VolleyError) {
                //grab the volley error from the throwable and cast it back
                VolleyError volleyError = (VolleyError) e.getCause();
                parseErrorResponse(volleyError, completion);
            } else {
                parseErrorResponse(new VolleyError(e), completion);
            }
        }
    }

    /**
     * @param method     : type of request this is POST, PUT etc
     * @param url        : url to query
     * @param params     : parameters to pass in the post request
     * @param completion
     */
    private void requestApi(int method, String url, final JSONObject params, final ApiResponse<ApiResult> completion) {
        LogHelper.v("Performing request: ", url);
        showProgressDialog(context.getResources().getString(R.string.loading), context);
        AssetExplorerApplication.getInstance().getRequestQueue().getCache().remove(url);
        if (params != null) {
            LogHelper.v(REQUEST_TAG, params.toString());
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(method, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgressDialog();
                        parseResponse(response, completion);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                parseErrorResponse(error, completion);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                return hashMap;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT_MILLIS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AssetExplorerApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * @param url        : url to query
     * @param params     : parameters to pass in the post request
     * @param completion
     */

    public void requestApiPost(String url, final JSONObject params, final ApiResponse<ApiResult> completion) {
        requestApi(Request.Method.POST, url, params, completion);
    }

    /**
     * @param url        : url to query
     * @param params     : parameters to pass in the post request
     * @param completion
     */

    public void requestApiPut(String url, final JSONObject params, final ApiResponse<ApiResult> completion) {
        requestApi(Request.Method.PUT, url, params, completion);
    }

    private ApiError getVolleyApiError(VolleyError error) {
        ApiError apiError = null;

        if (error instanceof TimeoutError) {
            apiError = new ApiError(ApiErrorType.TIMEOUT_ERROR, AssetExplorerApplication.getInstance().getString(R.string.error_network_timeout));
        } else if (error instanceof NoConnectionError) {
            apiError = new ApiError(ApiErrorType.NO_CONNECTION_ERROR, AssetExplorerApplication.getInstance().getString(R.string.error_no_connection));
        } else if (error instanceof NetworkError) {
            apiError = new ApiError(ApiErrorType.NETWORK_ERROR, AssetExplorerApplication.getInstance().getString(R.string.error_network));
        } else if (error instanceof ParseError) {
            apiError = new ApiError(ApiErrorType.PARSE_ERROR, AssetExplorerApplication.getInstance().getString(R.string.error_parse));
        } else if (error instanceof ServerError) {
            apiError = new ApiError(ApiErrorType.SERVER_ERROR, AssetExplorerApplication.getInstance().getString(R.string.error_server));
        } else {
            apiError = new ApiError(ApiErrorType.UNKNOWN, AssetExplorerApplication.getInstance().getString(R.string.unknown_error));
        }

        return apiError;
    }

    private void parseErrorResponse(VolleyError error, ApiResponse<ApiResult> result) {
        ApiResult res = new ApiResult();
        res.setSuccess(false);
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorStr = new String(error.networkResponse.data);
                LogHelper.e(TAG, errorStr);

            } catch (Exception e) {
                res.setError(getVolleyApiError(error));
                LogHelper.v("exception catch", e.getMessage());
            }
        } else {
            res.setError(getVolleyApiError(error));
        }
        result.onCompletion(res);
    }

    private void parseResponse(JSONObject response, ApiResponse<ApiResult> result) {
        ApiResult res = new ApiResult();
        if (response != null) {
            LogHelper.v(TAG, response.toString());
            try {
                if (response.get("data") instanceof JSONObject) {
                    res.setData(response.getJSONObject("data"));
                } else if (response.get("data") instanceof JSONArray) {
                    res.setData(response.getJSONArray("data"));
                }
            } catch (JSONException e) {
                LogHelper.d(TAG, e.toString());
                res.setData(response);
            }
            //Remove below 2 lines later when api format is fixed
            res.setSuccess(true);
        } else {
            res.setMessage("Object is Null");
        }
        result.onCompletion(res);
    }


    /**
     * @param url         : url to query
     * @param completion
     * @param requestBody
     */
    public void requestStringApi(String url, final ApiResponse<ApiResult> completion, Context context, final String requestBody) {


        LogHelper.v("Performing request: ", url);
        showProgressDialog(context.getResources().getString(R.string.loading), context);
        AssetExplorerApplication.getInstance().getRequestQueue().getCache().remove(url);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogHelper.v("Response :: ", response);
                        dismissProgressDialog();
                        parseAssetExplorerResponse(response, completion);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.v("Error :: ", error.toString());
                dismissProgressDialog();
                parseErrorResponse(error, completion);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                return hashMap;
            }

            @Override
            public String getBodyContentType() {
                return "text/xml; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    LogHelper.v("Unsupported Encoding", requestBody);
                    return new byte[0];
                }
            }

        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT_MILLIS, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AssetExplorerApplication.getInstance().addToRequestQueue(jsonRequest);
    }

    private void parseAssetExplorerResponse(String response, ApiResponse<ApiResult> result) {
        LogHelper.v("Performing RequestAssetExplorer: ", response);
        ApiResult res = new ApiResult();
        if (response != null) {
            LogHelper.v(TAG, response);
            try {
                JSONObject json = new JSONObject(response);
                res.setData(json);
            } catch (JSONException e) {
                LogHelper.d(TAG, e.toString());
                res.setData(response);
            }
            //Remove below 2 lines later when api format is fixed
            res.setSuccess(true);
        } else {
            res.setMessage("Object is Null");
        }
        result.onCompletion(res);
    }
}