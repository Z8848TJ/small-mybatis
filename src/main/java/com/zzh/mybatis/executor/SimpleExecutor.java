package com.zzh.mybatis.executor;

import com.zzh.mybatis.executor.statement.StatementHandler;
import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.session.ResultHandler;
import com.zzh.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author: zzh
 * @description: 简单执行器
 */
public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            // 创建 StatementHandler，内部对 ResultHandler 进行了初始化
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
            // 创建连接
            Connection connection = transaction.getConnection();
            // 根据 connection 创建 statement
            Statement stmt = handler.prepare(connection);
            // 填充参数
            handler.parameterize(stmt);
            // 执行查询
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
