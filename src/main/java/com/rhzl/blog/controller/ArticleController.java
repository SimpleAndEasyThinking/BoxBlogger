package com.rhzl.blog.controller;

import com.rhzl.blog.service.ArticleService;
import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.params.ArticleParam;
import com.rhzl.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){

        return articleService.listArticle(pageParams);
    }
    @PostMapping("hot")
    public Result hotArticle(){
        return articleService.hotArtiscle(5);
    }
    @PostMapping("new")
    public Result newArticle(){
        return articleService.newArtiscle(5);
    }

    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
