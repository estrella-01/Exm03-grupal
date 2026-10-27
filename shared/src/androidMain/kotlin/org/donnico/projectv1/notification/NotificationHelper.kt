package org.donnico.projectv1.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {
    private val channelId = "tareas_channel"
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal de Tareas"
            val descriptionText = "Notificaciones de nuevas tareas creadas"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            println("NOTIF_DEBUG: canal creado id=$channelId")
        }
    }

    fun setSilenced(silenced: Boolean) {
        prefs.edit().putBoolean("silenced", silenced).apply()
    }

    fun isSilenced(): Boolean {
        return prefs.getBoolean("silenced", false)
    }

    fun showNotification(titulo: String) {
        if (isSilenced()) {
            println("NOTIF_DEBUG: silenciado, no se muestra")
            return
        }

        // Validar permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                println("NOTIF_DEBUG: permiso POST_NOTIFICATIONS no concedido")
                return
            }
        }

        println("NOTIF_DEBUG: mostrando notificación '$titulo'")

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nueva tarea")
            .setContentText(titulo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}