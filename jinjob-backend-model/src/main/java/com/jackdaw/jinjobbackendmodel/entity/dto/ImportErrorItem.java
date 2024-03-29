package com.jackdaw.jinjobbackendmodel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorItem {
    private Integer rowNum;
    private List<String> errorItemList;

}
