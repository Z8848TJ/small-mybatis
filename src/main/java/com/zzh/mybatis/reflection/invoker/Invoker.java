package com.zzh.mybatis.reflection.invoker;

/**
 * @author: zzh
 * @description: 调用者
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();
}
