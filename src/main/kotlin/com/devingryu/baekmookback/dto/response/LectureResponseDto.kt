package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_LECTURER
import com.devingryu.baekmookback.util.AuthorityUtil.hasPermission

data class LectureResponseDto(
    val id: Long,
    val name: String,
    val description: String?,
    val lecturers: List<UserResponseDto>,
    val isInvolved: Boolean,
    val isLecturer: Boolean,
) {
    companion object {
        fun of(lecture: Lecture, user: User): LectureResponseDto {
            val isInvolved = lecture.users.any { it.user == user }
            val isLecturer = user.authorities.hasPermission(ROLE_LECTURER)
            return of(lecture, isInvolved, isInvolved && isLecturer)
        }
        fun of(lecture: Lecture, isInvolved: Boolean, isLecturer: Boolean): LectureResponseDto {
            return with(lecture) {
                LectureResponseDto(
                    id,
                    name,
                    description,
                    users.filter { it.user.authorities.hasPermission(ROLE_LECTURER) }
                        .map { UserResponseDto.of(it.user, false) },
                    isInvolved,
                    isLecturer
                )
            }
        }
    }
}