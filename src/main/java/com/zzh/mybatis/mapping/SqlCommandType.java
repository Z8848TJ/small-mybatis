package com.zzh.mybatis.mapping;

import org.omg.CORBA.UNKNOWN;

/**
 * @author: zzh
 * @description: SQL 指令类型
 */
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;
    
}
