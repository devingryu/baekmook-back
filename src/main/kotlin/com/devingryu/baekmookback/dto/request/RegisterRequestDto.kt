package com.devingryu.baekmookback.dto.request

data class RegisterRequestDto(
    val id: Long,
    val email: String,
    var password: String,
    val name: String,
    val isLecturer: Boolean,
)