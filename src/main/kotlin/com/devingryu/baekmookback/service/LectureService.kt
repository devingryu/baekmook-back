package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.entity.lecture.Post
import com.devingryu.baekmookback.entity.lecture.LectureUser
import com.devingryu.baekmookback.repository.postRepository
import com.devingryu.baekmookback.repository.LectureRepository
import com.devingryu.baekmookback.repository.LectureUserRepository
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_LECTURER
import com.devingryu.baekmookback.util.AuthorityUtil.hasPermission
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
    private val postRepository: postRepository,
    private val lectureUserRepository: LectureUserRepository,
) {
    fun createLecture(name: String, description: String?, user: User): Lecture {
        // Check Permissions
        if (!user.authorities.hasPermission(ROLE_LECTURER)) throw BaseException(BaseResponseCode.ACCESS_DENIED)
        
        return lectureRepository.save(Lecture(name, description).apply { users.add(LectureUser(user, this, true)) })
    }

    /** Check First if requesting user has permission to write post */
    fun createPost(lectureId: Long, title: String, content: String, registerer: User): Post {
        // Check Lecture
        val lecture =
            lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

        // Check Permission
        if (!(lecture.users.any { registerer.id == it.user.id && it.isUserLecturer }))
            throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

        // Create Post
        return postRepository.save(Post(title.trim(), content.trim(), registerer, lecture))
    }

    fun getLecture(lectureId: Long): Lecture =
        lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

    fun getLectures(n: Int, page: Int, user: User?): Page<Lecture> {
        val pageRequest = PageRequest.of(page, n)
        return if (user != null) lectureRepository.findAllByUsers_UserOrderByNameAsc(pageRequest, user)
        else lectureRepository.findAllByOrderByNameAsc(pageRequest)
    }
}