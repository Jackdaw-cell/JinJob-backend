package com.jackdaw.jinjobbackendmodel.entity.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoopDataVO<T> {
    private T pre;
    private T current;
    private T next;

}
