package com.gmail.pavlovsv93.whattoseetoday

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class MovieDetailsBCReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder()
            .apply {
                append("Сообщение от системы\n")
                append("Action: ${intent.action}")
                toString()
                    .also {
                        Toast.makeText(context, it, Toast.LENGTH_LONG)
                    }
        }
    }
}