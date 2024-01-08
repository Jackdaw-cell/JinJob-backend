package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * app轮播参数
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppCarouselQuery extends BaseParam {


	/**
	 * 自增ID
	 */
	private Integer carouselId;

	/**
	 * 图片
	 */
	private String imgPath;

	private String imgPathFuzzy;

	/**
	 * 0:分享1:问题 2:考题 3:外部连接
	 */
	private Integer objectType;

	/**
	 * 文章ID
	 */
	private String objectId;

	private String objectIdFuzzy;

	/**
	 * 外部连接
	 */
	private String outerLink;

	private String outerLinkFuzzy;

	/**
	 * 排序
	 */
	private Integer sort;


}
