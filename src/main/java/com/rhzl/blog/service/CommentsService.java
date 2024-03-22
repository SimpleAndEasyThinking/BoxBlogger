package com.rhzl.blog.service;

import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.params.CommentParam;

public interface CommentsService {
    /*
     * 根据文章id查询所有的评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(long id);

    Result comment(CommentParam commentParam);

}
