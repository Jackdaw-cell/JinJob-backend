package com.jackdaw.jinjobbackendmodel.entity.vo;

import cn.hutool.json.JSONUtil;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionInfo;
import com.jackdaw.jinjobbackendmodel.entity.dto.ojQuestion.JudgeConfig;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppUserInfoVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 * @TableName question
 */
@Data
public class OjQuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题用例（json 数组）
     */
    private String judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private String userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 模板方法
     */
    private String submitMethod;

    /**
     * 创建题目人的信息
     */
    private AppUserInfoVO userVO;

    /**
     * 包装类转对象
     *
     * @param ojQuestionVO
     * @return
     */
    public static OjQuestionInfo voToObj(OjQuestionVO ojQuestionVO) {
        if (ojQuestionVO == null) {
            return null;
        }
        OjQuestionInfo ojQuestionInfo = new OjQuestionInfo();
        BeanUtils.copyProperties(ojQuestionVO, ojQuestionInfo);
        List<String> tagList = ojQuestionVO.getTags();
        if (tagList != null) {
            ojQuestionInfo.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudgeConfig = ojQuestionVO.getJudgeConfig();
        if (voJudgeConfig != null) {
            ojQuestionInfo.setJudgeConfig(JSONUtil.toJsonStr(voJudgeConfig));
        }
        return ojQuestionInfo;
    }

    /**
     * 对象转包装类
     *
     * @param ojQuestionInfo
     * @return
     */
    public static OjQuestionVO objToVo(OjQuestionInfo ojQuestionInfo) {
        if (ojQuestionInfo == null) {
            return null;
        }
        OjQuestionVO ojQuestionVO = new OjQuestionVO();
        BeanUtils.copyProperties(ojQuestionInfo, ojQuestionVO);
        List<String> tagList = JSONUtil.toList(ojQuestionInfo.getTags(), String.class);
        ojQuestionVO.setTags(tagList);
        String judgeConfigStr = ojQuestionInfo.getJudgeConfig();
        ojQuestionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return ojQuestionVO;
    }

    private static final long serialVersionUID = 1L;
}