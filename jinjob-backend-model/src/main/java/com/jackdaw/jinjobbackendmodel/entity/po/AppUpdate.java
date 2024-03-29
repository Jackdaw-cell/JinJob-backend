package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * app发布
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUpdate implements Serializable {


    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新描述
     */
    private String updateDesc;

    /**
     * 更新类型0:全更新 1:局部热更新
     */
    private Integer updateType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0:未发布 1:灰度发布 2:全网发布
     */
    private Integer status;

    /**
     * 灰度设备ID
     */
    private String grayscaleDevice;

    private String[] updateDescArray;

    public String[] getUpdateDescArray() {
        if (!StringTools.isEmpty(updateDesc)) {
            return updateDesc.split("\\|");
        }
        return updateDescArray;
    }


    @Override
    public String toString() {
        return "自增ID:" + (id == null ? "空" : id) + "，版本号:" + (version == null ? "空" : version) + "，更新描述:" + (updateDesc == null ? "空" : updateDesc) + "，更新类型0:全更新 " +
                "1:局部热更新:" + (updateType == null ? "空" : updateType) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime,
                DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:未发布 1:灰度发布 2:全网发布:" + (status == null ? "空" : status) + "，灰度设备ID:" + (grayscaleDevice == null ? "空" : grayscaleDevice);
    }
}
