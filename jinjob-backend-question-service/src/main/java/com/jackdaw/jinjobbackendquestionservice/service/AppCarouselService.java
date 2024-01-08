package com.jackdaw.jinjobbackendquestionservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.AppCarousel;
import com.jackdaw.jinjobbackendmodel.entity.query.AppCarouselQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * app轮播 业务接口
 */
public interface AppCarouselService {

    /**
     * 根据条件查询列表
     */
    List<AppCarousel> findListByParam(AppCarouselQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(AppCarouselQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<AppCarousel> findListByPage(AppCarouselQuery param);

    /**
     * 新增
     */
    Integer add(AppCarousel bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<AppCarousel> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<AppCarousel> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(AppCarousel bean, AppCarouselQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(AppCarouselQuery param);

    /**
     * 根据CarouselId查询对象
     */
    AppCarousel getAppCarouselByCarouselId(Integer carouselId);


    /**
     * 根据CarouselId修改
     */
    Integer updateAppCarouselByCarouselId(AppCarousel bean, Integer carouselId);


    /**
     * 根据CarouselId删除
     */
    Integer deleteAppCarouselByCarouselId(Integer carouselId);

    void saveCarousel(AppCarousel appCarousel);

    void changeSort(String carouselIds);
}