package org.shrek.hadata.service.qimen.common;

import com.google.common.collect.Maps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 17:32
 */
public class QimenResponseUtil {

    public static <T> T success( Class<T> clazz){
        return createQimenResponse("success","0","处理成功!","", Maps.newHashMap(),clazz);
    }


    public static <T> T success(HashMap<String, String> extendMap, Class<T> clazz){
        return createQimenResponse("success","0","处理成功!","",extendMap,clazz);
    }

    public static <T> T success( String body,HashMap<String, String> extendMap, Class<T> clazz){
        return createQimenResponse("success","0","处理成功!",body,extendMap,clazz);
    }


    public static <T> T fail(HashMap<String, String> extendMap, Class<T> clazz){
        return createQimenResponse("failure","50","处理失败!","",extendMap,clazz);
    }

    public static <T> T fail(String code, String message,HashMap<String, String> extendMap, Class<T> clazz){
        return createQimenResponse("failure","50","处理失败!","",extendMap,clazz);
    }

    public static <T> T fail(String message,Class<T> clazz){
        return createQimenResponse("failure","50",message,"",Maps.newHashMap(),clazz);
    }

    public static <T> T fail(String message, HashMap<String, String> extendMap, Class<T> clazz){
        return createQimenResponse("failure","50",message,"",extendMap,clazz);
    }

    private static <T> T createQimenResponse(String flag, String code, String message, String body, HashMap<String, String> extendMap, Class<T> clazz) {
        T response = null;
        try {
            response = clazz.newInstance();
            Method method1 = clazz.getMethod("setFlag", String.class);
            method1.invoke(response, flag);
            Method method2 = clazz.getMethod("setCode", String.class);
            method2.invoke(response, code);
            Method method3 = clazz.getMethod("setMessage", String.class);
            method3.invoke(response, message);
            Method method4 = clazz.getMethod("setBody", String.class);
            method4.invoke(response, body);

            Iterator iter = extendMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) iter.next();
                String key = entry.getKey();
                String val = entry.getValue();
                Method method = clazz.getDeclaredMethod(key, String.class);
                method4.invoke(response, val);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return response;
    }
}
