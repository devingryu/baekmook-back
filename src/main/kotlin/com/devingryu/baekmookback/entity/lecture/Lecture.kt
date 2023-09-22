package com.devingryu.baekmookback.entity.lecture

import jakarta.persistence.*

@Entity
class Lecture (
    @Column(nullable = false)
    val name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val posts: List<LecturePost> = listOf()
}