package com.zzh.mybatis.session;

import com.zzh.mybatis.binding.MapperRegistry;
import com.zzh.mybatis.datasource.druid.DruidSourceFactory;
import com.zzh.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.zzh.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.zzh.mybatis.executor.Executor;
import com.zzh.mybatis.executor.SimpleExecutor;
import com.zzh.mybatis.executor.parameter.ParameterHandler;
import com.zzh.mybatis.executor.resultset.DefaultResultSetHandler;
import com.zzh.mybatis.executor.resultset.ResultSetHandler;
import com.zzh.mybatis.executor.statement.PreparedStatementHandler;
import com.zzh.mybatis.executor.statement.StatementHandler;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.reflection.MetaObject;
import com.zzh.mybatis.reflection.factory.DefaultObjectFactory;
import com.zzh.mybatis.reflection.factory.ObjectFactory;
import com.zzh.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.zzh.mybatis.reflection.wrapper.ObjectWrapperFactory;
import com.zzh.mybatis.scripting.LanguageDriver;
import com.zzh.mybatis.scripting.LanguageDriverRegistry;
import com.zzh.mybatis.scripting.xmltags.XMLLanguageDriver;
import com.zzh.mybatis.transaction.Transaction;
import com.zzh.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.zzh.mybatis.type.TypeAliasRegistry;
import com.zzh.mybatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    // 类型处理器注册机
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    protected final Set<String> loadedResources = new HashSet<>();

    protected String databaseId;
    
    public Configuration() {
        logger.debug("初始化 Configuration");
        
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        logger.debug("将 JDBC 事务管理器工厂注册到类型别名注册器");
        
        typeAliasRegistry.registerAlias("DRUID", DruidSourceFactory.class);
        logger.debug("将 Druid 数据源工厂注册到类型别名注册器");

        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        logger.debug("将 无池化 数据源工厂注册到类型别名注册器");
        
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        logger.debug("将 有池化 数据源工厂注册到类型别名注册器");

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
        logger.info("将 默认语言驱动器 注册语言注册器");
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
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

    public String getDatabaseId() {
        return databaseId;
    }
    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, resultHandler, rowBounds, boundSql);
    }

    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容 interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    // 类型处理器注册机
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }
}
