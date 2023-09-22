package com.devingryu.baekmookback.dto.response

data class LoginResponseDto (
    val token: String,
    val me: MeResponseDto
)