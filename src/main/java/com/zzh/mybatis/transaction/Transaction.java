package com.zzh.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author: zzh
 * @description: 事务接口
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
    
}
