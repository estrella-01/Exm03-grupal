package org.donnico.projectv1

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.donnico.projectv1.di.dataModule
import org.donnico.projectv1.di.domainModule
import org.donnico.projectv1.di.presentationModule
import org.donnico.projectv1.scheduler.LogScheduler

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(dataModule, domainModule, presentationModule)
        }
        LogScheduler(this).schedulePeriodicalUpload()
    }
}
