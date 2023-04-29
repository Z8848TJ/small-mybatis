package com.zzh.mybatis.session;

import com.zzh.mybatis.builder.xml.XMLConfigBuilder;
import com.zzh.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @author: zzh
 * @description: 构建 SqlSessionFactory 的工厂
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
    
}
