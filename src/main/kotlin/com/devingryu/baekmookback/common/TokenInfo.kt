package com.devingryu.baekmookback.common

class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenCookie: String,
    val accessExpiryDate: Long,
    val refreshExpiryDate: Long,
)