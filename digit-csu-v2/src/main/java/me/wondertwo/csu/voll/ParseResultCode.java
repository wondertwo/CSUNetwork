package me.wondertwo.csu.voll;

/**
 *
 * Created by wondertwo on 2016/7/25.
 */
public class ParseResultCode {

    /*private JSONObject mResponse;

    public ParseResultCode(JSONObject response) {
        this.mResponse = response;
    }*/



    /**
     * 解析返回结果Json中 ResultCode 含义
     *
     * @param code 返回结果码
     * @return 含义
     */
    public static String parseCode(int code) {
        switch (code) {
            case 0:
                return "成功";
            case 1:
                return "其他原因认证拒绝";
            case 2:
                return "用户连接已经存在";
            case 3:
                return "接入服器务繁忙，稍后重试";
            case 4:
                return "未知错误";
            case 6:
                return "认证响应超时";
            case 7:
                return "捕获用户网络地址错误";
            case 8:
                return "服务器网络连接异常";
            case 9:
                return "认证服务脚本执行异常";
            case 10:
                return "校验码错误";
            case 11:
                return "您的密码相对简单，帐号存在被盗风险，请及时修改成强度高的密码";
            case 12:
                return "无法获取您的网络地址,请输入任意其它网站从网关处导航至本认证页面";
            case 13:
                return "无法获取您接入点设备地址，请输入任意其它网站从网关处导航至本认证页面";
            case 14:
                return "无法获取您套餐信息";
            case 16:
                return "请输入任意其它网站导航至本认证页面,并按正常PORTAL正常流程认证";
            case 17:
                return "连接已失效，请输入任意其它网站从网关处导航至本认证页面";
            default:
                return "未知错误";
        }
    }
}
