package com.jackdaw.jinjobbackendquestionservice.service;

import com.jackdaw.jinjobbackendmodel.entity.dto.ImportErrorItem;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 问题 业务接口
 */
public interface QuestionInfoService {


    /**
     * 根据条件查询列表
     */
    List<QuestionInfo> findListByParam(QuestionInfoQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(QuestionInfoQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<QuestionInfo> findListByPage(QuestionInfoQuery param);

    PaginationResultVO<QuestionInfo> findListByPageRandom(QuestionInfoQuery param);

    /**
     * 新增
     */
    Integer add(QuestionInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<QuestionInfo> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<QuestionInfo> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(QuestionInfo bean, QuestionInfoQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(QuestionInfoQuery param);

    /**
     * 根据QuestionId查询对象
     */
    QuestionInfo getQuestionInfoByQuestionId(Integer questionId);


    /**
     * 根据QuestionId修改
     */
    Integer updateQuestionInfoByQuestionId(QuestionInfo bean, Integer questionId);


    /**
     * 根据QuestionId删除
     */
    Integer deleteQuestionInfoByQuestionId(Integer questionId);

    void saveQuestion(QuestionInfo questionInfo, Boolean superAdmin);

    void delQuestionBatch(String questionIds, Integer userId);

    List<ImportErrorItem> importQuestion(MultipartFile file, SessionUserAdminDto userAdminDto);

    QuestionInfo showDetailNext(QuestionInfoQuery query, Integer type, Integer currentId, Boolean updateReadCount);
}