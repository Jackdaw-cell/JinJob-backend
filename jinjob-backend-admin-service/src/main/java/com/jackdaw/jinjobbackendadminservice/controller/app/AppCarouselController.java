package com.jackdaw.jinjobbackendadminservice.controller.app;

import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.entity.po.AppCarousel;
import com.jackdaw.jinjobbackendmodel.entity.query.AppCarouselQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.AppCarouselService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.entity.query.AppCarouselQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * app轮播 Controller
 */
@RestController("appCarouselController")
@RequestMapping("/appCarousel")
@Slf4j
public class AppCarouselController extends ABaseController {

    @Resource
    private AppCarouselService appCarouselService;

    /**
     * 根据条件分页查询
     */
    @PostMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_LIST)
    public ResponseVO loadDataList( AppCarouselQuery query) {
        query.setOrderBy("sort asc");
        return getSuccessResponseVO(appCarouselService.findListByParam(query));
    }

    /**
     * 新增
     */
    @PostMapping("/saveCarousel")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
    public ResponseVO saveCarousel( AppCarousel bean) {
        appCarouselService.saveCarousel(bean);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delCarousel")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
    public ResponseVO delCarousel(Integer carouselId) {
        appCarouselService.deleteAppCarouselByCarouselId(carouselId);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/changeSort")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
    public ResponseVO changeSort(@VerifyParam(required = true) String carouselIds) {
        appCarouselService.changeSort(carouselIds);
        return getSuccessResponseVO(null);
    }
}