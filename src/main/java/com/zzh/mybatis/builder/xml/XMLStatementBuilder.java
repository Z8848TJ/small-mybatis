package com.zzh.mybatis.builder.xml;

import com.zzh.mybatis.builder.BaseBuilder;
import com.zzh.mybatis.builder.MapperBuilderAssistant;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.mapping.SqlCommandType;
import com.zzh.mybatis.mapping.SqlSource;
import com.zzh.mybatis.scripting.LanguageDriver;
import com.zzh.mybatis.session.Configuration;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author: zzh
 * @description:
 */
public class XMLStatementBuilder extends BaseBuilder {

    private final Logger logger = LoggerFactory.getLogger(XMLStatementBuilder.class);

    private MapperBuilderAssistant builderAssistant;

    private Element element;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, Element element) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.element = element;
    }

    //解析语句(select|insert|update|delete)
    //<select>
    //  id="selectPerson"
    //  parameterType="int"
    //  parameterMap="deprecated"
    //  resultType="hashmap"
    //  resultMap="personResultMap"
    //  flushCache="false"
    //  useCache="true"
    //  timeout="10000"
    //  fetchSize="256"
    //  statementType="PREPARED"
    //  resultSetType="FORWARD_ONLY">
    //  SELECT * FROM PERSON WHERE ID = #{id}
    //</select>
    public void parseStatementNode() {
        logger.info("开始解析 select 级别标签");

        String id = element.attributeValue("id");
        logger.info("id 方法名 ==> {}", id);
        // 参数类型
        String parameterType = element.attributeValue("parameterType");
        Class<?> parameterTypeClass = resolveAlias(parameterType);
        logger.info("参数类型 ==> {}", parameterType);
        // 外部应用 resultMap
        String resultMap = element.attributeValue("resultMap");
        
        // 结果类型
        String resultType = element.attributeValue("resultType");
        Class<?> resultTypeClass = resolveAlias(resultType);
        logger.info("结果类型 ==> {}", resultType);
        // 获取命令类型(select|insert|update|delete)
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        logger.info("标签名 ==> {}", nodeName);
        
        // 获取默认语言驱动器
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);
        logger.info("获取语言驱动器");

        // 返回静态 SQL 
        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);
        logger.info("构建 sqlSource ==> {}", sqlSource);

        // 调用助手类【本节新添加，便于统一处理参数的包装】
        builderAssistant.addMappedStatement(id,
                sqlSource,
                sqlCommandType,
                parameterTypeClass,
                resultMap,
                resultTypeClass,
                langDriver);
    }
}
