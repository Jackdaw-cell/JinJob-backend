package com.jackdaw.jinjobbackendmodel.entity.vo;

import cn.hutool.json.JSONUtil;
import com.jackdaw.jinjobbackendmodel.entity.dto.codesandbox.JudgeInfo;
import com.jackdaw.jinjobbackendmodel.entity.po.OjQuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交封装类
 * @TableName question
 */
@Data
public class OjQuestionSubmitVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

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
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 对应题目信息
     */
    private OjQuestionVO ojQuestionVO;

    /**
     * 包装类转对象
     *
     * @param ojQuestionSubmitVO
     * @return
     */
    public static OjQuestionSubmit voToObj(OjQuestionSubmitVO ojQuestionSubmitVO) {
        if (ojQuestionSubmitVO == null) {
            return null;
        }
        OjQuestionSubmit ojQuestionSubmit = new OjQuestionSubmit();
        BeanUtils.copyProperties(ojQuestionSubmitVO, ojQuestionSubmit);
        JudgeInfo judgeInfoObj = ojQuestionSubmitVO.getJudgeInfo();
        if (judgeInfoObj != null) {
            ojQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoObj));
        }
        return ojQuestionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param ojQuestionSubmit
     * @return
     */
    public static OjQuestionSubmitVO objToVo(OjQuestionSubmit ojQuestionSubmit) {
        if (ojQuestionSubmit == null) {
            return null;
        }
        OjQuestionSubmitVO ojQuestionSubmitVO = new OjQuestionSubmitVO();
        BeanUtils.copyProperties(ojQuestionSubmit, ojQuestionSubmitVO);
        String judgeInfoStr = ojQuestionSubmit.getJudgeInfo();
        ojQuestionSubmitVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return ojQuestionSubmitVO;
    }

    private static final long serialVersionUID = 1L;
}