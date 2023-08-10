package com.zzh.mybatis.scripting.xmltags;

import com.zzh.mybatis.builder.BaseBuilder;
import com.zzh.mybatis.builder.SqlSourceBuilder;
import com.zzh.mybatis.mapping.SqlSource;
import com.zzh.mybatis.scripting.defaults.RawSqlSource;
import com.zzh.mybatis.session.Configuration;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 小傅哥，微信：fustack
 * @description XML脚本构建器
 * @date 2022/5/17
 * @github https://github.com/fuzhengwei/CodeDesignTutorials
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class XMLScriptBuilder extends BaseBuilder {

    private final Logger logger = LoggerFactory.getLogger(XMLScriptBuilder.class);
    
    private Element element;
    private boolean isDynamic;
    private Class<?> parameterType;

    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }

    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    List<SqlNode> parseDynamicTags(Element element) {
        List<SqlNode> contents = new ArrayList<>();
        // element.getText 拿到 SQL
        String data = element.getText();
        logger.info("原始 SQL 语句 ==> {}", data);
        
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }

}
