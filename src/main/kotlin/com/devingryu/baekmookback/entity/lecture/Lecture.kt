package com.devingryu.baekmookback.entity.lecture

import jakarta.persistence.*

@Entity
class Lecture (
    @Column(nullable = false)
    var name: String,
    var description: String?,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    val posts: List<Post> = listOf()

    @OneToMany(mappedBy = "lecture", fetch = FetchType.LAZY)
    val users: MutableSet<LectureUser> = hashSetOf()

    var isPublic: Boolean = true
}