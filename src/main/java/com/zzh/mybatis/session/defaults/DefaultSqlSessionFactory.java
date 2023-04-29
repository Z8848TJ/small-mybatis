package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.session.SqlSession;
import com.zzh.mybatis.session.SqlSessionFactory;

/**
 * @author: zzh
 * @description: 默认的 DefaultSqlSessionFactory
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
