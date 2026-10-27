package org.donnico.projectv1.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.donnico.projectv1.exchange.data.datasource.RealTimeDataBase
import org.donnico.projectv1.exchange.data.repository.ExchangeRepositoryImpl
import org.donnico.projectv1.exchange.domain.repository.ExchangeRepository

val dataModule = module {
    singleOf(::RealTimeDataBase)
    single<ExchangeRepository> { ExchangeRepositoryImpl(get()) }
}
