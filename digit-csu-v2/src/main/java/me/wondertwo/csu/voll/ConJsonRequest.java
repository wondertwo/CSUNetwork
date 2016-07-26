package me.wondertwo.csu.voll;

import me.android.volley.AuthFailureError;
import me.android.volley.NetworkResponse;
import me.android.volley.ParseError;
import me.android.volley.Request;
import me.android.volley.Response;
import me.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 *
 *
 * Created by wondertwo on 2016/7/25.
 */

public class ConJsonRequest extends Request<JSONObject> {
    private Map<String, String> headers = null;
    private Map<String, String> form = null;
    private Response.Listener<JSONObject> listener;

    public ConJsonRequest(String url, Map<String, String> form,
                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        this.listener = listener;
        this.form = form;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setForm(Map<String, String> form) {
        this.form = form;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
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
