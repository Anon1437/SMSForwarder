package com.example.smsforwarder

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SmsData(
    val sender: String,
    val body: String,
    val time: String = SimpleDateFormat("dd-MM-yyyy | hh:mm a", Locale.getDefault()).format(Date())
)

object MsgStore {
    val messages = mutableListOf<SmsData>()

    fun addMessage(sms: SmsData) {
        messages.add(0, sms)
        if (messages.size > 60) {
            messages.removeAt(messages.size - 1)
        }
    }
}
