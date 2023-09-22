package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.util.Extensions.toTimestamp
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class MeResponseDto(
    val email: String,
    val name: String,
    val role: String,
    @JsonProperty("created_date")
    val createdDate: Long
) {
    companion object {
        fun fromUser(user: User) = with(user) {
            val authorities = user.authorities.map { it.authority.name }
            val role = when {
                "ROLE_MASTER" in authorities -> "master"
                "ROLE_LECTURER" in authorities -> "lecturer"
                "ROLE_STUDENT" in authorities -> "student"
                else -> "unknown"
            }
            MeResponseDto(username, name, role, createdDate.toTimestamp())
        }
    }
}