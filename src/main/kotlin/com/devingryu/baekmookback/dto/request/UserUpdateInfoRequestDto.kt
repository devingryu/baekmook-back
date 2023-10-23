package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserUpdateInfoRequestDto(
    val currentPassword: String,
    val newName: String?,
    var newPassword: String?,
)