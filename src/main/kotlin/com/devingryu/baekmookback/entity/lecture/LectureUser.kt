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
class LectureUser (
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    val lecture: Lecture
)

class LectureUserId(private val lecture: Long = -1, private val user: Long = -1): Serializable