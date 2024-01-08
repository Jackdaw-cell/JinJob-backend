package com.jackdaw.jinjobbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackdaw.jinjobbackendquestionservice.service.AppExamAnswerQuestionService;
import com.jackdaw.jinjobbackendmodel.entity.po.AppExamAnswerQuestion;
import com.jackdaw.jinjobbackendquestionservice.mapper.AppExamAnswerQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author Jackdaw
* @description 针对表【app_exam_answer_question(面试题回答纪录)】的数据库操作Service实现
* @createDate 2024-01-03 11:26:26
*/
@Service
public class AppExamAnswerQuestionServiceImpl extends ServiceImpl<AppExamAnswerQuestionMapper, AppExamAnswerQuestion>
    implements AppExamAnswerQuestionService {

}




