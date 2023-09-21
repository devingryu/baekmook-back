package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserRefreshRequestDto(
    @JsonProperty("refresh_token")
    val refreshToken: String
)