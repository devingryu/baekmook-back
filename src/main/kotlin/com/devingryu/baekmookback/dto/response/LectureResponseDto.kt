package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_LECTURER
import com.devingryu.baekmookback.util.AuthorityUtil.hasPermission

data class LectureResponseDto(
    val id: Long,
    val name: String,
    val description: String?,
    val lecturers: List<UserResponseDto>,
    val mine: Boolean,
) {
    companion object {
        fun of(lecture: Lecture, isMine: Boolean): LectureResponseDto {
            return with(lecture) {
                LectureResponseDto(
                    id,
                    name,
                    description,
                    users.filter { it.user.authorities.hasPermission(ROLE_LECTURER) }
                        .map { UserResponseDto.of(it.user, false) },
                    isMine
                )
            }
        }
    }
}