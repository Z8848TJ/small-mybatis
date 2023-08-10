package com.zzh.mybatis.scripting.xmltags;

import com.zzh.mybatis.executor.parameter.ParameterHandler;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.mapping.SqlSource;
import com.zzh.mybatis.scripting.LanguageDriver;
import com.zzh.mybatis.scripting.defaults.DefaultParameterHandler;
import com.zzh.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @author 小傅哥，微信：fustack
 * @description XML语言驱动器
 * @date 2022/5/17
 * @github https://github.com/fuzhengwei/CodeDesignTutorials
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

}