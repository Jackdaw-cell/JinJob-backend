package com.jackdaw.jinjobbackendquestionservice;

import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestionSubmit.OjQuestionSubmitAddRequest;
import com.jackdaw.jinjobbackendmodel.entity.po.User;
import com.jackdaw.jinjobbackendquestionservice.controller.content.AppExamController;
import com.jackdaw.jinjobbackendquestionservice.service.OjQuestionSubmitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest(classes = {JinjobBackendQuestionServiceApplication.class})

class JinjobBackendQuestionServiceApplicationTests {

    @Resource
    private AppExamController appExamController;

    @Test
    void name() {
        appExamController.test(1);
    }

//    @Test
//    void contextLoads() {
//        User user = new User();
//        OjQuestionSubmitAddRequest ojQuestionSubmitAddRequest = new OjQuestionSubmitAddRequest();
//
//        ojQuestionSubmitService.doQuestionSubmit(ojQuestionSubmitAddRequest,user);
//    }

}
