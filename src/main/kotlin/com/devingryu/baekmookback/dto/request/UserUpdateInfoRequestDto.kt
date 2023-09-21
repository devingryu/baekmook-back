package com.devingryu.baekmookback.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserUpdateInfoRequestDto(
    @JsonProperty("current_password")
    val currentPassword: String,
    @JsonProperty("new_name")
    val newName: String?,
    @JsonProperty("new_password")
    var newPassword: String?,
)