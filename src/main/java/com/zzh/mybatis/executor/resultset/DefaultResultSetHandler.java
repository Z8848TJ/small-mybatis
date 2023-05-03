package com.zzh.mybatis.executor.resultset;

import com.zzh.mybatis.executor.Executor;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: zzh
 * @description: 默认 Map 结果处理器
 */
public class DefaultResultSetHandler implements ResultSetHandler{

    private final BoundSql boundSql;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        this.boundSql = boundSql;
    }


    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        ResultSet resultSet = stmt.getResultSet();
        try {
            return resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 遍历结果集数据
            while (resultSet.next()) {
                T obj = (T)clazz.newInstance();
                // 遍历行数据，按照列的编号遍历
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    // 组装对象的 set 方法名
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    // 执行 set 方法
                    method.invoke(obj, value);
                }

                list.add(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}