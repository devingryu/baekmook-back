package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.common.TokenInfo
import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.dto.request.UserLoginRequestDto
import com.devingryu.baekmookback.dto.request.UserRegisterRequestDto
import com.devingryu.baekmookback.dto.request.UserUpdateInfoRequestDto
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.repository.UserRepository
import com.devingryu.baekmookback.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Service
class UserService(private val UserRepository: UserRepository, private val jwtTokenProvider: JwtTokenProvider) {

    @Throws(BaseException::class)
    fun findUserByEmail(email: String): User
    = UserRepository.findByEmail(email) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)

    fun checkUserByEmail(email: String): Boolean
    = UserRepository.existsByEmail(email)

    fun createUser(user: User): User
    = UserRepository.save(user)


    fun login(req: UserLoginRequestDto): TokenInfo
    = jwtTokenProvider.createAllToken(req.email)

    fun logout(): String
    = jwtTokenProvider.createInvalidationCookie()

    fun refresh(token: String): TokenInfo {
        if (!jwtTokenProvider.validateToken(token)) throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)
        val userPk = jwtTokenProvider.getUserPkFromRefreshToken(token)
            ?: throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)

        return jwtTokenProvider.createAllToken(userPk)
    }

    @Transactional(readOnly = false)
    fun update(userPk: String, req: UserUpdateInfoRequestDto) {
        val found = UserRepository.findByEmail(userPk)
            ?: throw BaseException(BaseResponseCode.INTERNAL_SERVER_ERROR)
        if (req.newName != null)
            found.name = req.newName
        req.newPassword?.let {
            found.password = it
        }
    }
}