package com.rhzl.blog.service;

import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.params.ArticleParam;
import com.rhzl.blog.vo.params.PageParams;

public interface ArticleService {
    /**
     * 文章分列 分页
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    Result hotArtiscle(int i);

    Result newArtiscle(int i);

    Result listArchives();

    Result findArticleById(long articleId);

    Result publish(ArticleParam articleParam);
}
