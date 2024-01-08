package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备信息参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDeviceQuery extends BaseParam {


	/**
	 * 设备ID
	 */
	private String deviceId;

	private String deviceIdFuzzy;

	/**
	 * 手机品牌
	 */
	private String deviceBrand;

	private String deviceBrandFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 最后使用时间
	 */
	private String lastUseTime;

	private String lastUseTimeStart;

	private String lastUseTimeEnd;

	/**
	 * ip
	 */
	private String ip;

	private String ipFuzzy;


}
