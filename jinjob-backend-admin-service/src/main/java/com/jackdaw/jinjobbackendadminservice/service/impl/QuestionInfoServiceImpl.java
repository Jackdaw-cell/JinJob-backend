package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.ACommonMapper;
import com.jackdaw.jinjobbackendadminservice.mapper.QuestionInfoMapper;
import com.jackdaw.jinjobbackendadminservice.service.CategoryService;
import com.jackdaw.jinjobbackendadminservice.service.QuestionInfoService;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.ExcelUtils;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendcommon.utils.VerifyUtils;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.ImportErrorItem;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.Category;
import com.jackdaw.jinjobbackendmodel.entity.po.QuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.QuestionInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 问题 业务接口实现
 */
@Service("questionInfoService")
public class QuestionInfoServiceImpl implements QuestionInfoService {

    @Resource
    private QuestionInfoMapper<QuestionInfo, QuestionInfoQuery> questionInfoMapper;

    @Resource
    private CategoryService categoryService;

    @Resource
    private ACommonMapper aCommonMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<QuestionInfo> findListByParam(QuestionInfoQuery param) {
        return this.questionInfoMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(QuestionInfoQuery param) {
        return this.questionInfoMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<QuestionInfo> findListByPage(QuestionInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<QuestionInfo> list = this.findListByParam(param);
        PaginationResultVO<QuestionInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(QuestionInfo bean) {
        return this.questionInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<QuestionInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.questionInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<QuestionInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.questionInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(QuestionInfo bean, QuestionInfoQuery param) {
        StringTools.checkParam(param);
        return this.questionInfoMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(QuestionInfoQuery param) {
        StringTools.checkParam(param);
        return this.questionInfoMapper.deleteByParam(param);
    }

    /**
     * 根据QuestionId获取对象
     */
    @Override
    public QuestionInfo getQuestionInfoByQuestionId(Integer questionId) {
        return this.questionInfoMapper.selectByQuestionId(questionId);
    }

    /**
     * 根据QuestionId修改
     */
    @Override
    public Integer updateQuestionInfoByQuestionId(QuestionInfo bean, Integer questionId) {
        return this.questionInfoMapper.updateByQuestionId(bean, questionId);
    }

    /**
     * 根据QuestionId删除
     */
    @Override
    public Integer deleteQuestionInfoByQuestionId(Integer questionId) {
        return this.questionInfoMapper.deleteByQuestionId(questionId);
    }

    @Override
    public void saveQuestion(QuestionInfo questionInfo, Boolean superAdmin) {
        Category category = categoryService.getCategoryByCategoryId(questionInfo.getCategoryId());
        questionInfo.setCategoryName(category.getCategoryName());
        if (null == questionInfo.getQuestionId()) {
            questionInfo.setCreateTime(new Date());
            this.questionInfoMapper.insert(questionInfo);
        } else {
            QuestionInfo dbInfo = this.questionInfoMapper.selectByQuestionId(questionInfo.getQuestionId());
            /*if (PostStatusEnum.POST.getStatus().equals(dbInfo.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }*/
            //只能自己发布的才能修改
            if (!dbInfo.getCreateUserId().equals(questionInfo.getCreateUserId()) && !superAdmin) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            questionInfo.setCreateUserId(null);
            questionInfo.setCreateUserName(null);
            questionInfo.setCreateTime(null);
            this.questionInfoMapper.updateByQuestionId(questionInfo, questionInfo.getQuestionId());
        }
    }

    @Override
    public void delQuestionBatch(String questionIds, Integer userId) {
        String[] questionIdArray = questionIds.split(",");
        if (userId != null) {
            QuestionInfoQuery infoQuery = new QuestionInfoQuery();
            infoQuery.setQuestionIds(questionIdArray);
            List<QuestionInfo> questionInfoList = this.questionInfoMapper.selectList(infoQuery);
            List<QuestionInfo> currentUserDataList = questionInfoList.stream()
                    .filter(a -> !a.getCreateUserId().equals(String.valueOf(userId))).collect(Collectors.toList());
            if (!currentUserDataList.isEmpty()) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }
        questionInfoMapper.deleteBatchByQuestionId(questionIdArray, PostStatusEnum.NO_POST.getStatus(), userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportErrorItem> importQuestion(MultipartFile file, SessionUserAdminDto userAdminDto) {
        //查询出所有的分类
        List<Category> categoryList = categoryService.loadAllCategoryByType(CategoryTypeEnum.QUESTION.getType());
        Map<String, Category> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));

        List<List<String>> dataList = ExcelUtils.readExcel(file, Constants.EXCEL_TITLE_QUESTION, Constants.ONE);

        //错误列表
        List<ImportErrorItem> errorList = new ArrayList<>();

        //问题列表
        List<QuestionInfo> questionList = new ArrayList<>();

        //数据行编号
        Integer dataRowNum = 2;
        for (List<String> row : dataList) {
            if (errorList.size() > Constants.LENGTH_50) {
                throw new BusinessException("错误数据超过" + Constants.LENGTH_50 + "行，请认真检查数据后再导入");
            }
            dataRowNum++;
            List<String> errorItemList = new ArrayList<>();
            Integer index = 0;
            //标题
            String title = row.get(index++);
            if (StringTools.isEmpty(title) || title.length() > Constants.LENGTH_150) {
                errorItemList.add("标题不能为空，且长度不能超过150");
            }
            //分类
            String categoryName = row.get(index++);
            Category category = categoryMap.get(categoryName);
            if (category == null) {
                errorItemList.add("分类名称不存在");
            }
            //难度
            String difficultyLevel = row.get(index++);
            Integer difficultyLevelInt = null;
            if (!VerifyUtils.verify(VerifyRegexEnum.NUMBER_LETTER_UNDER_LINE, difficultyLevel)) {
                difficultyLevelInt = Integer.parseInt(difficultyLevel);
                if (difficultyLevelInt > 5) {
                    errorItemList.add("难度只能是1到5的数字");
                }
            } else {
                errorItemList.add("难度必须是数字");
            }

            //问题描述
            String question = row.get(index++);
            String answerAnalysis = row.get(index++);
            if (StringTools.isEmpty(answerAnalysis)) {
                errorItemList.add("答案解析不能为空");
            }

            //有错误就继续
            if (errorItemList.size() > 0 || errorList.size() > 0) {
                ImportErrorItem errorItem = new ImportErrorItem();
                errorItem.setRowNum(dataRowNum);
                errorItem.setErrorItemList(errorItemList);
                errorList.add(errorItem);
                continue;
            }
            //封装对象
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setTitle(title);
            questionInfo.setCategoryId(category.getCategoryId());
            questionInfo.setCategoryName(category.getCategoryName());
            questionInfo.setDifficultyLevel(difficultyLevelInt);
            questionInfo.setQuestion(question);
            questionInfo.setAnswerAnalysis(answerAnalysis);
            questionInfo.setCreateTime(new Date());
            questionInfo.setStatus(PostStatusEnum.NO_POST.getStatus());
            questionInfo.setCreateUserId(String.valueOf(userAdminDto.getUserId()));
            questionInfo.setCreateUserName(userAdminDto.getUserName());
            questionList.add(questionInfo);
        }
        if (questionList.isEmpty()) {
            return errorList;
        }
        //插入数据库
        this.questionInfoMapper.insertBatch(questionList);
        return errorList;
    }

    @Override
    public QuestionInfo showDetailNext(QuestionInfoQuery query, Integer type, Integer currentId, Boolean updateReadCount) {
        if (type == null) {
            query.setQuestionId(currentId);
        } else {
            query.setNextType(type);
            query.setCurrentId(currentId);
        }
        QuestionInfo questionInfo = questionInfoMapper.showDetailNext(query);
        if (questionInfo == null && type == null) {
            throw new BusinessException("内容不存在");
        } else if (questionInfo == null && type == -1) {
            throw new BusinessException("已经是第一条了");
        } else if (questionInfo == null && type == 1) {
            throw new BusinessException("已经是最后一条了");
        }

        if (updateReadCount && questionInfo != null) {
            //更新阅读数
            aCommonMapper.updateCount(Constants.TABLE_NAME_QUESTION_INFO, 1, null, currentId);
            questionInfo.setReadCount(questionInfo.getReadCount() + 1);
        }
        return questionInfo;
    }
}