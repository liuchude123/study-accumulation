package com.rich.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchude lWX433784 on 2017/2/20.
 */
public class ConvertUtils {

    private static volatile ConvertUtils uniqueInstance = new ConvertUtils();

    private ConvertUtils() {
    }

    /**
     * 线程安全的单例模式
     */
    private ConvertUtils getUniqueInstance() {
        // 线程安全的单例模式
        if (uniqueInstance == null) {
            // 只有第一次才彻底执行这里的代码
            synchronized (this) {
                // 再检查一次
                if (uniqueInstance == null)
                    uniqueInstance = new ConvertUtils();
            }
        }
        return uniqueInstance;
    }

    public static <I, O> List<O> voToDto(List<I> voList) {
        return uniqueInstance.convertListAToListB(voList, "vo", "dto");
    }

    public static <I, O> List<O> dtoToPo(List<I> dtoList) {
        return uniqueInstance.convertListAToListB(dtoList, "dto", "po");
    }

    public static <I, O> List<O> poToDto(List<I> poList) {
        return uniqueInstance.convertListAToListB(poList, "po", "dto");
    }

    public static <I, O> List<O> dtoToVo(List<I> dtoList) {
        return uniqueInstance.convertListAToListB(dtoList, "dto", "vo");
    }

    public static <I, O> O voToDto(I vo) {
        return uniqueInstance.convertAToB(vo, "vo", "dto");
    }

    public static <I, O> O dtoToPo(I dto) {
        return uniqueInstance.convertAToB(dto, "dto", "po");
    }

    public static <I, O> O poToDto(I po) {
        return uniqueInstance.convertAToB(po, "po", "dto");
    }

    public static <I, O> O dtoToVo(I dto) {
        return uniqueInstance.convertAToB(dto, "dto", "vo");
    }

    private <I, O> O convertAToB(I in, String aSuffix, String bSuffix) {
        if (in == null) {
            return null;
        }
        // 返回结果
        O o = null;
        try {
            // 参数泛型Class
            Class inClazz = in.getClass();
            // 参数类变量集合
            Field[] inFields = inClazz.getDeclaredFields();
            String packageName = "com.rich.entity." + bSuffix.toLowerCase();
            String className = ".";
            if ("po".equals(bSuffix.toLowerCase())) {
                className += inClazz.getSimpleName().replace(aSuffix.toUpperCase(), "");
            } else {
                className += inClazz.getSimpleName().replace(aSuffix.toUpperCase(), "") + bSuffix.toUpperCase();
            }
            // 返回列表类泛型Class
            Class outClazz = Class.forName(packageName + className);
            // 返回列表对象
            Object outObj = outClazz.newInstance();
            for (Field inField : inFields) {
                // 参数类变量名称
                String variable = inField.getName();
                // 参数类变量类型
                Class variableType = inField.getType();
                variable = variable.substring(0, 1).toUpperCase() + variable.substring(1);

                // 参数类变量get方法名
                String getMethodName = "get" + variable;
                Object value;
                if ("page,rows,total".contains(variable.toLowerCase())) {
                    // 获取getter方法
                    Method getMethod = inClazz.getSuperclass().getDeclaredMethod(getMethodName);
                    // 通过参数对象getter方法获取变量值
                    value = getMethod.invoke(in, null);
                } else {
                    // 获取getter方法
                    Method getMethod = inClazz.getDeclaredMethod(getMethodName);
                    // 通过参数对象getter方法获取变量值
                    value = getMethod.invoke(in, null);
                }

                // 返回对象set方法名
                String setMethodName = "set" + variable;
                if ("page,rows,total".contains(variable.toLowerCase())) {
                    // 获取setter方法
                    Method setMethod = outClazz.getSuperclass().getDeclaredMethod(setMethodName, variableType);
                    // 通过返回对象setter方法设置返回对象变量值
                    setMethod.invoke(outObj, value);
                } else {
                    // 获取setter方法
                    Method setMethod = outClazz.getDeclaredMethod(setMethodName, variableType);
                    // 通过返回对象setter方法设置返回对象变量值
                    setMethod.invoke(outObj, value);
                }
            }
            o = (O) outObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    private <I, O> List<O> convertListAToListB(List<I> inList, String aSuffix, String bSuffix) {
        if (inList == null || inList.size() == 0) {
            return null;
        }
        // 返回结果
        List<O> dtoList = new ArrayList<>();
        try {
            // 遍历参数列表
            for (Object inObj : inList) {
                // 参数泛型Class
                Class inClazz = inObj.getClass();
                // 参数类变量集合
                Field[] inFields = inClazz.getDeclaredFields();
                String packageName = "com.rich.entity." + bSuffix.toLowerCase();
                String className = ".";
                if ("po".equals(bSuffix.toLowerCase())) {
                    className += inClazz.getSimpleName().replace(aSuffix.toUpperCase(), "");
                } else {
                    className += inClazz.getSimpleName().replace(aSuffix.toUpperCase(), "") + bSuffix.toUpperCase();
                }
                // 返回列表类泛型Class
                Class outClazz = Class.forName(packageName + className);
                // 返回列表对象
                Object outObj = outClazz.newInstance();
                for (Field inField : inFields) {
                    // 参数类变量名称
                    String variable = inField.getName();
                    // 参数类变量类型
                    Class variableType = inField.getType();
                    variable = variable.substring(0, 1).toUpperCase() + variable.substring(1);

                    // 参数类变量get方法名
                    String getMethodName = "get" + variable;
                    Object value;
                    if ("page,rows,total".contains(variable.toLowerCase())) {
                        // 获取getter方法
                        Method getMethod = inClazz.getSuperclass().getDeclaredMethod(getMethodName);
                        // 通过参数对象getter方法获取变量值
                        value = getMethod.invoke(inObj, null);
                    } else {
                        // 获取getter方法
                        Method getMethod = inClazz.getDeclaredMethod(getMethodName);
                        // 通过参数对象getter方法获取变量值
                        value = getMethod.invoke(inObj, null);
                    }

                    // 返回对象set方法名
                    String setMethodName = "set" + variable;
                    if ("page,rows,total".contains(variable.toLowerCase())) {
                        // 获取setter方法
                        Method setMethod = outClazz.getSuperclass().getDeclaredMethod(setMethodName, variableType);
                        // 通过返回对象setter方法设置返回对象变量值
                        setMethod.invoke(outObj, value);
                    } else {
                        // 获取setter方法
                        Method setMethod = outClazz.getDeclaredMethod(setMethodName, variableType);
                        // 通过返回对象setter方法设置返回对象变量值
                        setMethod.invoke(outObj, value);
                    }
                }
                dtoList.add((O) outObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtoList;
    }

}
