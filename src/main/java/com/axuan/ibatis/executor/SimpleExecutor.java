package com.axuan.ibatis.executor;

import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.utils.SqlUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单执行器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 15:49
 */
public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) throws SQLException {
        super(configuration, transaction);
    }



    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String originalSql = ms.getSql();
        Object[] parameters = (Object[]) parameter;
        Class<?> pojoClass = ms.getResultType();
        Connection conn = getConnection(null);
        String resultSql = null;
        try  {
            resultSql  = SqlUtil.paramToSql(originalSql, parameters);
            preparedStatement = conn.prepareStatement(resultSql);
            resultSet = preparedStatement.executeQuery();
            List<E> list = new ArrayList<>();
            while (resultSet.next()) {
                E pojo = (E)pojoClass.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(columnName);

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, pojoClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(pojo, value);
                }
                list.add(pojo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        PreparedStatement preparedStatement = null;
        // xml里的sql字符串
        String originalSql = ms.getSql();
        String resultSql = null;
        Class<?> parameterType = ms.getParameterType();
        Object[] parameters = (Object[]) parameter;
        //Connection conn = conn(null);
        try {
            resultSql = SqlUtil.paramToSql(originalSql, parameters);
            preparedStatement = conn.prepareStatement(resultSql);
            return preparedStatement.executeUpdate();
        } catch (Exception e)  {
            throw new SQLException("update时sql执行或注入实体类错误", e);
         } finally {
            closeStatement(preparedStatement);
        }
    }


}
