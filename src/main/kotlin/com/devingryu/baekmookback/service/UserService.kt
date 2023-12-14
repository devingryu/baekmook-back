package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.common.TokenInfo
import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.dto.request.UserUpdateInfoRequestDto
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.repository.UserRepository
import com.devingryu.baekmookback.security.JwtTokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Service
class UserService(private val userRepository: UserRepository, private val jwtTokenProvider: JwtTokenProvider) {

    @Throws(BaseException::class)
    fun findUserByEmail(email: String): User
    = userRepository.findByEmail(email) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)

    fun checkUserByEmail(email: String): Boolean
    = userRepository.existsByEmail(email)

    @Throws(BaseException::class)
    fun findUserById(id: Long): User
            = userRepository.findByIdOrNull(id) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)

    fun checkUserById(id: Long): Boolean
            = userRepository.existsById(id)

    @Throws(BaseException::class)
    fun findUserByStudentId(id: String): User
            = userRepository.findByStudentId(id) ?: throw BaseException(BaseResponseCode.USER_NOT_FOUND)

    fun checkUserByStudentId(id: String): Boolean
            = userRepository.existsByStudentId(id)

    fun createUser(user: User): User {
        return userRepository.save(user)
    }


    fun login(userPk: Long): TokenInfo
    = jwtTokenProvider.createAccessToken(userPk.toString())

    fun refresh(token: String): Pair<User, TokenInfo> {
        if (!jwtTokenProvider.validateToken(token)) throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)
        val userPk = jwtTokenProvider.getUserPkFromRefreshToken(token)
            ?: throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)
        val tokens = jwtTokenProvider.createAccessToken(userPk)
        val user = try {
            userRepository.findById(userPk.toLong()).orElseThrow()
        } catch (e: Exception) {
            throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)
        }

        return user to tokens
    }

    @Transactional(readOnly = false)
    fun update(userPk: String, req: UserUpdateInfoRequestDto): User {
        val found = userRepository.findByEmail(userPk)
            ?: throw BaseException(BaseResponseCode.INTERNAL_SERVER_ERROR)
        if (req.newName != null)
            found.name = req.newName
        req.newPassword?.let {
            found.password = it
        }
        return found
    }
}