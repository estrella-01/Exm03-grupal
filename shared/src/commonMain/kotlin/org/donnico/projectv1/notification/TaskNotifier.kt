package org.donnico.projectv1.notification

expect class TaskNotifier(context: Any? = null) {
    fun setSilenced(silenced: Boolean)
    fun isSilenced(): Boolean
    fun showNotification(titulo: String)
}
