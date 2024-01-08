package com.jackdaw.jinjobbackendadminservice.service;

import com.jackdaw.jinjobbackendmodel.entity.dto.StatisticsDataDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.StatisticsDataWeekDto;

import java.util.List;

public interface StatisticsDataService {
    List<StatisticsDataDto> getAllData();

    StatisticsDataWeekDto getAppWeekData();

    StatisticsDataWeekDto getContentWeekData();
}
