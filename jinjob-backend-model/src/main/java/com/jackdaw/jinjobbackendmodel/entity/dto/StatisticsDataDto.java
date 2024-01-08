package com.jackdaw.jinjobbackendmodel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDataDto {
    private String statisticsName;
    private Integer count;
    private Integer preCount;
    private List<Integer> listData;

}
