package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: zzh
 * @description: 默认 SqlSession 实现类
 */
public class DefaultSqlSession implements SqlSession {

    private final Logger logger = LoggerFactory.getLogger(MappedStatement.class);

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        logger.debug("执行代理 ==> 方法名: {}, 参数: {}", statement, parameter);
        try{
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            Environment environment = configuration.getEnvironment();
            Connection connection = environment.getDataSource().getConnection();
            
            BoundSql boundSql = mappedStatement.getBoundSql();

            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> objList = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
            return objList.get(0);
        } catch (Exception e) {
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

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
