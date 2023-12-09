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
    val roleTranslated: String?,
    val createdDate: Long?
) {
    companion object {
        fun of(user: User, isMe: Boolean) = with(user) {
            if (isMe) {
                val authorities = user.authorities.map { it.authority }
                val role = when {
                    AuthorityUtil.ROLE_MASTER in authorities -> "master" to "관리자"
                    AuthorityUtil.ROLE_LECTURER in authorities -> "lecturer" to "교수자"
                    AuthorityUtil.ROLE_STUDENT in authorities -> "student" to "학생"
                    else -> "unknown" to "알 수 없음"
                }
                UserResponseDto(id, studentId, username, name, role.first, role.second, createdDate.toTimestamp())
            } else {
                UserResponseDto(id, studentId, username, name, null, null, null)
            }
        }
    }
}