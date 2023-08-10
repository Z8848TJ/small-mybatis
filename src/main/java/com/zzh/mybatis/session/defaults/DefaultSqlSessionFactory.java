package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.builder.xml.XMLConfigBuilder;
import com.zzh.mybatis.executor.Executor;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.session.SqlSession;
import com.zzh.mybatis.session.SqlSessionFactory;
import com.zzh.mybatis.session.TransactionIsolationLevel;
import com.zzh.mybatis.transaction.Transaction;
import com.zzh.mybatis.transaction.TransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author: zzh
 * @description: 默认的 DefaultSqlSessionFactory
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Logger logger = LoggerFactory.getLogger(DefaultSqlSessionFactory.class);

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        logger.info("开启 SQL Session");
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            logger.info("创建执行器");
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            logger.info("创建 DefaultSqlSession");
            // 创建DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
