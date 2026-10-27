package org.donnico.projectv1.exchange.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    suspend fun observe(): Flow<String?>
}
