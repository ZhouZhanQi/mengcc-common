package com.mengcc.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据校验工具类
 *
 * @author ZhouZQ
 * @create 2019/3/21
 */
public class ValidateUtils {

    private ValidateUtils() {
    }

    /**
     * 判断字符串是否和正则表达式相匹配,大小写敏感
     *
     * @param str   字符串
     * @param regEx 正则表达式
     * @return 判断结果\
     */
    public static boolean isMatch(final String str, final String regEx) {
        return isMatch(str, regEx, false);
    }

    /**
     * 判断是否为IP字符串
     *
     * @param ipStr ip字符串
     * @return 判断结果
     */
    public static boolean isIP(final String ipStr) {
        return isMatch(ipStr, "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
    }


    /**
     * 验证手机号码正确性
     *
     * @param strMobilePhone 手机号码
     * @return
     */
    public static boolean isMobilePhoneNo(final String strMobilePhone) {
        return isMatch(strMobilePhone, "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
    }


    /**
     * 验证会员商家密码
     * @param memberPassword
     * @return
     */
    public static boolean isMemberPassword(String memberPassword) {
        return isMatch(memberPassword, "^[a-zA-Z0-9]{6,16}$");
    }

    /**
     * 判断是否是Email地址字符串
     *
     * @param strEmail URL地址字符串
     * @return 判断结果
     */
    public static boolean isEmail(final String strEmail) {
        return isMatch(strEmail, "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    }

    /**
     * 判断字符串是否是身份证号码
     * @param str
     * @return
     */
    public static boolean isIdCard(final String str){
        return IdCardUtil.isValidatedAllIdcard(str);
    }

    /**
     * 判断是否是Base64字符串
     *
     * @param str Base64字符串
     * @return 判断结果
     */
    public static boolean isBase64(final String str) {
        return isMatch(str, "[A-Za-z0-9\\+\\/\\=]");
    }

    /**
     * 判断字符串是否和正则表达式相匹配
     *
     * @param str             字符串
     * @param regEx           正则表达式
     * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
     * @return 判断结果
     */
    public static boolean isMatch(final String str, final String regEx, final boolean caseInsensetive) {

        if (!StringUtils.isAnyBlank(str, regEx)) {
            Pattern pattern = null;
            if (caseInsensetive) {
                pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            } else {
                pattern = Pattern.compile(regEx);
            }
            final Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
        return false;
    }
    /**
     * Description:判断短信验证码是数据
     * @param
     * @return:boolean
     * Author:周涛
     * Date Created in 2019/6/25 9:22
     */
    public static boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
