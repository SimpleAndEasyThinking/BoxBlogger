package com.rhzl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rhzl.blog.dao.dos.Archives;
import com.rhzl.blog.dao.mapper.ArticleBodyMapper;
import com.rhzl.blog.dao.mapper.ArticleMapper;
import com.rhzl.blog.dao.mapper.ArticleTagMapper;
import com.rhzl.blog.dao.pojo.Article;
import com.rhzl.blog.dao.pojo.ArticleBody;
import com.rhzl.blog.dao.pojo.ArticleTag;
import com.rhzl.blog.dao.pojo.SysUser;
import com.rhzl.blog.service.*;
import com.rhzl.blog.utils.UserThreadLocal;
import com.rhzl.blog.vo.*;
import com.rhzl.blog.vo.params.ArticleParam;
import com.rhzl.blog.vo.params.PageParams;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActicleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

 //   @Override
//    public Result listArticle(PageParams pageParams) {
//        /*
//        *   1.分页查询 article 数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null) {
//            //and category_id=#{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if(pageParams.getTagId()!=null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleTags.size() > 0) {
//                // and id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//
//        }
//        //置顶   权重排序
//        //按时间排序
//        queryWrapper.orderByDesc(Article::getCreateDate,Article::getWeight);
//        Page<Article> articlePage = articleMapper.selectPage(page,queryWrapper);
//        List<Article> recorder = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(recorder,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArtiscle(int i) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+i);
        List<Article> articles =  articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArtiscle(int i) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);

        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+i);
        List<Article> articles =  articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archives = articleMapper.listArchives();
        return Result.success(archives);
    }

    @Autowired
    private ThreadService threadService;
    @Override
    public Result findArticleById(long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        Article article = new Article();
        //录入作者ID
        SysUser sysUser = UserThreadLocal.get();
        article.setAuthorId(sysUser.getId());
        //录入标题
        article.setTitle(articleParam.getTitle());
        //录入内容id，需要插入文章后再次录入
        //article.setBodyId();
        //录入类别
        article.setCategoryId(articleParam.getCategory().getId());
        //录入概述
        article.setSummary(articleParam.getSummary());
        //录入创建时间
        article.setCreateDate(System.currentTimeMillis());
        //录入评论和浏览数
        article.setViewCounts(0);
        article.setCommentCounts(0);
        //录入
        article.setWeight(Article.Article_Common);
        //插入数据
        this.articleMapper.insert(article);

        List<TagVo> tagVos = articleParam.getTags();
        if (tagVos != null)
        for(TagVo tagVo:tagVos){
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tagVo.getId());
            this.articleTagMapper.insert(articleTag);
        }
        //录入文章本体
        ArticleBody articleBody = new ArticleBody();

        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //录入内容id，需要插入文章后再次录入
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);

    }

    private List<ArticleVo> copyList(List<Article> recorder,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : recorder){
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> recorder,boolean isTag,boolean isAuthor,boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : recorder){
            articleVoList.add(copy(record,isTag,isAuthor,isBody,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> recorder,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for(Article record : recorder){
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo= new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-mm-dd HH:mm"));

        if (isTag){
            long articleId=article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserByid(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategorys(categoryService.findCategoryById(categoryId));
        }

        return articleVo;

    }

    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    //构建ArticleBodyMapper
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }



}
