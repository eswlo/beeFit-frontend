package com.health.beefit.utils

import android.util.Base64
import org.json.JSONObject

object JWTUtils {
    fun decodeJWT(jwt: String): JSONObject? {
        val parts = jwt.split(".")
        if (parts.size == 3) {
            val decodedPayload = decodeBase64Url(parts[1])
            return JSONObject(decodedPayload)
        }
        return null
    }

    private fun decodeBase64Url(input: String): String {
        val decodedBytes = Base64.decode(input, Base64.URL_SAFE)
        return String(decodedBytes, Charsets.UTF_8)
    }
}
