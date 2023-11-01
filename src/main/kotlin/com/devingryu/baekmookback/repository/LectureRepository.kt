package com.devingryu.baekmookback.repository

import com.devingryu.baekmookback.entity.lecture.Lecture
import com.devingryu.baekmookback.entity.lecture.Post
import com.devingryu.baekmookback.entity.lecture.LectureUser
import com.devingryu.baekmookback.entity.lecture.LectureUserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository: JpaRepository<Lecture, Long> {
    fun findAllByOrderByNameAsc(pageable: Pageable): Page<Lecture>
}

interface postRepository: JpaRepository<Post, Long> {}

interface LectureUserRepository: JpaRepository<LectureUser, LectureUserId> {}