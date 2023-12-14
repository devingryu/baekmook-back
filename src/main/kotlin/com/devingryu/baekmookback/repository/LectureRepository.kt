package com.devingryu.baekmookback.repository

import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.entity.lecture.Post
import com.devingryu.baekmookback.entity.lecture.LectureUser
import com.devingryu.baekmookback.entity.lecture.LectureUserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository: JpaRepository<Lecture, Long> {
    fun findAllByOrderByNameAsc(pageable: Pageable): Page<Lecture>
    fun findAllByUsers_UserOrderByNameAsc(pageable: Pageable, user: User): Page<Lecture>
}

interface PostRepository: JpaRepository<Post, Long> {
    fun findAllByLecture_Users_UserOrderByCreatedDateDesc(pageable: Pageable, user: User): Page<Post>
}

interface LectureUserRepository: JpaRepository<LectureUser, LectureUserId> {}