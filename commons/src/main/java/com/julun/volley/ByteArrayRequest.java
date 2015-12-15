package com.julun.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.Map;

/**
 * Created by danjp on 2015/12/10.
 * 字节码请求：验证码图片
 */
@Deprecated
public class ByteArrayRequest extends Request<byte[]> {
    private Map<String, String> param;
    private Response.Listener<byte[]> successListener;

    public ByteArrayRequest(String url, VolleyRequestCallback<byte[]> callback, Map<String, String> map) {
        super(Request.Method.POST, url, callback.errorListener);
//        callback.successListener
//        successListener = callback.successListener;
        param = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        byte[] bytes = response.data;
        return Response.success(bytes, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        successListener.onResponse(response);
    }
}
