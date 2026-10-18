package com.example.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class SmsReceiver : BroadcastReceiver() {
    
    // ⚠️ CHANGE THIS to your PHP server URL
    private val PHP_URL = "https://yourdomain.com/sms_receiver.php"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle: Bundle = intent.extras ?: return
            val pdus = bundle.get("pdus") as? Array<*> ?: return
            
            for (pdu in pdus) {
                val format = bundle.getString("format")
                val sms = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(pdu as ByteArray, format)
                } else {
                    @Suppress("DEPRECATION")
                    SmsMessage.createFromPdu(pdu as ByteArray)
                }
                
                val sender = sms.originatingAddress ?: "Unknown"
                val body = sms.messageBody ?: ""
                
                MsgStore.addMessage(SmsData(sender, body))
                forwardToServer(sender, body)
            }
        }
    }

    private fun forwardToServer(sender: String, body: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val formBody = FormBody.Builder()
                    .add("sender", sender)
                    .add("body", body)
                    .build()

                val request = Request.Builder()
                    .url(PHP_URL)
                    .post(formBody)
                    .build()

                client.newCall(request).execute().use { response ->
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
