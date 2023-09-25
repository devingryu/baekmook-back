package com.devingryu.baekmookback.entity.lecture

import com.devingryu.baekmookback.entity.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serializable

@Entity
@IdClass(LectureUserId::class)
class LectureUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null

    @Id
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture? = null
}

class LectureUserId(private val lecture: Long, private val user: Long): Serializable