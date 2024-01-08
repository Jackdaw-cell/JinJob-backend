package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendmodel.entity.po.*;
import com.jackdaw.jinjobbackendmodel.entity.query.AppCarouselQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.ExamQuestionItemQuery;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.RequestFrequencyTypeEnum;

import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类 Controller
 */
@RestController("indexController")
@RequestMapping("/index")
public class IndexController extends ABaseController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private AppCarouselService appCarouselService;

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private ExamQuestionItemService examQuestionItemService;

    @Resource
    private AppDeviceService appDeviceService;


    /**
     * 根据条件分页查询
     */
    @GetMapping("/loadAllCategory")
    @GlobalInterceptor
    public BaseResponse<List<Category>> loadAllCategory(@VerifyParam(required = true) Integer type) {
//        return getSuccessResponseVO(categoryService.loadAllCategoryByType(type));
        return ResultUtils.success(categoryService.loadAllCategoryByType(type));
    }

    @GetMapping("/loadCarousel")
    @GlobalInterceptor
    public BaseResponse<List<AppCarousel>> loadCarousel() {
        AppCarouselQuery query = new AppCarouselQuery();
        query.setOrderBy("sort asc");
        List<AppCarousel> carouselList = appCarouselService.findListByParam(query);
//        return getSuccessResponseVO(carouselList);
        return ResultUtils.success(carouselList);

    }

    @GetMapping("/getExamQuestionById")
    @GlobalInterceptor
    public BaseResponse<ExamQuestion> getExamQuestionById(@VerifyParam(required = true)
                                                          @RequestParam Integer questionId) {
        ExamQuestion examQuestion = examQuestionService.getExamQuestionByQuestionId(questionId);
        if (examQuestion == null || !PostStatusEnum.POST.getStatus().equals(examQuestion.getStatus())) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ExamQuestionItemQuery itemQuery = new ExamQuestionItemQuery();
        itemQuery.setQuestionId(examQuestion.getQuestionId());
        itemQuery.setOrderBy("sort asc");
        List<ExamQuestionItem> questionItemList = examQuestionItemService.findListByParam(itemQuery);
        examQuestion.setQuestionItemList(questionItemList);
//        return getSuccessResponseVO(examQuestion);
        return ResultUtils.success(examQuestion);
    }

    @PostMapping("/report")
    @GlobalInterceptor(checkParams = false, frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public BaseResponse<Object> report(HttpServletRequest request,
                                       @VerifyParam(required = true, max = 32) @RequestParam String deviceId,
                                       @VerifyParam(max = 30) @RequestParam String deviceBrand) {
        AppDevice appDevice = new AppDevice();
        appDevice.setDeviceId(deviceId);
        appDevice.setDeviceBrand(deviceBrand);
        appDevice.setIp(getIpAddr(request));
        appDeviceService.reportData(appDevice);
//        return getSuccessResponseVO(null);
        return ResultUtils.success(null);
    }
}