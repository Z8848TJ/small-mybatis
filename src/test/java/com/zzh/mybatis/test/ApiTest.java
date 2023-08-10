package com.zzh.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.zzh.mybatis.io.Resources;
import com.zzh.mybatis.session.SqlSession;
import com.zzh.mybatis.session.SqlSessionFactory;
import com.zzh.mybatis.session.SqlSessionFactoryBuilder;
import com.zzh.mybatis.test.dao.IUserDao;
import com.zzh.mybatis.test.po.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        sqlSession = sqlSessionFactory.openSession();
    }
    

    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 通过类加载器加载配置文件
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        /**
         *  通过 SAXReader 解析 mybatis 配置文件， 获取配置文件根元素
         *      解析环境配置 environments，并将其放入到 Configuration
         *      解析映射器 Mappers，获取 Mapper 文件所在位置
         *  解析 Mapper 文件，获取 SQL 相关信息
         *      将解析后的 SQL 信息放入到 Statement，将 Statement 放入 Configuration
         *      将 Mapper 配置文件与其对应的接口注册到 MapperRegistry
         *  返回 SqlSessionFactory
         */
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById(1L);
        
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

    @Test
    public void test_unpooled() throws IOException {
        // 通过类加载器加载配置文件
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        for (int i = 1; i <= 20; i++) {
            User user = userDao.queryUserInfoById(1L);
            logger.info("{}-测试结果：{}", i, JSON.toJSONString(user));
        }
        
    }
    
    
    @Test
    public void test08() throws IOException {
        // 1. 从SqlSessionFactory 中获取 SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        logger.info("sqlSessionFactory 构建完毕");
        
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
        
    }
    
    @Test
    public void test09() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：对象参数
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }
    

}
