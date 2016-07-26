package me.wondertwo.csu.voll;

import me.android.volley.AuthFailureError;
import me.android.volley.NetworkResponse;
import me.android.volley.Response;
import me.android.volley.toolbox.HttpHeaderParser;
import me.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *
 *
 * Created by wondertwo on 2016/7/25.
 */

public class ConStringRequest extends StringRequest {
    private Map<String, String> headers = null;
    private Map<String, String> form = null;
    private boolean ifGetCookie;

    public ConStringRequest(int method, String url,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.ifGetCookie = false;
    }

    public ConStringRequest(int method, String url, boolean ifGetCookie,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.ifGetCookie = ifGetCookie;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setForm(Map<String, String> form) {
        this.form = form;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        String cookie = "";
        if (ifGetCookie)
            cookie = "[" + response.headers.get("Set-Cookie") + "]";
        return Response.success(cookie + parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null)
            return super.getHeaders();
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (form == null)
            return super.getParams();
        return form;
    }
}
