package com.zzh.mybatis.scripting;

import com.zzh.mybatis.executor.parameter.ParameterHandler;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.mapping.SqlSource;
import com.zzh.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @author 小傅哥，微信：fustack
 * @description 脚本语言驱动
 * @date 2022/5/17
 * @github https://github.com/fuzhengwei/CodeDesignTutorials
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
}
