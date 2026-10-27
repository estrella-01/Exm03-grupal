package org.donnico.projectv1.scheduler

expect class LogScheduler(context: Any? = null) {
    fun schedulePeriodicalUpload()
}
