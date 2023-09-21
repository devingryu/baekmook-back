package com.devingryu.baekmookback.dto.request

data class UserLoginRequestDto(
    val email: String,
    val password: String,
)