package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class WithdrawLectureRequestDto(
    @JsonProperty(value="lectureId")
    val lectureId: Long,
    val userId: Long,
)