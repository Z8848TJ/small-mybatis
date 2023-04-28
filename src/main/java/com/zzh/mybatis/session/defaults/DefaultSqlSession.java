package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.binding.MapperRegistry;
import com.zzh.mybatis.session.SqlSession;

/**
 * @author: zzh
 * @description: 默认SqlSession实现类
 */
public class DefaultSqlSession implements SqlSession {

    /**
     * 映射器注册机
     */
    private final MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你被代理了！" + "方法：" + statement + " 入参：" + parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
