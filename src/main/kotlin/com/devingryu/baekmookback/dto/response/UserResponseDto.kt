package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.util.AuthorityUtil
import com.devingryu.baekmookback.util.Extensions.toTimestamp
import com.fasterxml.jackson.annotation.JsonProperty

class UserResponseDto(
    val id: Long,
    val studentId: String,
    val email: String,
    val name: String,
    val role: String?,
    val createdDate: Long?
) {
    companion object {
        fun of(user: User, isMe: Boolean) = with(user) {
            if (isMe) {
                val authorities = user.authorities.map { it.authority }
                val role = when {
                    AuthorityUtil.ROLE_MASTER in authorities -> "master"
                    AuthorityUtil.ROLE_LECTURER in authorities -> "lecturer"
                    AuthorityUtil.ROLE_STUDENT in authorities -> "student"
                    else -> "unknown"
                }
                UserResponseDto(id, studentId, username, name, role, createdDate.toTimestamp())
            } else {
                UserResponseDto(id, studentId, username, name, null, null)
            }
        }
    }
}