package com.jackdaw.jinjobbackendmodel.entity.po;

import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;
import com.jackdaw.jinjobbackendcommon.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 文章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareInfo implements Serializable {


    /**
     * 自增ID
     */
    private Integer shareId;

    /**
     * 标题
     */
    private String title;

    /**
     * 0:无封面 1:横幅  2:小图标
     */
    private Integer coverType;

    /**
     * 封面路径
     */
    private String coverPath;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0:未发布 1:已发布
     */
    private Integer status;

    /**
     * 用户ID
     */
    private String createUserId;

    /**
     * 姓名
     */
    private String createUserName;

    /**
     * 阅读数量
     */
    private Integer readCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 0:内部 1:外部投稿
     */
    private Integer postUserType;

    private Boolean haveCollect;

    private Integer collectId;

    @Override
    public String toString() {
        return "自增ID:" + (shareId == null ? "空" : shareId) + "，标题:" + (title == null ? "空" : title) + "，0:无封面 1:横幅  2:小图标:" + (coverType == null ? "空" : coverType) +
                "，封面路径:" + (coverPath == null ? "空" : coverPath) + "，内容:" + (content == null ? "空" : content) + "，创建时间:" + (createTime == null ? "空" :
                DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:未发布 1:已发布:" + (status == null ? "空" : status) + "，用户ID:" + (createUserId == null ? "空" : createUserId) + "，姓名:" + (createUserName == null ? "空" : createUserName) + "，阅读数量:" + (readCount == null ? "空" : readCount) + "，收藏数:" + (collectCount == null ? "空" : collectCount) + "，0:内部 1:外部投稿:" + (postUserType == null ? "空" : postUserType);
    }
}
