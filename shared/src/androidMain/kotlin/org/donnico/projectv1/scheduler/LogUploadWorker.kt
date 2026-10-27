package org.donnico.projectv1.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.donnico.projectv1.exchange.domain.usecase.ObserveExchangeUseCase

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val observeExchangeUseCase: ObserveExchangeUseCase by inject()

    override suspend fun doWork(): Result {
        println("ejecutar instrucción para subir datos")
        observeExchangeUseCase.invoke()
        return Result.success()
    }
}
