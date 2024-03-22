package com.rhzl.blog.controller;

import com.rhzl.blog.service.TagService;
import com.rhzl.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService tagService;


    @GetMapping("hot")
    public Result hots(){
        return tagService.hots(6);
    }
    @GetMapping
    public Result findAll(){
        return tagService.findAll(6);
    }
    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result findDetailByid(@PathVariable("id")Long id){
        return tagService.findDetailByid(id);
    }
}
