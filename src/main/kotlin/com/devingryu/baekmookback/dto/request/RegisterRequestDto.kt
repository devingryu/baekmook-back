package com.devingryu.baekmookback.dto.request

data class RegisterRequestDto(
    val studentId: String,
    val email: String,
    var password: String,
    val name: String,
    val isLecturer: Boolean,
)