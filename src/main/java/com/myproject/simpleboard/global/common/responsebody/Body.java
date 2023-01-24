package com.myproject.simpleboard.global.common.responsebody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Body {
    private Object data;
}
