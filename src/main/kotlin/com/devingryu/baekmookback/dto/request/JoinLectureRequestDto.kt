package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class JoinLectureRequestDto(
    @JsonProperty(value="lectureId")
    val lectureId: Long,
)