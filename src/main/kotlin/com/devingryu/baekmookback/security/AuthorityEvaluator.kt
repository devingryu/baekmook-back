package com.devingryu.baekmookback.security


import com.devingryu.baekmookback.util.AuthorityUtil
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import java.io.Serializable


class AuthorityEvaluator : PermissionEvaluator {
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if (authentication == null || targetDomainObject == null || permission !is String) return false
        val targetType: String = targetDomainObject::class.simpleName ?: return false

        return hasPrivilege(authentication, targetType, permission.toString().uppercase())
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        if (authentication == null || targetType == null || permission !is String) return false
        return hasPrivilege(
            authentication,
            targetType,
            permission.toString()
        )
    }

    private fun hasPrivilege(auth: Authentication, targetType: String, permission: String): Boolean {
        val authString = AuthorityUtil.getAuthorityString(targetType, permission)
        auth.authorities.forEach { grantedAuth ->
            if (grantedAuth.authority == authString || grantedAuth.authority == AuthorityUtil.ROLE_MASTER) {
                return true
            }
        }
        return false
    }
}