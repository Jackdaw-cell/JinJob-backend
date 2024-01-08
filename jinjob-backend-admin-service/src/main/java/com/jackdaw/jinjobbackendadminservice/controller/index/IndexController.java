package com.jackdaw.jinjobbackendadminservice.controller.index;

import com.jackdaw.jinjobbackendadminservice.controller.ABaseController;
import com.jackdaw.jinjobbackendadminservice.service.StatisticsDataService;
import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("indexController")
@RequestMapping("/index")
public class IndexController extends ABaseController {

    @Resource
    private StatisticsDataService statisticsDataService;

    @PostMapping("/getAllData")
    @GlobalInterceptor
    public ResponseVO getAllData() {
        return getSuccessResponseVO(statisticsDataService.getAllData());
    }

    @PostMapping("/getAppWeekData")
    @GlobalInterceptor
    public ResponseVO getAppWeekData() {
        return getSuccessResponseVO(statisticsDataService.getAppWeekData());
    }

    @PostMapping("/getContentWeekData")
    @GlobalInterceptor
    public ResponseVO getContentWeekData() {
        return getSuccessResponseVO(statisticsDataService.getContentWeekData());
    }
}
