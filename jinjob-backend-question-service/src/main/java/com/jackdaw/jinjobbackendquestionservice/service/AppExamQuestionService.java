package com.jackdaw.jinjobbackendquestionservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.AppExamQuestion;
import com.jackdaw.jinjobbackendmodel.entity.query.AppExamQuestionQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 考试问题 业务接口
 */
public interface AppExamQuestionService {

	/**
	 * 根据条件查询列表
	 */
	List<AppExamQuestion> findListByParam(AppExamQuestionQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppExamQuestionQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppExamQuestion> findListByPage(AppExamQuestionQuery param);

	/**
	 * 新增
	 */
	Integer add(AppExamQuestion bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppExamQuestion> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppExamQuestion> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppExamQuestion bean,AppExamQuestionQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppExamQuestionQuery param);

	/**
	 * 根据Id查询对象
	 */
	AppExamQuestion getAppExamQuestionById(Integer id);


	/**
	 * 根据Id修改
	 */
	Integer updateAppExamQuestionById(AppExamQuestion bean,Integer id);


	/**
	 * 根据Id删除
	 */
	Integer deleteAppExamQuestionById(Integer id);


	/**
	 * 根据ExamIdAndUserIdAndQuestionId查询对象
	 */
	AppExamQuestion getAppExamQuestionByExamIdAndUserIdAndQuestionId(Integer examId,String userId,Integer questionId);


	/**
	 * 根据ExamIdAndUserIdAndQuestionId修改
	 */
	Integer updateAppExamQuestionByExamIdAndUserIdAndQuestionId(AppExamQuestion bean,Integer examId,String userId,Integer questionId);


	/**
	 * 根据ExamIdAndUserIdAndQuestionId删除
	 */
	Integer deleteAppExamQuestionByExamIdAndUserIdAndQuestionId(Integer examId,String userId,Integer questionId);

}