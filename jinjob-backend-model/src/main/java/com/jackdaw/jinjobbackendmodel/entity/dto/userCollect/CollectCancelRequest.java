package com.jackdaw.jinjobbackendmodel.entity.dto.userCollect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectCancelRequest {
    private String objectId;
    private Integer collectType;
}
