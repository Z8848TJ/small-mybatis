package com.zzh.mybatis.mapping;


import com.zzh.mybatis.scripting.LanguageDriver;
import com.zzh.mybatis.session.Configuration;

import java.util.List;

/**
 * @author: zzh
 * @description: 映射语句类
 */
public class MappedStatement {
    
    private Configuration configuration;
    private String id;
    private SqlCommandType sqlCommandType;

    private SqlSource sqlSource;

    private LanguageDriver lang;
    Class<?> resultType;

    private List<ResultMap> resultMaps;
    

    private MappedStatement() {}

    public LanguageDriver getLang() {
        return lang;
    }

    public List<ResultMap> getResultMaps() {
        return resultMaps;
    }

    public static class Builder {

        private final MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

        public String id() {
            return mappedStatement.id;
        }

        public Builder resultMaps(List<ResultMap> resultMaps) {
            mappedStatement.resultMaps = resultMaps;
            return this;
        }

    }


    public Configuration getConfiguration() {
        return configuration;
    }


    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }
   

    public Class<?> getResultType() {
        return resultType;
    }
    
}
