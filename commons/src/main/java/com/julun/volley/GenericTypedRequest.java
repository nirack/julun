package com.julun.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <ul>
 *     <li>
 *          1.继承 Request 的时候，使用的泛型是String,因为无论解析成任何其他的类型,首先都是String.
 *     </li>
 *     <li>
 *         2.不要重写 #getParams ,#getBodyContentType 之类的,否则又有一大堆的 方法要重写,
 *         如果一些方法没有覆盖,则会出现服务端收不到参数 .而要覆盖其他配套的方法以达到参数能够正确的传递,我只想说：请不要这么蛋疼!!!!
 *     </li>
 *
 *</ul>
 * VolleyRequestCallback配合这个类使用,但是通常使用者不需要自行实例化这个类.只需要实现 VolleyRequestCallback.
 * @see  VolleyRequestCallback
 *
 */
public class GenericTypedRequest extends Request<String> {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private Map<String, String> param;
    private Response.Listener<String> successListener;

    public GenericTypedRequest(String url, VolleyRequestCallback<String> callback, Map<String, String> map) {
        super(Request.Method.POST, url, callback.getErrorListener());
        successListener = callback.getSuccessListener();
        param = map;
    }

    /**
     * 通过这个方法解决普通参数传递问题.这个一定要覆盖，一定要覆盖，一定要覆盖！！！！！
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return param;
    }

    /**
     * 返回数据的初步处理,由于一些比如List或者自定义的泛型类的解析稍有点麻烦,需要获取 ParameterizedType ，而在 VolleyRequestCallback 里获取更合适（目前认为这种方式更合适）
     * 就放在 Callback 里处理,这里就返回String给 VolleyRequestCallback 用来解析成具体的 JavaBean.
     * @see VolleyRequestCallback
     * @param response
     * @return
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        successListener.onResponse(response.toString());
    }

}
