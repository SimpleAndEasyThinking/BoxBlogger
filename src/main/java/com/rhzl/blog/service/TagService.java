package com.rhzl.blog.service;

import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /*
    根据文章id查询标签列表
     */
    List<TagVo> findTagsByArticleId(long articleId);
    Result hots(int limit);

    Result findAll(int i);

    Result findAllDetail();

    Result findDetailByid(long id);
}
