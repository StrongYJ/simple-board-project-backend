package com.myproject.simpleboard.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

public record ResponseDto<T> (T content) {

}