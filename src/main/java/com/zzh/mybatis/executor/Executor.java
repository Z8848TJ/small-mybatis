package com.zzh.mybatis.executor;

import com.zzh.mybatis.mapping.BoundSql;
import com.zzh.mybatis.mapping.MappedStatement;
import com.zzh.mybatis.session.ResultHandler;
import com.zzh.mybatis.session.RowBounds;
import com.zzh.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author: zzh
 * @description: 执行器
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
    
}
