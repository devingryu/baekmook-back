package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.lecture.Post
import com.devingryu.baekmookback.util.Extensions.toTimestamp
import java.time.format.DateTimeFormatter

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val creationTime: Long,
    val creationTimeFormatted: String,
    val registerer: UserResponseDto?
) {
    companion object {
        fun of(post: Post) =
            with(post) {
                val formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm")
                PostResponseDto(
                    id,
                    title,
                    content,
                    createdDate.toTimestamp(),
                    createdDate.format(formatter),
                    if (registerer != null) UserResponseDto.of(registerer, false) else null
                )
            }
    }
}