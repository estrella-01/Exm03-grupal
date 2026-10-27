package org.donnico.projectv1.notification

import android.content.Context

actual class TaskNotifier actual constructor(context: Any?) {
    private val helper = NotificationHelper(
        (context as? Context) ?: throw IllegalArgumentException("Context required")
    )

    actual fun setSilenced(silenced: Boolean) {
        helper.setSilenced(silenced)
    }

    actual fun isSilenced(): Boolean {
        return helper.isSilenced()
    }

    actual fun showNotification(titulo: String) {
        helper.showNotification(titulo)
    }
}
