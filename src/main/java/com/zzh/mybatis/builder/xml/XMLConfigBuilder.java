package com.zzh.mybatis.builder.xml;

import com.zzh.mybatis.builder.BaseBuilder;
import com.zzh.mybatis.datasource.DataSourceFactory;
import com.zzh.mybatis.io.Resources;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.mapping.SqlCommandType;
import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: zzh
 * @description: XML配置构建器，建造者模式，继承BaseBuilder，解析 XML 文件
 */

public class XMLConfigBuilder extends BaseBuilder {

    private final Logger logger = LoggerFactory.getLogger(XMLConfigBuilder.class);
    
    private Element root;
    
    public XMLConfigBuilder(Reader reader) {
        // 调用父类初始化 Configuration
        super(new Configuration());
        // dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
            logger.debug("mybatis 配置文件根元素 {}", root.getName());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     *
     * @return Configuration
     */
    public Configuration parse() {
        try {
            // 解析环境
            environmentsElement(root.element("environments"));
            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        
        return configuration;
    }

    /**
     *     <environments default="development">
     *         <environment id="development">
     *             <transactionManager type="JDBC"/>
     *             <dataSource type="DRUID">
     *                 <property name="driver" value="com.mysql.jdbc.Driver"/>
     *                 <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true"/>
     *                 <property name="username" value="root"/>
     *                 <property name="password" value="123456"/>
     *             </dataSource>
     *         </environment>
     *     </environments>
     */
    private void environmentsElement(Element context) throws Exception {
        logger.info("开始解析环境配置");
        String environment = context.attributeValue("default");
        logger.debug("默认环境名 ==> {}", environment);
        
        List<Element> environmentList = context.elements("environment");
        for(Element e : environmentList) {
            String id = e.attributeValue("id");
            logger.debug("数据源 id ==> {}", id);
            
            if(environment.equals(id)) {
                // 通过反射，创建事务管理器
                logger.info("解析 transactionManager 标签，创建事务管理器工厂");
                TransactionFactory txFactory = (TransactionFactory)typeAliasRegistry.
                        resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 数据源
                logger.info("解析 dataSource 标签, 创建数据源工厂");
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.
                        resolveAlias(dataSourceElement.attributeValue("type")).newInstance();

                logger.info("解析 property，获取数据库连接属性");
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for(Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }

                dataSourceFactory.setProperties(props);

                logger.info("通过数据源工厂创建数据源");
                DataSource dataSource = dataSourceFactory.getDataSource();

                logger.info("构建 environment 对象，记录事务管理器工厂和数据源");
                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                logger.info("将 environment 对象，装配到 configuration 对象");
                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    private void mapperElement(Element mappers) throws Exception {

        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            // 加载 Mapper.xml
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();
        }
        
//        List<Element> mapperList = mappers.elements("mapper");
//        for (Element e : mapperList) {
//            String resource = e.attributeValue("resource");
//            // 加载 Mapper.xml
//            Reader reader = Resources.getResourceAsReader(resource);
//
//            SAXReader saxReader = new SAXReader();
//            Document document = saxReader.read(new InputSource(reader));
//            Element root = document.getRootElement();
//
//            logger.debug("mapper 配置文件根元素 {}", root.getName());
//
//            //命名空间
//            String namespace = root.attributeValue("namespace");
//
//            logger.debug("mapper 配置文件 namespace {}", namespace);
//
//            // SELECT 标签
//            List<Element> selectNodes = root.elements("select");
//
//            for (Element node : selectNodes) {
//                String id = node.attributeValue("id");
//                String parameterType = node.attributeValue("parameterType");
//                String resultType = node.attributeValue("resultType");
//                String sql = node.getText();
//
//                logger.debug("select 标签 id：{}, parameterType：{}， resultType：{}，原始 sql：{}", 
//                        id, parameterType, resultType, sql);
//
//                // ? 匹配
//                Map<Integer, String> parameter = new HashMap<>();
//                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
//                Matcher matcher = pattern.matcher(sql);
//                for (int i = 1; matcher.find(); i++) {
//                    String g1 = matcher.group(1);
//                    String g2 = matcher.group(2);
//
//                    logger.debug("select 标签 g1：{}, g2：{}", g1, g2);
//                    parameter.put(i, g2);
//                    sql = sql.replace(g1, "?");
//                    logger.debug("解析后的 SQL {}", sql);
//                }
//                
//                // 组装全路径方法名
//                String msId = namespace + "." + id;
//                // 
//                String nodeName = node.getName();
//                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
//
//                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);
//                
//                MappedStatement mappedStatement = new MappedStatement.
//                        Builder(configuration, msId, sqlCommandType, boundSql).build();
//                
//                // 添加解析 SQL
//                configuration.addMappedStatement(mappedStatement);
//            }
//
//            // 添加 Mapper 映射器
//            configuration.addMapper(Resources.classForName(namespace));
    }

}
