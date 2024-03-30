package top.eviarch.simplenote.extra

import java.util.UUID.randomUUID

object UUIDUtil {
    fun generateUniqueId(): Long {
        return randomUUID().mostSignificantBits and Long.MAX_VALUE
    }
}