package com.devingryu.baekmookback.entity.lecture

import com.devingryu.baekmookback.entity.User
import jakarta.persistence.*

@Entity
class Post(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val registerer: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1
}