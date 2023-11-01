package com.devingryu.baekmookback.dto.response

data class LecturesResponseDto(
    val totalPages: Int,
    val lectures: List<LectureResponseDto>
)