package com.rhzl.blog.service;

import com.rhzl.blog.vo.CategoryVo;
import com.rhzl.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result findAllDetail();

    Result findAllDetailByid(Long id);
}
