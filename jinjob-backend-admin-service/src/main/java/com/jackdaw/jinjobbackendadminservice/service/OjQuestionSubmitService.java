package com.jackdaw.jinjobbackendadminservice.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitQueryRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import com.jackdaw.jinjobbackendmodel.entity.vo.OjQuestionSubmitVO;

/**
* @author Jackdaw
* @description@description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-08-07 20:58:53
*/
public interface OjQuestionSubmitService extends IService<OjQuestionSubmit> {

    /**
     * 获取查询条件
     *
     * @param ojQuestionSubmitQueryRequest
     * @return
     */
    QueryWrapper<OjQuestionSubmit> getQueryWrapper(OjQuestionSubmitQueryRequest ojQuestionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param ojQuestionSubmit
     * @param loginUser
     * @return
     */
    OjQuestionSubmitVO getQuestionSubmitVO(OjQuestionSubmit ojQuestionSubmit, AppUserInfo loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<OjQuestionSubmitVO> getQuestionSubmitVOPage(Page<OjQuestionSubmit> questionSubmitPage, AppUserInfo loginUser);
}
