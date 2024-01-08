package com.jackdaw.jinjobbackendmodel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserAppDto {
    private String userId;
    private String nickName;
    private String sessionId;

}
