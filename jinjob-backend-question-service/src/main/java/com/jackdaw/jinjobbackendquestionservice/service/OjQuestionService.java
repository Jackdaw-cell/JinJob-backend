package com.jackdaw.jinjobbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.OjQuestionQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Jackdaw 针对表【question(题目)】的数据库操作Service
* @createDate 2023-08-07 20:58:00
*/
public interface OjQuestionService extends IService<OjQuestionInfo> {


    /**
     * 校验
     *
     * @param ojQuestionInfo
     * @param add
     */
    void validQuestion(OjQuestionInfo ojQuestionInfo, boolean add);

    /**
     * 获取查询条件
     *
     * @param ojQuestionQueryRequest
     * @return
     */
    QueryWrapper<OjQuestionInfo> getQueryWrapper(OjQuestionQueryRequest ojQuestionQueryRequest);
    
    /**
     * 获取题目封装
     *
     * @param ojQuestionInfo
     * @param request
     * @return
     */
    OjQuestionVO getQuestionVO(OjQuestionInfo ojQuestionInfo, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @return
     */
//    Page<OjQuestionVO> getQuestionVOPage(Page<OjQuestion> questionPage);
    
}
