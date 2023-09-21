package com.devingryu.baekmookback.repository

import com.devingryu.baekmookback.entity.Authority
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.UserAuthority
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}

interface UserAuthorityRepository: JpaRepository<UserAuthority, Long> {
    fun findByUserAndAuthority(user: User, authority: Authority): UserAuthority?

    fun existsByUserAndAuthority(user: User, authority: Authority):Boolean
}

interface AuthorityRepository: JpaRepository<Authority, Long> {
    fun findByName(name: String): Authority?

    fun existsByName(email: String): Boolean
}