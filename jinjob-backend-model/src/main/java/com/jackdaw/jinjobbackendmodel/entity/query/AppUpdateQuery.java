package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * app发布参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUpdateQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 版本号
	 */
	private String version;

	private String versionFuzzy;

	/**
	 * 更新描述
	 */
	private String updateDesc;

	private String updateDescFuzzy;

	/**
	 * 更新类型0:全更新 1:局部热更新
	 */
	private Integer updateType;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 0:未发布 1:灰度发布 2:全网发布
	 */
	private Integer status;

	/**
	 * 灰度设备ID
	 */
	private String grayscaleDevice;

	private String grayscaleDeviceFuzzy;

}
