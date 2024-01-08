package com.jackdaw.jinjobbackendmodel.entity.query;

import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户收藏参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserCollectQuery extends BaseParam {


    /**
     * 收藏ID
     */
    private Integer collectId;

    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 主体ID 问题ID,考题ID,分享文章ID
     */
    private String objectId;

    private String objectIdFuzzy;

    /**
     * 0:分享收藏 1:问题收藏  2:考题收藏
     */
    private Integer collectType;

    /**
     * 收藏时间
     */
    private String collectTime;

    private String collectTimeStart;

    private String collectTimeEnd;

    private List<String> objectIdList;


}
