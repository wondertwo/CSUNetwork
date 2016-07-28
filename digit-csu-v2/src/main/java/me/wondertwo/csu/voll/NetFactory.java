package me.wondertwo.csu.voll;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigInteger;

import me.android.volley.DefaultRetryPolicy;
import me.android.volley.Request;
import me.android.volley.RequestQueue;
import me.android.volley.Response;
import me.android.volley.VolleyError;
import me.android.volley.toolbox.StringRequest;
import me.android.volley.toolbox.Volley;
import me.wondertwo.csu.app.MainApplication;
import me.wondertwo.csu.net.NetworkConstant;

/**
 *
 * Created by wondertwo on 2016/7/25.
 */
public class NetFactory {

    private static NetFactory mNetFactory;
    private Context mContext;
    private RequestQueue mVolleyQueue;
    private final String TAG_REQUEST = "DO_LOG_IN_REQUEST";
    private JSONObject mLoginResponse;

    private final String LOGIN_POST_URL = "http://61.137.86.87:8080/portalNat444/AccessServices/login";
    private final String LOGIN_PEFER = "http://61.137.86.87:8080/portalNat444/index.jsp";
    private final String LOGOUT_POST_URL = "http://61.137.86.87:8080/portalNat444/AccessServices/logout?";
    private final String LOGOUT_PEFER = "http://61.137.86.87:8080/portalNat444/main2.jsp";

    private NetFactory(Context context) {
        this.mContext = context;
        mVolleyQueue = Volley.newRequestQueue(context);
    }

    public static NetFactory getInstance(Context context) {
        if (mNetFactory == null) {
            mNetFactory = new NetFactory(context);
        }
        return mNetFactory;
    }

    public JSONObject getmLoginResponse() {
        return mLoginResponse;
    }

    public void setmLoginResponse(JSONObject mLoginResponse) {
        this.mLoginResponse = mLoginResponse;
    }

    /**
     * 登陆，分两步:
     *
     * 1. 尝试加载一个网址来连接数字中南登陆页面，获取页面中brasAddress和userIntranetAddress两个值；
     * 2. 将加密的密码和账号连同第一步获取的两个值一起post给数字中南登陆页，实现登陆。
     */
    public JSONObject doLogin(String id, String password) {
        return doLoginWithEncryptedPassword(id, encrytePassword(password));
    }

    /**
     * 先加密 password，再登陆
     */
    public JSONObject doLoginWithEncryptedPassword(String id, String password) {
        KeyValuePairs post_datas = getAddress();
        post_datas.add("accountID", id + "@zndx.inter")
                .add("password", password);
        KeyValuePairs post_header = KeyValuePairs.create()
                .add("Referer", LOGIN_PEFER);
        final JSONObject[] result = new JSONObject[1]; //请求结果response
        ConJsonRequest request = new ConJsonRequest(LOGIN_POST_URL, post_datas.build(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        result[0] = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        /**
         * Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
         * Volley doe retry for you if you have specified the policy.
         */
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG_REQUEST);
        request.setHeaders(post_header.build());
        mVolleyQueue.add(request);
        Log.e("================", String.valueOf(result[0]));
        return result[0];
    }


    /**
     * 密码需要经过RSA加密
     */
    public String encrytePassword(String passwordInStr) {
        String mudulusInHexStr = "a8a02b821d52d3d0ca90620c78474b78435423be99da83cc190ab5cb5b9b922a4c8ba6b251e78429757cf11cde119e1eacff46fa3bf3b43ef68ceb29897b7aa6b5b1359fef6f35f32b748dc109fd3d09f3443a2cc3b73e99579f3d0fe6a96ccf6a48bc40056a6cac327d309b93b1d61d6f6e8f4a42fc9540f34f1c4a2e053445";
        String exponentInHexStr = "10001";
        String result;
        BigInteger bigInt_mudulus = new BigInteger(mudulusInHexStr, 16);
        BigInteger bigInt_exponent = new BigInteger(exponentInHexStr, 16);
        BigInteger bigInt_password = new BigInteger(new StringBuilder(passwordInStr).reverse().toString().getBytes());
        BigInteger bigInt_result = bigInt_password.modPow(bigInt_exponent, bigInt_mudulus);
        result = bigInt_result.toString(16);
        String zeroStr = "";
        int zeroNum = mudulusInHexStr.length() - result.length();
        for (int i = 0; i < zeroNum; i++) {
            zeroStr += "0";
        }
        return zeroStr + result;
    }

    /**
     * 获取 userIntranetAddress 和 brasAddress
     *
     * @return
     */
    public KeyValuePairs getAddress() {
        final KeyValuePairs addresses = KeyValuePairs.create();
        String url = "http://www.baidu.com";
        final StringRequest request = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Document doc = Jsoup.parse(response);
                            String userIntranetAddress = doc.getElementById("userIntranetAddress").val().toString();
                            String brasAddress = doc.getElementById("brasAddress").val().toString();
                            addresses.add("userIntranetAddress", userIntranetAddress);
                            addresses.add("brasAddress", brasAddress);
                            MainApplication.getSpUtil().setValue(NetworkConstant.SP_USER_BRAS_ADDRESS, brasAddress);
                            MainApplication.getSpUtil().setValue(NetworkConstant.SP_USER_INTRANET_ADDRESS, userIntranetAddress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*if( error instanceof NetworkError) {
                        } else if( error instanceof ServerError) {
                        } else if( error instanceof AuthFailureError) {
                        } else if( error instanceof ParseError) {
                        } else if( error instanceof NoConnectionError) {
                        } else if( error instanceof TimeoutError) {
                        }*/
                        error.printStackTrace();
                    }
                });
        mVolleyQueue.add(request);
        return addresses;
    }
}
