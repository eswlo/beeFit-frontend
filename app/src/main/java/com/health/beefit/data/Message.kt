package com.health.beefit.data

import android.text.SpannableStringBuilder

class Message(var message: SpannableStringBuilder, var sentBy: String) {
    companion object {
        const val SENT_BY_ME = "me"
        const val SENT_BY_BOT = "bot"
    }
}
