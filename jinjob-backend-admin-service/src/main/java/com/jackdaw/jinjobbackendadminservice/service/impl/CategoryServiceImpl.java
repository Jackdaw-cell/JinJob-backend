package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.CategoryMapper;
import com.jackdaw.jinjobbackendadminservice.mapper.ExamQuestionMapper;
import com.jackdaw.jinjobbackendadminservice.mapper.QuestionInfoMapper;
import com.jackdaw.jinjobbackendadminservice.service.CategoryService;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.Category;
import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestion;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.CategoryQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.CategoryTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.enums.ResponseCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 分类 业务接口实现
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper<Category, CategoryQuery> categoryMapper;

    @Resource
    private QuestionInfoMapper<QuestionInfo, QuestionInfoQuery> questionInfoMapper;

    @Resource
    private ExamQuestionMapper<ExamQuestion, ExamQuestionQuery> examQuestionMapper;


    /**
     * 根据条件查询列表
     */
    @Override
    public List<Category> findListByParam(CategoryQuery param) {
        return this.categoryMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(CategoryQuery param) {
        return this.categoryMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<Category> findListByPage(CategoryQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Category> list = this.findListByParam(param);
        PaginationResultVO<Category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Category bean) {
        return this.categoryMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Category> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.categoryMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Category bean, CategoryQuery param) {
        StringTools.checkParam(param);
        return this.categoryMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(CategoryQuery param) {
        StringTools.checkParam(param);
        return this.categoryMapper.deleteByParam(param);
    }

    /**
     * 根据CategoryId获取对象
     */
    @Override
    public Category getCategoryByCategoryId(Integer categoryId) {
        return this.categoryMapper.selectByCategoryId(categoryId);
    }

    /**
     * 根据CategoryId修改
     */
    @Override
    public Integer updateCategoryByCategoryId(Category bean, Integer categoryId) {
        return this.categoryMapper.updateByCategoryId(bean, categoryId);
    }

    /**
     * 根据CategoryId删除
     */
    @Override
    public Integer deleteCategoryByCategoryId(Integer categoryId) {
        QuestionInfoQuery questionInfoQuery = new QuestionInfoQuery();
        questionInfoQuery.setCategoryId(categoryId);
        Integer count = questionInfoMapper.selectCount(questionInfoQuery);
        if (count > 0) {
            throw new BusinessException("问题库正在使用改分类无法删除");
        }
        ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
        examQuestionQuery.setCategoryId(categoryId);
        count = examQuestionMapper.selectCount(examQuestionQuery);
        if (count > 0) {
            throw new BusinessException("试题库正在使用改分类无法删除");
        }
        return this.categoryMapper.deleteByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(Category category) {
        if (null == category.getCategoryId()) {
            CategoryQuery categoryQuery = new CategoryQuery();
            Integer count = this.categoryMapper.selectCount(categoryQuery);
            category.setSort(count + 1);
            this.categoryMapper.insert(category);
        } else {
            this.categoryMapper.updateByCategoryId(category, category.getCategoryId());
        }
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setCategoryName(category.getCategoryName());
        categoryQuery.setType(category.getType());
        Integer count = this.categoryMapper.selectCount(categoryQuery);
        if (count > 1) {
            throw new BusinessException("分类名称重复");
        }
        if (null == category.getCategoryId()) {
            return;
        }
        Category dbInfo = categoryMapper.selectByCategoryId(category.getCategoryId());
        //更新冗余的分类名称
        if (!dbInfo.getCategoryName().equals(category.getCategoryName())) {
            categoryMapper.updateCategoryName(category.getCategoryName(), category.getCategoryId());
        }
    }

    @Override
    public List<Category> loadAllCategoryByType(Integer type) {
        CategoryTypeEnum typeEnum = CategoryTypeEnum.getByType(type);
        if (typeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setOrderBy("sort asc");
        categoryQuery.setTypes(new Integer[]{typeEnum.getType(), CategoryTypeEnum.QUESTION_EXAM.getType()});
        return this.categoryMapper.selectList(categoryQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSort(String categoryIds) {
        String[] categoryIdArray = categoryIds.split(",");
        Integer index = 1;
        for (String categoryIdStr : categoryIdArray) {
            Integer categoryId = Integer.parseInt(categoryIdStr);
            Category category = new Category();
            category.setSort(index);
            categoryMapper.updateByCategoryId(category, categoryId);
            index++;
        }
    }
}