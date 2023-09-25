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

        // Create Lecture
        val newLecture = lectureRepository.save(Lecture(name, description))

        // Add Lecturer
        val lu = lectureUserRepository.save(LectureUser(user, newLecture))
        return newLecture.apply { users.add(lu) }
    }

    /** Check First if requesting user has permission to write post */
    fun createPost(lectureId: Long, title: String, content: String, registerer: User): Post {
        // Check Lecture
        val lecture =
            lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

        // Check Permission
        if (!(registerer.authorities.hasPermission(ROLE_LECTURER) && lecture.users.any { registerer.id == it.user.id }))
            throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

        // Create Post
        return postRepository.save(Post(title.trim(), content.trim(), registerer, lecture))
    }

    fun getLecture(lectureId: Long): Lecture =
        lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

}