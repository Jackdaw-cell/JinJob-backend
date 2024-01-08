package com.jackdaw.jinjobbackendquestionservice.service;

import com.jackdaw.jinjobbackendmodel.entity.dto.AppExamPostDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.AppExam;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;

import java.util.List;


/**
 * 用户在线考试 业务接口
 */
public interface AppExamService {

    /**
     * 根据条件查询列表
     */
    List<AppExam> findListByParam(AppExamQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(AppExamQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<AppExam> findListByPage(AppExamQuery param);

    /**
     * 新增
     */
    Integer add(AppExam bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<AppExam> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<AppExam> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(AppExam bean, AppExamQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(AppExamQuery param);

    /**
     * 根据ExamId查询对象
     */
    AppExam getAppExamByExamId(Integer examId);


    /**
     * 根据ExamId修改
     */
    Integer updateAppExamByExamId(AppExam bean, Integer examId);

    /**
     * 根据ExamId删除
     */
    Integer deleteAppExamByExamId(Integer examId);

    AppExam createExam(List<String> categoryIds, AppUserLoginDto appDto);

    List<ExamQuestionVO> getAppExamQuestion(AppExamQuestionQuery appExamQuestionQuery);

    AppExam postExam(AppUserLoginDto appDto, AppExamPostDto appExamPostDto);

    void delExam4Api(String userId, Integer examId);
}