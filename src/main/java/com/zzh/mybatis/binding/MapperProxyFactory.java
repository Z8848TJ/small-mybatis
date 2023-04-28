package com.zzh.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author: zzh
 * @description: 映射器器代理工厂
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(Map<String, Object> sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        /*
         * mapperInterface.getClassLoader(): 指定加载代理对象的类加载器
         *
         * new Class[]{mapperInterface}： 指定代理对象要代理那些接口中的方法
         *
         * mapperProxy：指定代理对象具体要做那些事情
         *
         * return：代理对象
         */
        return (T)Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

}
