package me.wondertwo.csu.net;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by wondertwo on 2016/7/25.
 */
public class ParseResult {

    /**
     * 把网路请求结果，解析成 JSONObject
     *
     * @param result
     * @return
     */
    public static JSONObject getResponseEntity(NetRequestResult result) {
        JSONObject entity = null;
        if (!TextUtils.isEmpty((String) result.getArgs()[0])) {
            try {
                entity =  new JSONObject((String) result.getArgs()[0]);
                Log.e("get response entity", entity.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public static String getResultDesc(JSONObject entity) {
        try {
            return entity.get("resultDescribe").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析登录结果Json中 ResultCode 含义
     *
     * @param code 返回结果码
     * @return 含义
     */
    public static String parseLoginCode(int code) {
        switch (code) {
            case -1: // 响应状态码不包含-1，我们用-1表示网络无响应
                return "网络无响应";
            case 0:
                return "登录成功";
            case 1:
                return "当前账号已在线";
            case 2:
                return "其他原因认证拒绝";
            case 3:
                return "接入服器务繁忙，稍后重试";
            case 4:
                return "未知错误";
            case 5:
                return "认证响应超时";
            case 6:
                return "捕获用户网络地址错误";
            case 7:
                return "服务器网络连接异常";
            case 8:
                return "认证服务脚本执行异常";
            case 9:
                return "校验码错误";
            case 10:
                return "您的密码相对简单，帐号存在被盗风险，请及时修改成强度高的密码";
            case 11:
                return "无法获取您的网络地址,请输入任意其它网站从网关处导航至本认证页面";
            case 12:
                return "无法获取您接入点设备地址，请输入任意其它网站从网关处导航至本认证页面";
            case 13:
                return "无法获取您套餐信息";
            case 14:
                return "请输入任意其它网站导航至本认证页面,并按正常PORTAL正常流程认证";
            case 15:
                return "连接已失效，请输入任意其它网站从网关处导航至本认证页面";
            default:
                return "未知错误，登录失败";
        }
    }
    /*<string-array name="login_tip">
        <item>登陆成功</item>
        <item>其他原因认证拒绝</item>
        <item>用户连接已经存在</item>
        <item>接入服器务繁忙，稍后重试</item>
        <item>未知错误</item>
        <item>认证响应超时</item>
        <item>捕获用户网络地址错误</item>
        <item>服务器网络连接异常</item>
        <item>认证服务脚本执行异常</item>
        <item>校验码错误</item>
        <item>您的密码相对简单，帐号存在被盗风险，请及时修改成强度高的密码</item>
        <item>无法获取您的网络地址,请确定您连接了数字中南的路由</item>
        <item>无法获取您接入点设备地址,请确定您连接了数字中南的路由</item>
        <item>无法获取您套餐信息</item>
        <item>未知原因，登录失败</item>
    </string-array>

    <string-array name="logout_tip">
        <item>下线成功</item>
        <item>服务器拒绝请求</item>
        <item>下线请求执行失败</item>
        <item>您已经下线</item>
        <item>服务器响应超时</item>
        <item>后台网络连接异常</item>
        <item>服务脚本执行异常</item>
        <item>未知错误</item>
    </string-array>*/

    /**
     * 解析下线结果Json中 ResultCode 含义
     *
     * @param code 返回结果码
     * @return 含义
     */
    public static String parseLogoutCode(int code) {
        switch (code) {
            case -1: // 响应状态码不包含-1，我们用-1表示网络无响应
                return "网络无响应";
            case 0:
                return "下线成功";
            case 1:
                return "服务器拒绝请求";
            case 2:
                return "下线请求执行失败";
            case 3:
                return "您已经下线";
            case 4:
                return "服务器响应超时";
            case 5:
                return "后台网络连接异常";
            case 6:
                return "服务脚本执行异常";
            case 7:
                return "未知错误";
            default:
                return "未知错误，操作失败";
        }
    }
}
