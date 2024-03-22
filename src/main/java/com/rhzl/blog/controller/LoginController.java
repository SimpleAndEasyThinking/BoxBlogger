package com.rhzl.blog.controller;


import com.rhzl.blog.service.LoginService;
import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //验证用户完整性
        //
        return loginService.login(loginParam);
    }
}
