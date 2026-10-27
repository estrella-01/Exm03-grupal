package org.donnico.projectv1.exchange.data.repository

import kotlinx.coroutines.flow.Flow
import org.donnico.projectv1.exchange.data.datasource.RealTimeDataBase
import org.donnico.projectv1.exchange.domain.repository.ExchangeRepository

class ExchangeRepositoryImpl(
    val realTimeDataBase: RealTimeDataBase
) : ExchangeRepository {
    override suspend fun observe(): Flow<String?> {
        return realTimeDataBase.observeMessage()
    }
}
