package com.jackdaw.jinjobbackendadminservice.controller.content;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.CategoryService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.po.Category;
import com.jackdaw.jinjobbackendmodel.entity.query.CategoryQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 分类 Controller
 */
@RestController("categoryController")
@RequestMapping("/category")
public class CategoryController extends ABaseController {

    @Resource
    private CategoryService categoryService;

    /**
     * 根据条件分页查询
     */
    @PostMapping("/loadAllCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEOGRY_LIST)
    public ResponseVO loadDataList(CategoryQuery query) {
        query.setOrderBy("sort asc");
        return getSuccessResponseVO(categoryService.findListByParam(query));
    }

    @PostMapping("/saveCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEOGRY_EDIT)
    public ResponseVO saveCategory(Category category) {
        categoryService.saveCategory(category);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delCategory")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEOGRY_EDIT)
    public ResponseVO delCategory(@VerifyParam(required = true) Integer categoryId) {
        categoryService.deleteCategoryByCategoryId(categoryId);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/changeSort")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEOGRY_EDIT)
    public ResponseVO changeSort(@VerifyParam(required = true) String categoryIds) {
        categoryService.changeSort(categoryIds);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/loadAllCategory4Select")
    @GlobalInterceptor
    public ResponseVO loadAllCategory(@VerifyParam(required = true) Integer type) {
        return getSuccessResponseVO(categoryService.loadAllCategoryByType(type));
    }
}