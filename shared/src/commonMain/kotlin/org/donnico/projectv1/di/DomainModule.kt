package org.donnico.projectv1.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.donnico.projectv1.exchange.domain.usecase.ObserveExchangeUseCase

val domainModule = module {
    singleOf(::ObserveExchangeUseCase)
}
