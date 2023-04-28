package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.binding.MapperRegistry;
import com.zzh.mybatis.session.SqlSession;
import com.zzh.mybatis.session.SqlSessionFactory;

/**
 * @author: zzh
 * @description: 默认的 DefaultSqlSessionFactory
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }
    
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
