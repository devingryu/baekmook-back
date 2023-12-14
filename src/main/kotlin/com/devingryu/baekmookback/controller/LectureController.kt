package com.devingryu.baekmookback.controller

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.dto.request.CreateLectureRequestDto
import com.devingryu.baekmookback.dto.request.CreatePostRequestDto
import com.devingryu.baekmookback.dto.request.JoinLectureRequestDto
import com.devingryu.baekmookback.dto.request.WithdrawLectureRequestDto
import com.devingryu.baekmookback.dto.response.LectureResponseDto
import com.devingryu.baekmookback.dto.response.LecturesResponseDto
import com.devingryu.baekmookback.dto.response.PostResponseDto
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.service.LectureService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/lecture")
class LectureController(
    private val lectureService: LectureService
) {
    @GetMapping("")
    fun getLectures(
        @RequestParam(defaultValue = "20") n: Int,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "false") isMine: Boolean,
        @AuthenticationPrincipal user: User
    ): LecturesResponseDto {
        val lecturePage = lectureService.getLectures(n, page - 1, if (isMine) user else null)
        val totalPages = lecturePage.totalPages
        val lectures = lecturePage.content.map { LectureResponseDto.of(it, user) }
        return LecturesResponseDto(totalPages, lectures)
    }

    @GetMapping("/{id}")
    fun getLecture(@PathVariable("id") id: String, @AuthenticationPrincipal user: User): LectureResponseDto {
        val lectureId = id.toLongOrNull() ?: throw BaseException(BaseResponseCode.BAD_REQUEST)
        val lecture = lectureService.getLecture(lectureId)
        return LectureResponseDto.of(lecture, user)
    }

    @GetMapping("/{id}/posts")
    fun getPosts(@PathVariable("id") id: String, @AuthenticationPrincipal user: User): List<PostResponseDto> {
        val lectureId = id.toLongOrNull() ?: throw BaseException(BaseResponseCode.BAD_REQUEST)
        val lecture = lectureService.getLecture(lectureId)
        // 자신이 속한 강의가 아닐 경우 존재하지 않는 강의로 표시
        if (!lecture.users.any { user == it.user })
            throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)
        return lecture.posts.map { PostResponseDto.of(it) }
    }

    @GetMapping("/recent-posts")
    fun getRecentPosts(
        @RequestParam(defaultValue = "20") n: Int,
        @RequestParam(defaultValue = "1") page: Int,
        @AuthenticationPrincipal user: User
    ): List<PostResponseDto> {
        val posts = lectureService.getRecentPosts(n, page - 1, user).content
        return posts.map { PostResponseDto.of(it, true) }
    }

    @PostMapping("/create")
    fun createLecture(
        @RequestBody req: CreateLectureRequestDto,
        @AuthenticationPrincipal user: User
    ): LectureResponseDto {
        val lecture = lectureService.createLecture(req.name, req.description, user)
        return LectureResponseDto.of(lecture, isInvolved = true, isLecturer = true)
    }

    @PostMapping("/write-post")
    fun createPost(@RequestBody req: CreatePostRequestDto, @AuthenticationPrincipal user: User): PostResponseDto {
        val post = lectureService.createPost(req.lectureId, req.title, req.content, user)
        return PostResponseDto.of(post)
    }

    @PostMapping("/join")
    fun joinLecture(@RequestBody req: JoinLectureRequestDto, @AuthenticationPrincipal user: User): BaseResponse {
        lectureService.enrollStudent(req.lectureId, user)
        return BaseResponseCode.OK.toResponse()
    }

    @PostMapping("/withdraw")
    fun withdrawLecture(@RequestBody req: WithdrawLectureRequestDto, @AuthenticationPrincipal user: User): BaseResponse {
        lectureService.withdrawStudent(req.lectureId, user, req.userId)
        return BaseResponseCode.OK.toResponse()
    }
}