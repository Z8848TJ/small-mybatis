package com.zzh.mybatis.builder.xml;

import com.zzh.mybatis.builder.BaseBuilder;
import com.zzh.mybatis.builder.MapperBuilderAssistant;
import com.zzh.mybatis.io.Resources;
import com.zzh.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * @author: zzh
 * @description: XML映射构建器
 */

public class XMLMapperBuilder extends BaseBuilder {

    private final Logger logger = LoggerFactory.getLogger(XMLMapperBuilder.class);
    private Element element;
    // 映射器构建助手
    private MapperBuilderAssistant builderAssistant;
    private String resource;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    private XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.element = document.getRootElement();
        this.resource = resource;
    }

    /**
     * 解析
     */
    public void parse() throws Exception {
        // 如果当前资源没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            logger.info("开始解析 mapper 映射文件");
            // 解析 mapper 映射文件
            configurationElement(element);
            // 标记一下，已经加载过了
            configuration.addLoadedResource(resource);
            // 绑定映射器到 namespace
            configuration.addMapper(Resources.classForName(builderAssistant.getCurrentNamespace()));
        }
    }

    // 配置mapper元素
    // <mapper namespace="org.mybatis.example.BlogMapper">
    //   <select id="selectBlog" parameterType="int" resultType="Blog">
    //    select * from Blog where id = #{id}
    //   </select>
    // </mapper>
    private void configurationElement(Element element) {
        // 1. 命名空间
        String namespace = element.attributeValue("namespace");
        logger.info("namespace ==> {}", namespace);
        if (namespace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }

        builderAssistant.setCurrentNamespace(namespace);

        // 2.配置select|insert|update|delete
        buildStatementFromContext(element.elements("select"));
    }

    // 配置select|insert|update|delete 解析 select 级别的标签
    private void buildStatementFromContext(List<Element> list) {
        for (Element element : list) {
            final XMLStatementBuilder statementParser = 
                    new XMLStatementBuilder(configuration, builderAssistant, element);
            statementParser.parseStatementNode();
        }
    }
    
}
