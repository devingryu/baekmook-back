package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.repository.UserRepository
import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.entity.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw BaseException(BaseResponseCode.BAD_REQUEST)
        return userRepository.findByEmail(username) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)
    }

    fun loadMemberByEmail(email: String?): User {
        if (email == null) throw BaseException(BaseResponseCode.BAD_REQUEST)
        return userRepository.findByEmail(email) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)
    }

}