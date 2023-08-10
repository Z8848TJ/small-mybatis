package com.zzh.mybatis.scripting.defaults;

import com.zzh.mybatis.builder.SqlSourceBuilder;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.SqlSource;
import com.zzh.mybatis.scripting.xmltags.DynamicContext;
import com.zzh.mybatis.scripting.xmltags.SqlNode;
import com.zzh.mybatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author: zzh
 * @description: XML配置构建器，建造者模式，继承BaseBuilder，解析 XML 文件
 */
public class RawSqlSource implements SqlSource {

    private final Logger logger = LoggerFactory.getLogger(RawSqlSource.class);

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        
        logger.info("参数类型 ==> {}", parameterType);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

}
