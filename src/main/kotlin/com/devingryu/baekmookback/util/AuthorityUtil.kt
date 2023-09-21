package com.devingryu.baekmookback.util

import org.springframework.security.core.GrantedAuthority

object AuthorityUtil {
    fun getAuthorityString(targetType: String, permission: String): String {
        val t = targetType.uppercase()
        val p = permission.uppercase()
        return "${t}_${p}_$PRIV_POSTFIX"
    }

    fun getRoleString(roleName: String): String {
        val n = roleName.uppercase()
        return "${ROLE_PREFIX}_$n"
    }

    fun Iterable<GrantedAuthority>.hasPermission(target: String, includeMaster: Boolean = true): Boolean {
        if (!includeMaster)
            forEach {
                if (it.authority == target) return true
            }
        else
            forEach {
                with(it.authority) {
                    if (this == target || this == MASTER_ROLE) return true
                }
            }
        return false
    }

    const val PRIV_POSTFIX = "PRIVILEGE"
    const val ROLE_PREFIX = "ROLE"
    const val MASTER_ROLE = "ROLE_MASTER"
}