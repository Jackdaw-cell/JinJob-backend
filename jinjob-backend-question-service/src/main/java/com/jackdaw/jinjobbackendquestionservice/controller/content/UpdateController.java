package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUpdate;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppUpdateVO;
import com.jackdaw.jinjobbackendmodel.enums.AppUpdateTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.RequestFrequencyTypeEnum;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.AppUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;

@RestController
@RequestMapping("/update")
public class UpdateController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    @Resource
    private AppConfig appConfig;

    @Resource
    private AppUpdateService appUpdateService;

    @PostMapping("/checkVersion")
    @GlobalInterceptor
    public BaseResponse<? extends Object> checkVersion(@RequestParam String appVersion,
                                     @VerifyParam(required = true) @RequestParam String deviceId) {
        if (StringTools.isEmpty(appVersion)) {
//            return getSuccessResponseVO(null);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AppUpdate appUpdate = appUpdateService.getLatestUpdate(appVersion, deviceId);
        if (appUpdate == null) {
//            return getSuccessResponseVO(null);
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        AppUpdateVO updateVO = CopyTools.copy(appUpdate, AppUpdateVO.class);

        AppUpdateTypeEnum typeEnum = AppUpdateTypeEnum.getByType(appUpdate.getUpdateType());
        File file = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER + appUpdate.getId() + typeEnum.getSuffix());
        updateVO.setSize(file.length());
        updateVO.setUpdateList(Arrays.asList(appUpdate.getUpdateDescArray()));
//        return getSuccessResponseVO(updateVO);
        return ResultUtils.success(updateVO);
    }

    @PostMapping("/download")
    @GlobalInterceptor(frequencyType = RequestFrequencyTypeEnum.DAY, reqeustFrequencyThreshold = 10)
    public void download(HttpServletResponse response,
                         @VerifyParam(required = true) @RequestParam Integer id) {
        OutputStream out = null;
        InputStream in = null;
        try {
            AppUpdate appUpdate = appUpdateService.getAppUpdateById(id);
            AppUpdateTypeEnum typeEnum = AppUpdateTypeEnum.getByType(appUpdate.getUpdateType());
            String fileName = appConfig.getAppName() + "." + appUpdate.getVersion() + typeEnum.getSuffix();
            File file = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER + appUpdate.getId() + typeEnum.getSuffix());
            if (!file.exists()) {
                return;
            }
            response.setContentType("application/x-msdownload; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            response.setContentLengthLong(file.length());
            //读取文件
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }
}
