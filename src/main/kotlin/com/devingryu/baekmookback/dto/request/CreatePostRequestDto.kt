package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreatePostRequestDto(
    @JsonProperty("lecture_id")
    val lectureId: Long,
    val title: String,
    val content: String,
)