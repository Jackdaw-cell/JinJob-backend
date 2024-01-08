package com.jackdaw.jinjobbackendmodel.entity.dto.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfoDto {

    private String userId;

    private String avatar;

    private String nickName;
    /**
     * OJ通过数量
     */
    private Integer ojAcSum;

    /**
     * 考试通过数量
     */
    private Integer examAcSum;

    /**
     * 考试通过均分
     */
    private Float examAcScore;
    /**
     * 标签
     */
    private String tags;

    /**
     * 简介
     */
    private String description;

    /**
     * 背景
     */
    private String background;
}
