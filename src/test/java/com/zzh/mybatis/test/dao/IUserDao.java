package com.zzh.mybatis.test.dao;

import com.zzh.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);
    
    User queryUserInfo(User user);

}
