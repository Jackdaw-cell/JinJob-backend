package com.jackdaw.jinjobbackendmodel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDataWeekDto {
    private List<String> dateList;
    private List<StatisticsDataDto> itemDataList;

    public List<String> getDateList() {
        return dateList;
    }

}
