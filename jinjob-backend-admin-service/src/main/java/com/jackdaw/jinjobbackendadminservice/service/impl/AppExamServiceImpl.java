package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.*;
import com.jackdaw.jinjobbackendadminservice.service.AppExamService;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.dto.AppExamPostDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.*;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.ExamQuestionVO;
import com.jackdaw.jinjobbackendmodel.enums.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 用户在线考试 业务接口实现
 */
@Service("appExamService")
public class AppExamServiceImpl implements AppExamService {

    @Resource
    private AppExamMapper<AppExam, AppExamQuery> appExamMapper;

    @Resource
    private AppExamQuestionMapper<AppExamQuestion, AppExamQuestionQuery> appExamQuestionMapper;


    @Resource
    private ExamQuestionMapper<ExamQuestion, ExamQuestionQuery> examQuestionMapper;

    @Resource
    private ExamQuestionItemMapper<ExamQuestionItem, ExamQuestionItemQuery> examQuestionItemMapper;

    @Resource
    private AppUserCollectMapper<AppUserCollect, AppUserCollectQuery> appUserCollectMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppExam> findListByParam(AppExamQuery param) {
        return this.appExamMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppExamQuery param) {
        return this.appExamMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppExam> findListByPage(AppExamQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppExam> list = this.findListByParam(param);
        PaginationResultVO<AppExam> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppExam bean) {
        return this.appExamMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppExam> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appExamMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppExam> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appExamMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppExam bean, AppExamQuery param) {
        StringTools.checkParam(param);
        return this.appExamMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppExamQuery param) {
        StringTools.checkParam(param);
        return this.appExamMapper.deleteByParam(param);
    }

    /**
     * 根据ExamId获取对象
     */
    @Override
    public AppExam getAppExamByExamId(Integer examId) {
        return this.appExamMapper.selectByExamId(examId);
    }

    /**
     * 根据ExamId修改
     */
    @Override
    public Integer updateAppExamByExamId(AppExam bean, Integer examId) {
        return this.appExamMapper.updateByExamId(bean, examId);
    }

    /**
     * 根据ExamId删除
     */
    @Override
    public Integer deleteAppExamByExamId(Integer examId) {
        return this.appExamMapper.deleteByExamId(examId);
    }

    @Override
    public AppExam createExam(String categoryIds, AppUserLoginDto appDto) {
        AppExamQuestionQuery appExamQuestionQuery = new AppExamQuestionQuery();
        appExamQuestionQuery.setUserId(appDto.getUserId());
        appExamQuestionQuery.setAnswerResult(AnswerResultEnum.RIGHT.getResult());
        List<AppExamQuestion> dbAppExamQuestionList = appExamQuestionMapper.selectList(appExamQuestionQuery);
        List<Integer> excludeQuestionIdList = dbAppExamQuestionList.stream().map(item -> item.getQuestionId()).collect(Collectors.toList());

        ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
        examQuestionQuery.setCategoryIds(categoryIds.split(","));
        examQuestionQuery.setExcludeQuestinIdList(excludeQuestionIdList);
        examQuestionQuery.setOrderBy("rand()");
        examQuestionQuery.setStatus(PostStatusEnum.POST.getStatus());
        examQuestionQuery.setPageNo(1);
        examQuestionQuery.setSimplePage(new SimplePage(0, PageSize.SIZE50.getSize()));
        List<ExamQuestion> examQuestionList = examQuestionMapper.selectList(examQuestionQuery);
        if (examQuestionList.isEmpty()) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"该分类木有新题了");
        }
        AppExam appExam = new AppExam();
        appExam.setUserId(appDto.getUserId());
        appExam.setNickName(appDto.getNickName());
        appExam.setCreateTime(new Date());
        appExam.setStatus(AppExamStatusEnum.INIT.getStatus());
        appExamMapper.insert(appExam);

        List<AppExamQuestion> appExamQuestionList = new ArrayList<>();
        for (ExamQuestion examQuestion : examQuestionList) {
            AppExamQuestion appExamQuestion = new AppExamQuestion();
            appExamQuestion.setExamId(appExam.getExamId());
            appExamQuestion.setQuestionId(examQuestion.getQuestionId());
            appExamQuestion.setUserId(appDto.getUserId());
            appExamQuestion.setAnswerResult(AnswerResultEnum.INIT.getResult());
            appExamQuestionList.add(appExamQuestion);
        }
        if (!appExamQuestionList.isEmpty()) {
            appExamQuestionMapper.insertBatch(appExamQuestionList);
        }
        return appExam;
    }

    @Override
    public List<ExamQuestionVO> getAppExamQuestion(AppExamQuestionQuery appExamQuestionQuery) {
        //查询考卷的考题
        List<AppExamQuestion> appExamQuestionList = appExamQuestionMapper.selectList(appExamQuestionQuery);

        //考题map
        Map<Integer, AppExamQuestion> appExamQuestionMap = new HashMap<>();
        if (appExamQuestionQuery.getShowUserAnswer() != null && appExamQuestionQuery.getShowUserAnswer()) {
            appExamQuestionMap = appExamQuestionList.stream().collect(Collectors.toMap(item -> item.getQuestionId(),
                    Function.identity(), (data1, data2) -> data2));
        }
        //考题问题ID
        List<String> questionIdList = appExamQuestionList.stream().map(item -> item.getQuestionId() + "").collect(Collectors.toList());

        //查询考题
        ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
        examQuestionQuery.setQueryAnswer(appExamQuestionQuery.getShowUserAnswer());
        examQuestionQuery.setQuestionIds(questionIdList.toArray(new String[appExamQuestionList.size()]));
        List<ExamQuestion> examQuestionList = examQuestionMapper.selectList(examQuestionQuery);

        List<ExamQuestionVO> examQuestionVOList = CopyTools.copyList(examQuestionList, ExamQuestionVO.class);

        //查询考题选项
        ExamQuestionItemQuery examQuestionItemQuery = new ExamQuestionItemQuery();
        examQuestionItemQuery.setQuestionIdList(questionIdList);
        List<ExamQuestionItem> examQuestionItemList = examQuestionItemMapper.selectList(examQuestionItemQuery);

        Map<Integer, List<ExamQuestionItem>> tempExamItemMap = examQuestionItemList.stream().collect(Collectors.groupingBy(item -> item.getQuestionId()));

        //查询用户是否已收藏
        AppUserCollectQuery appUserCollectQuery = new AppUserCollectQuery();
        appUserCollectQuery.setObjectIdList(questionIdList);
        appUserCollectQuery.setUserId(appUserCollectQuery.getUserId());
        List<AppUserCollect> appUserCollectList = appUserCollectMapper.selectList(appUserCollectQuery);

        Map<String, AppUserCollect> appUserCollectMap = appUserCollectList.stream().collect(Collectors.toMap(item -> item.getObjectId(),
                Function.identity(), (data1, data2) -> data2));

        for (ExamQuestionVO item : examQuestionVOList) {
            if (appUserCollectMap.get(item.getQuestionId().toString()) != null) {
                item.setHaveCollect(true);
            } else {
                item.setHaveCollect(false);
            }
            item.setExamId(appExamQuestionQuery.getExamId());
            List<ExamQuestionItem> questionItemList = tempExamItemMap.get(item.getQuestionId());
            item.setQuestionItemList(questionItemList);
            if (!appExamQuestionQuery.getShowUserAnswer()) {
                continue;
            }
            AppExamQuestion appExamQuestion = appExamQuestionMap.get(item.getQuestionId());
            if (appExamQuestion != null) {
                item.setAnswerResult(appExamQuestion.getAnswerResult());
                item.setUserAnswer(appExamQuestion.getUserAnswer());
            }
        }
        return examQuestionVOList;
    }

    @Override
    public AppExam postExam(AppUserLoginDto appDto, AppExamPostDto appExamPostDto) {
        Date curDate = new Date();

        AppExam appExam = appExamMapper.selectByExamId(appExamPostDto.getExamId());
        if (null == appExam || !appExam.getUserId().equals(appDto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (!appExam.getStatus().equals(AppExamStatusEnum.INIT.getStatus())) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"考试已经提交");
        }

        List<AppExamQuestion> appExamQuestionList = appExamPostDto.getAppExamQuestionList();
        if (appExamQuestionList.isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //查询考试的所有题目
        AppExamQuestionQuery appExamQuestionQuery = new AppExamQuestionQuery();
        appExamQuestionQuery.setExamId(appExamPostDto.getExamId());
        List<AppExamQuestion> dbAppExamQuestionList = appExamQuestionMapper.selectList(appExamQuestionQuery);
        List<String> dbQuestionIdList = dbAppExamQuestionList.stream().map(item -> item.getQuestionId().toString()).collect(Collectors.toList());

        //查询问题
        ExamQuestionQuery examQuestionQuery = new ExamQuestionQuery();
        examQuestionQuery.setQueryAnswer(true);
        examQuestionQuery.setQuestionIds(dbQuestionIdList.toArray(new String[appExamQuestionList.size()]));
        List<ExamQuestion> examQuestionList = examQuestionMapper.selectList(examQuestionQuery);

        //转问题为map
        Map<Integer, ExamQuestion> tempExamItemMap = examQuestionList.stream().collect(Collectors.toMap(item ->
                item.getQuestionId(), Function.identity(), (data1, data2) -> data2));
        for (AppExamQuestion item : appExamQuestionList) {
            ExamQuestion examQuestion = tempExamItemMap.get(item.getQuestionId());
            if (examQuestion == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            item.setId(null);
            item.setExamId(appExam.getExamId());
            item.setUserId(appDto.getUserId());
            if (examQuestion.getQuestionAnswer().equals(item.getUserAnswer())) {
                item.setAnswerResult(AnswerResultEnum.RIGHT.getResult());
            } else {
                item.setAnswerResult(AnswerResultEnum.WRONG.getResult());
            }
        }
        appExamQuestionMapper.insertOrUpdateBatch(appExamQuestionList);

        AppExam udpateAppExam = new AppExam();
        udpateAppExam.setStatus(AppExamStatusEnum.FINISHED.getStatus());
        udpateAppExam.setEndTime(curDate);
        udpateAppExam.setRemark(appExamPostDto.getRemark());

        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setExamId(appExam.getExamId());
        appExamQuery.setUserId(appExam.getUserId());
        appExamQuery.setStatus(AppExamStatusEnum.INIT.getStatus());
        Integer count = appExamMapper.updateByParam(udpateAppExam, appExamQuery);
        if (count == 0) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"考试提交失败");
        }
        return appExam;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delExam4Api(String userId, Integer examId) {
        appExamMapper.deleteByExamId(examId);

        AppExamQuestionQuery params = new AppExamQuestionQuery();
        params.setExamId(examId);
        params.setUserId(userId);
        appExamQuestionMapper.deleteByParam(params);
    }
}