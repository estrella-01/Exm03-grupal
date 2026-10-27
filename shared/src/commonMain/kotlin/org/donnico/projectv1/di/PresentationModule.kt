package org.donnico.projectv1.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.donnico.projectv1.exchange.presentation.viewmodel.ExchangeViewModel

val presentationModule = module {
    viewModelOf(::ExchangeViewModel)
}
