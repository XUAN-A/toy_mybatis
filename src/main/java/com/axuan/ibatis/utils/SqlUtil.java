package com.axuan.ibatis.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/13 14:59
 */
public class SqlUtil {

    public static String paramToSql(String sql, Object[] parameter) throws Exception {
        int paramCount = StringUtil.getTargetStringNum(sql, "#");

        if (paramCount < 1) { // 没有参数
            return sql;
        } else if (paramCount == 1) { // 只有一个参数，即只有一个基本数据类型，转换为字符串
            String regex = "#\\{([^}])*}";
            // 将sql 语句中的 #{*}替换为?
            return sql.replaceAll(regex, "\"" + parameter[0] + "" + "\"");
        } else if (paramCount > 1 && parameter.length == 1) { // 多个参数，且传实体对象
            Class<?> obj = parameter[0].getClass();
            Field[] declaredFields = obj.getDeclaredFields();
            for (Field field : declaredFields) {
                String fieldName = field.getName();
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, obj);
                Method readMethod = propertyDescriptor.getReadMethod();
                Object o = readMethod.invoke(parameter[0]);
                if (o != null) {
                    sql = sql.replace("#{" + fieldName + "}","\"" + o + "" + "\"");
                } else {
                    sql = sql.replace("#{" + fieldName + "}", "null");
                }
            }
            return sql;
        } else {
            return sql;
        }
    }
}
