package com.devingryu.baekmookback.controller

import com.devingryu.baekmookback.dto.BaseException
import com.devingryu.baekmookback.dto.BaseResponse
import com.devingryu.baekmookback.dto.BaseResponse.Companion.toResponse
import com.devingryu.baekmookback.dto.BaseResponseCode
import com.devingryu.baekmookback.dto.request.LoginRequestDto
import com.devingryu.baekmookback.dto.request.RegisterRequestDto
import com.devingryu.baekmookback.dto.request.UserUpdateInfoRequestDto
import com.devingryu.baekmookback.dto.response.LoginResponseDto
import com.devingryu.baekmookback.dto.response.UserResponseDto
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.security.JwtTokenProvider
import com.devingryu.baekmookback.service.AuthorityService
import com.devingryu.baekmookback.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
    private val authorityService: AuthorityService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping("/api/register")
    fun register(@RequestBody req: RegisterRequestDto): ResponseEntity<BaseResponse> {
        if (userService.checkUserByEmail(req.email)) throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        if (userService.checkUserById(req.id)) throw BaseException(BaseResponseCode.DUPLICATE_STUDENT_ID)

        val encodedPw = passwordEncoder.encode(req.password)
        val user = with(req) { User(id, email, encodedPw, name) }
        val created = userService.createUser(user)
        val role = authorityService.getRole(if (req.isLecturer) "LECTURER" else "STUDENT")
        authorityService.addAuthority(created, role)

        return ResponseEntity.ok(BaseResponseCode.OK.toResponse())
    }

    @PostMapping("/api/login")
    fun login(@RequestBody req: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val found = userService.findUserByEmail(req.email)

        if (!passwordEncoder.matches(req.password, found.password))
            throw BaseException(BaseResponseCode.INVALID_PASSWORD)

        val tokens = userService.login(found.id)
        val body = LoginResponseDto(
            token = tokens.accessToken,
            me = UserResponseDto.of(found, true)
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, tokens.refreshTokenCookie)
            .body(body)
    }

    @PostMapping("/api/refresh")
    fun refresh(@CookieValue(JwtTokenProvider.REFRESH_COOKIE_NAME) refreshToken: String?): ResponseEntity<LoginResponseDto> {
        if (refreshToken == null) throw BaseException(BaseResponseCode.REFRESH_TOKEN_INVALID)
        val res = userService.refresh(refreshToken)
        val body = LoginResponseDto(
            token = res.second.accessToken,
            me = UserResponseDto.of(res.first, true)
        )

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, res.second.refreshTokenCookie)
            .body(body)
    }

    @PostMapping("/api/logout")
    fun logout(): ResponseEntity<BaseResponse> {
        val invalidationCookie = userService.logout()

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, invalidationCookie)
            .body(BaseResponseCode.OK.toResponse())
    }

    @PatchMapping("/api/update-user")
    fun updateMemberInfo(
        @RequestBody req: UserUpdateInfoRequestDto,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<UserResponseDto> {
        if (!passwordEncoder.matches(req.currentPassword, user.password))
            throw BaseException(BaseResponseCode.INVALID_PASSWORD)
        if (req.newPassword != null)
            req.newPassword = passwordEncoder.encode(req.newPassword)
        val newUser = userService.update(user.username, req)

        return ResponseEntity.ok()
            .body(UserResponseDto.of(newUser, true))
    }

    @GetMapping("/api/me")
    fun getMe(@AuthenticationPrincipal user: User): ResponseEntity<UserResponseDto> =
        ResponseEntity.ok(UserResponseDto.of(user, true))
}