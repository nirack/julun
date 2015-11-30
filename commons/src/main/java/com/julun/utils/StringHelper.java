package com.julun.utils;


import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2015-11-08.
 */
public class StringHelper {
    private static Pattern p = Pattern.compile("\\{\\d+\\}");

    /**
     * 判断字符串是否为空
     * @return boolean 空返回true 不为空返回lase
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()) || "null".equals(str)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断字符不为NULL，""，null值
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     * 从开始位置，删除指定个数的字符串
     * @return
     */
    public static String delStartChar(String src ,int len){
        if(null != src && src.length() > 0){
            src = src.substring( len );
        }
        return src;
    }
    /**
     * 从结束位置，删除指定个数的字符串
     * @param src 源字符串
     * @param len 要删除的长度
     * @return
     */
    public static String delEndChar(String src , int len){
        if(null != src && src.length() > 0){
            src = src.substring( 0 , src.length() - len );
        }
        return src;
    }

    /**
     * 将给定的params按顺序拼接起来
     * @param params 需要拼接的参数
     * @return
     */
    public static String append(Serializable ... params){
        StringBuilder sb = new StringBuilder(100);
        for (Serializable s : params) {
            sb.append( s );
        }
        return sb.toString();
    }

    /**
     * 字符串左边补0
     * @param str 字符串
     * @param num 补0后长度
     * @return
     */
    public static String leftWithZero(String str,int num){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<num-str.length();i++){
            sb.append("0");
        }
        return sb.append(str).toString();
    }

    /**
     * 是否數字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 用分隔符将字符串数组连接成字符串
     *
     * @param args
     *            字符串数组
     * @param sep
     *            分隔符
     * @return
     */
    public static final String join(String[] args, String sep) {
        StringBuilder buf = new StringBuilder(256);
        int j = args.length - 1;
        for (int i = 0; i < j; i++) {
            buf.append(args[i]).append(sep);
        }
        buf.append(args[j]);
        return buf.toString();
    }

    /**
     * 清除特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public   static   String StringFilter(String   str)   throws   PatternSyntaxException   {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

    /**
     * <p>
     * 根据参数的类型,解析含有占位符的字符串，
     * 字符类型按:'参数'的格式替换 数字类型不加任何字符直接替换
     * </p>
     *
     * @param source
     *            需要格式化的字符串
     * @param params
     *            要替换的可变参数
     * @return 格式化后的字符串
     */
    public static String format(String source, Object... params) {
        return parses(source, "'", params);
    }
    /**
     * <p>
     *  忽略参数的类型，按照给定的startChar,endChar替换字符串
     * </p>
     *
     * @param source
     *            源字符串
     * @param params
     *            根据占位符给定的参数
     * @return
     */
    public static String formatIgnoreType(String source, Object... params) {
        return parses(source,"",params);
    }

    /**
     * <p> 功能：格式化带有'{0}'占位符的字符串 </p>
     * @param source 源字符串
     * @param split 添加在目标匹配的字符串开始和结尾的字符
     * @param params 可变的参数，根据{0}的个数传入相应的参数值
     * @return 格式化后的字符串
     */
    private static String parses(String source,String split,Object... params){
        Matcher m = p.matcher(source);
        int i = 0;
        int endIndex = 0;
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            String p = "''";
            if (params[i] != null){
                if (params[i] instanceof String || params[i] instanceof Date
                        || params[i] instanceof Character)
                    p =split+params[i++].toString() + split;
                else
                    p =params[i++].toString();
                endIndex = m.end();
            }
            m.appendReplacement(buf,p);
        }
        if (endIndex > 0)buf.append(source.substring(endIndex)).toString();
        else return source;
        return buf.toString();
    }

    /**
     * 如果一个字符串为空,返回参数默认值.
     * @param target
     * @param defaults
     * @return
     */
    public static String ifEmpty(String target, String defaults) {
        return isEmpty(target) ? defaults : target;
    }

    public static boolean isHttpUrl(String url) {
        return isNotEmpty(url) && url.indexOf("http://") >-1;
    }

    public static boolean isNotHttpUrl(String url) {
        return !isHttpUrl(url);
    }
}
