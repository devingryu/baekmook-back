package com.devingryu.baekmookback.entity.lecture

import jakarta.persistence.*

@Entity
class LecturePost(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    val lecture: Lecture? = null
}