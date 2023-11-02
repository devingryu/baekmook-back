package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_LECTURER
import com.devingryu.baekmookback.util.AuthorityUtil.hasPermission

data class LectureResponseDto(
    val id: Long,
    val name: String,
    val description: String?,
    val isInvolved: Boolean?,
    val isLecturer: Boolean?,
    val lecturers: List<UserResponseDto>,
    val students: List<UserResponseDto>?,
) {
    companion object {
        fun of(lecture: Lecture, user: User): LectureResponseDto {
            val isInvolved = lecture.users.find { it.user == user }
            val isLecturer = isInvolved?.isUserLecturer
            return of(lecture, isInvolved != null, isLecturer == true)
        }

        fun of(lecture: Lecture, isInvolved: Boolean, isLecturer: Boolean): LectureResponseDto {
            val partition = lecture.users.partition { it.isUserLecturer }
            return with(lecture) {
                LectureResponseDto(
                    id,
                    name,
                    description,
                    isInvolved,
                    isLecturer,
                    partition.first
                        .map { UserResponseDto.of(it.user, false) },
                    if (isLecturer) partition.second.map { UserResponseDto.of(it.user, false) } else null
                )
            }
        }
    }
}