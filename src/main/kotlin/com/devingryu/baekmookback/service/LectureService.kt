package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.entity.lecture.Post
import com.devingryu.baekmookback.entity.lecture.LectureUser
import com.devingryu.baekmookback.entity.lecture.LectureUserId
import com.devingryu.baekmookback.repository.PostRepository
import com.devingryu.baekmookback.repository.LectureRepository
import com.devingryu.baekmookback.repository.LectureUserRepository
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_LECTURER
import com.devingryu.baekmookback.util.AuthorityUtil.ROLE_STUDENT
import com.devingryu.baekmookback.util.AuthorityUtil.hasPermission
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
    private val postRepository: PostRepository,
    private val lectureUserRepository: LectureUserRepository,
) {
    @Transactional
    fun createLecture(name: String, description: String?, user: User): Lecture {
        // Check Permissions
        if (!user.authorities.hasPermission(ROLE_LECTURER)) throw BaseException(BaseResponseCode.ACCESS_DENIED)
        val newLecture = lectureRepository.saveAndFlush(Lecture(name.trim().take(255), description?.trim()?.take(255)))
        lectureUserRepository.save(LectureUser(user, newLecture, true))
        return newLecture
    }

    /** Check First if requesting user has permission to write post */
    fun createPost(lectureId: Long, title: String, content: String, registerer: User): Post {
        // Check Permission
        val lectureUser = lectureUserRepository.findByIdOrNull(LectureUserId(lectureId, registerer.id))
        if (lectureUser == null || !lectureUser.isUserLecturer)
            throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)
        if (title.isBlank() || content.isBlank()) throw BaseException(BaseResponseCode.BAD_REQUEST)
        // Create Post
        return postRepository.save(Post(title.trim().take(255), content.trim(), registerer, lectureUser.lecture))
    }

    fun getPosts(lectureId: Long): List<Post> {
        return postRepository.findAllByLecture_IdOrderByCreatedDateDesc(lectureId)
    }

    fun getRecentPosts(n: Int, page: Int, user: User): Page<Post> {
        val pageRequest = PageRequest.of(page, n)
        return postRepository.findAllByLecture_Users_UserOrderByCreatedDateDesc(pageRequest, user)
    }


    fun getLecture(lectureId: Long): Lecture =
        lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)

    // User를 넘겨주면 해당 유저가 속해있는 것만 검색함
    fun getLectures(n: Int, page: Int, user: User?): Page<Lecture> {
        val pageRequest = PageRequest.of(page, n)
        return if (user != null) lectureRepository.findAllByUsers_UserOrderByNameAsc(pageRequest, user)
        else lectureRepository.findAllByOrderByNameAsc(pageRequest)
    }

    fun enrollStudent(lectureId: Long, user: User): LectureUser {
        val lecture =
            lectureRepository.findByIdOrNull(lectureId) ?: throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)
        if (!user.authorities.hasPermission(ROLE_STUDENT)) throw BaseException(BaseResponseCode.ENROLL_STUDENT_ONLY)

        lectureUserRepository.findById(LectureUserId(lectureId, user.id)).ifPresent {
            throw BaseException(BaseResponseCode.ALREADY_ENROLLED)
        }
        return lectureUserRepository.save(LectureUser(user, lecture, false))
    }

    fun withdrawStudent(lectureId: Long, user: User, studentId: Long) {
        val lectureUser = lectureUserRepository.findByIdOrNull(LectureUserId(lectureId, user.id))
        if (lectureUser == null || !lectureUser.isUserLecturer)
            throw BaseException(BaseResponseCode.LECTURE_NOT_FOUND)
        val student = lectureUserRepository.findById(LectureUserId(lecture = lectureId, user = studentId))
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
        lectureUserRepository.delete(student)
    }
}