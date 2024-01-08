package com.jackdaw.jinjobbackendadminservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.Category;
import com.jackdaw.jinjobbackendmodel.entity.query.CategoryQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 分类 业务接口
 */
public interface CategoryService {

    /**
     * 根据条件查询列表
     */
    List<Category> findListByParam(CategoryQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(CategoryQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<Category> findListByPage(CategoryQuery param);

    /**
     * 新增
     */
    Integer add(Category bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<Category> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<Category> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(Category bean, CategoryQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(CategoryQuery param);

    /**
     * 根据CategoryId查询对象
     */
    Category getCategoryByCategoryId(Integer categoryId);


    /**
     * 根据CategoryId修改
     */
    Integer updateCategoryByCategoryId(Category bean, Integer categoryId);


    /**
     * 根据CategoryId删除
     */
    Integer deleteCategoryByCategoryId(Integer categoryId);

    void saveCategory(Category category);

    List<Category> loadAllCategoryByType(Integer type);

    void changeSort(String categoryIds);
}