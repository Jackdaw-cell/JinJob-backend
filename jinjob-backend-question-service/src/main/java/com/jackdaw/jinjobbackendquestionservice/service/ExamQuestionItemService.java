package com.jackdaw.jinjobbackendquestionservice.service;

import com.jackdaw.jinjobbackendmodel.entity.po.ExamQuestionItem;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionItemQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;

import java.util.List;


/**
 *  业务接口
 */
public interface ExamQuestionItemService {

	/**
	 * 根据条件查询列表
	 */
	List<ExamQuestionItem> findListByParam(ExamQuestionItemQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ExamQuestionItemQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ExamQuestionItem> findListByPage(ExamQuestionItemQuery param);

	/**
	 * 新增
	 */
	Integer add(ExamQuestionItem bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ExamQuestionItem> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ExamQuestionItem> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(ExamQuestionItem bean,ExamQuestionItemQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ExamQuestionItemQuery param);

	/**
	 * 根据ItemId查询对象
	 */
	ExamQuestionItem getExamQuestionItemByItemId(Integer itemId);


	/**
	 * 根据ItemId修改
	 */
	Integer updateExamQuestionItemByItemId(ExamQuestionItem bean,Integer itemId);


	/**
	 * 根据ItemId删除
	 */
	Integer deleteExamQuestionItemByItemId(Integer itemId);

}