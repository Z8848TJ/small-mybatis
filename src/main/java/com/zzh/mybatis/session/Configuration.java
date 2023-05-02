package com.zzh.mybatis.session;

import com.zzh.mybatis.binding.MapperRegistry;
import com.zzh.mybatis.datasource.druid.DruidSourceFactory;
import com.zzh.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.zzh.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.zzh.mybatis.type.TypeAliasRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zzh
 * @description: 配置项
 * 
 * 整个 Mybatis 的操作都是使用 Configuration 配置项进行串联流程，
 * 所以所有内容都会在 Configuration 中进行链接
 */
public class Configuration {

    private final Logger logger = LoggerFactory.getLogger(Configuration.class);
    
    protected Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();
    
    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    
    public Configuration() {
        logger.debug("初始化 Configuration");
        
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        logger.debug("将 JDBC 事务工厂注册到类型别名注册器");
        
        typeAliasRegistry.registerAlias("DRUID", DruidSourceFactory.class);
        logger.debug("将 Druid 数据源工厂注册到类型别名注册器");

        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        logger.debug("将 无池化 数据源工厂注册到类型别名注册器");
        
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        logger.debug("将 有池化 数据源工厂注册到类型别名注册器");
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }
    
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
