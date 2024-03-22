package com.rhzl.blog.service;

import com.rhzl.blog.dao.pojo.SysUser;
import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.UserVo;

public interface SysUserService {
    SysUser findUserByid(long id);
    UserVo findUserVoByid(long id);
    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

}
