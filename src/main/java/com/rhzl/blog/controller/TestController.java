package com.rhzl.blog.controller;

import com.rhzl.blog.dao.pojo.SysUser;
import com.rhzl.blog.utils.UserThreadLocal;
import com.rhzl.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
