package org.donnico.projectv1.notification

actual class TaskNotifier actual constructor(context: Any?) {
    actual fun setSilenced(silenced: Boolean) {}
    actual fun isSilenced(): Boolean = false
    actual fun showNotification(titulo: String) {}
}
