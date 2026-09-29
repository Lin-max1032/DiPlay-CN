package com.shilapi.xcertplay.orchestration

/** Rules an existing (car) hotspot must meet before CarPlay can hand its credentials to the iPhone. */
object ManualHotspotValidation {
    /** Security implied by the password: the car hotspot UI only offers open or WPA2 networks. */
    fun securityFor(passphrase: String): ManualHotspotSecurity =
        if (passphrase.isEmpty()) ManualHotspotSecurity.OPEN else ManualHotspotSecurity.WPA2

    /** Returns a message for the user, or null when the name and password can be used. */
    fun validate(ssid: String, passphrase: String): String? = when {
        ssid.isBlank() -> "请输入车机热点名称"
        ssid.encodeToByteArray().size > 32 -> "热点名称最多为 32 个字节"
        '\u0000' in ssid || '\u0000' in passphrase -> "名称或密码包含无效字符"
        passphrase.isNotEmpty() && passphrase.length !in 8..63 -> "热点密码必须为 8–63 个字符"
        else -> null
    }
}
