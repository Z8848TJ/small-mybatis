package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.executor.Executor;
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

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

//    @Override
//    public <T> T selectOne(String statement, Object parameter) {
//        logger.debug("执行代理 ==> 方法名: {}, 参数: {}", statement, parameter);
//        try{
//            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
//            Environment environment = configuration.getEnvironment();
//            Connection connection = environment.getDataSource().getConnection();
//            
//            BoundSql boundSql = mappedStatement.getBoundSql();
//
//            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
//            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            List<T> objList = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
//            return objList.get(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        
//    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        logger.debug("执行代理 ==> 方法名: {}, 参数: {}", statement, parameter);
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        List<T> list = executor.query(mappedStatement, parameter,
                Executor.NO_RESULT_HANDLER, mappedStatement.getBoundSql());
        
        return list.get(0);
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
