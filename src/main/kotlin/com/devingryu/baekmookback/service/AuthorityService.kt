package com.devingryu.baekmookback.service

import com.devingryu.baekmookback.entity.Authority
import com.devingryu.baekmookback.entity.User
import com.devingryu.baekmookback.entity.UserAuthority
import com.devingryu.baekmookback.repository.AuthorityRepository
import com.devingryu.baekmookback.repository.UserAuthorityRepository
import com.devingryu.baekmookback.util.AuthorityUtil
import org.springframework.stereotype.Service

@Service
class AuthorityService(
    private val authorityRepository: AuthorityRepository,
    private val memberAuthorityRepository: UserAuthorityRepository
) {

    fun getAuthority(type: String, permission: String): Authority {
        val authorityName = AuthorityUtil.getAuthorityString(type, permission)
        return authorityRepository.findByName(authorityName) ?: authorityRepository.save(Authority(authorityName))
    }

    fun getRole(name: String): Authority {
        val roleName = AuthorityUtil.getRoleString(name)
        return authorityRepository.findByName(roleName) ?: authorityRepository.save(Authority(roleName))
    }

    fun addAuthority(user: User, authority: Authority) {
        if(!memberAuthorityRepository.existsByUserAndAuthority(user, authority)) {
            memberAuthorityRepository.save(UserAuthority(user, authority))
        }
    }

    fun removeAuthority(user: User, authority: Authority) {
        val ma = memberAuthorityRepository.findByUserAndAuthority(user, authority)
        if (ma != null)
            memberAuthorityRepository.delete(ma)
    }

    companion object {

    }
}