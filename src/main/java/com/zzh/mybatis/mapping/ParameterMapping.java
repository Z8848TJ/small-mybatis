package com.zzh.mybatis.mapping;

import cn.hutool.db.meta.JdbcType;
import com.zzh.mybatis.session.Configuration;

/**
 * @author: zzh
 * @description: 参数映射 #{property,javaType=int,jdbcType=NUMERIC}
 */ 
public class ParameterMapping {

    private Configuration configuration;

    // property
    private String property;
    
    // javaType = int
    private Class<?> javaType = Object.class;
    
    // jdbcType=NUMERIC
    private JdbcType jdbcType;

    private ParameterMapping() {}

    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }
}
