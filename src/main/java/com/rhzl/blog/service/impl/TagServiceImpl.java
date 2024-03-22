package com.rhzl.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rhzl.blog.dao.mapper.TagMapper;
import com.rhzl.blog.dao.pojo.Tag;
import com.rhzl.blog.service.TagService;
import com.rhzl.blog.vo.Result;
import com.rhzl.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tags){
        List<TagVo> tagVos = new ArrayList<>();
        for(Tag tag:tags){
            tagVos.add(copy(tag));
        }
        return tagVos;
    }
    @Override
    public List<TagVo> findTagsByArticleId(long articleId) {
        //mybatisPlus 无法进行多表查询
        List<Tag>  tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit){
        /**
         * 返回标签中最热门的
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll(int i) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);

        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());

        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailByid(long id) {
        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }
}
