package com.devingryu.baekmookback.dto.response

import com.devingryu.baekmookback.entity.lecture.Post

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val registerer: UserResponseDto?
) {
    companion object {
        fun of(post: Post) =
            with(post) {
                PostResponseDto(
                    id,
                    title,
                    content,
                    if (registerer != null) UserResponseDto.of(registerer, false) else null
                )
            }
    }
}