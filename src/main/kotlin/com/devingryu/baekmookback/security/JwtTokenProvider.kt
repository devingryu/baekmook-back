package com.devingryu.baekmookback.security

import com.devingryu.baekmookback.common.TokenInfo
import com.devingryu.baekmookback.service.UserDetailsService
import com.devingryu.baekmookback.util.Extensions.toDate
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey
import kotlin.Exception

@Component
class JwtTokenProvider(
    private val userDetailsService: UserDetailsService,
    @Value("\${baekmook.jwt.secret}")
    private val b64Key: String,
    @Value("\${baekmook.jwt.access-time-ms}")
    private val accessTokenValidTime: Long,
    @Value("\${baekmook.jwt.refresh-time-ms}")
    private val refreshTokenValidTime: Long,
) {

    private var secretKey: SecretKey? = null

    @PostConstruct
    protected fun init() {
        val rawKey = Decoders.BASE64.decode(b64Key)
        secretKey = Keys.hmacShaKeyFor(rawKey)
    }

    fun createAllToken(userPk: String): TokenInfo {
        val accessToken = createToken(userPk, accessTokenValidTime, TOKEN_TYPE_ACCESS)
        val refreshToken = createToken(userPk, refreshTokenValidTime, TOKEN_TYPE_REFRESH)
        val refreshCookie = createRefreshCookie(refreshToken.first)

        return TokenInfo(
            accessToken = accessToken.first,
            accessExpiryDate = accessToken.second,
            refreshToken = refreshToken.first,
            refreshExpiryDate = refreshToken.second,
            refreshTokenCookie = refreshCookie
        )
    }

    fun createToken(userPk: String, validTime: Long, tokenType: String): Pair<String, Long> {
        val claims = Jwts.claims().setSubject(userPk)
        claims["tokenType"] = tokenType
        val now = LocalDateTime.now().toDate()
        val expiration = Date(now.time + validTime)
        val token = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()

        return Pair(token, expiration.time)
    }

    private fun createRefreshCookie(refreshToken: String): String {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
            .httpOnly(true)
//            .secure(true)
            .path("/")
            .maxAge(refreshTokenValidTime / 1000L)
            .build().toString()
    }

    fun createInvalidationCookie(): String {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
            .httpOnly(true)
//            .secure(true)
            .path("/")
            .maxAge(0)
            .build().toString()
    }


    /** Retrieves authentication information from access token. Returns null if invalid. */
    fun getAuthentication(token: String): Authentication? {
        return try {
            val parsed = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            val userDetails = userDetailsService.loadMemberByEmail(parsed.body.subject)

            if (parsed.body["tokenType"]!! == TOKEN_TYPE_ACCESS)
                UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities())
            else null
        } catch (e: Exception) {
            null
        }
    }

    /** Retrieves userPk(email) from refresh token. Returns null if invalid. */
    fun getUserPkFromRefreshToken(refreshToken: String): String? {
        return try {
            val parsed = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken)

            if (parsed.body["tokenType"]!! == TOKEN_TYPE_REFRESH)
                parsed.body.subject
            else null
        } catch (e: Exception) {
            null
        }
    }

    fun resolveAccessToken(request: HttpServletRequest): String?
    = request.getHeader("Authorization")

    fun validateToken(jwtToken: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken)
            !claims.body.expiration.before(LocalDateTime.now().toDate())
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        const val TOKEN_TYPE_ACCESS = "tok-acc"
        const val TOKEN_TYPE_REFRESH = "tok-ref"

        const val REFRESH_COOKIE_NAME = "baekmook_refresh_token"
    }
}