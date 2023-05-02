package com.zzh.mybatis.builder;

import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.type.TypeAliasRegistry;

/**
 * @author: zzh
 * @description: 构建器的基类，建造者模式
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;
    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    
}
