package com.devingryu.baekmookback.entity.lecture

import jakarta.persistence.*

@Entity
class Lecture (
    @Column(nullable = false)
    val name: String,
    val description: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val posts: List<Post> = listOf()

    @OneToMany(mappedBy = "lecture", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val users: MutableSet<LectureUser> = hashSetOf()
}