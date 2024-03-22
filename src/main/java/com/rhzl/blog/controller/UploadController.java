package com.rhzl.blog.controller;

import com.rhzl.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
//    @PostMapping
//    public Result upload(@RequestParam("image")MultipartFile file){
//        //原始文件名称
//        String originalFilename = file.getOriginalFilename();
//        //拼接文件名称
//        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename,".");
//        //将图片放入图片服务器
//
//    }
}
