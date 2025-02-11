package com.example.pushnotifi

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.w("AAAA","llego mensaje")
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        super.onMessageReceived(message)

    }

}