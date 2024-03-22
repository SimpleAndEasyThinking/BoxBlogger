package com.rhzl.blog.service;

import com.rhzl.blog.dao.pojo.SysUser;
import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.params.LoginParam;

public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
