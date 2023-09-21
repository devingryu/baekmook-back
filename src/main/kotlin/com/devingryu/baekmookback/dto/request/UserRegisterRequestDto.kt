package com.devingryu.baekmookback.dto.request

data class UserRegisterRequestDto(
    val id: Long,
    val email: String,
    var password: String,
    val name: String,
    val isLecturer: Boolean,
)