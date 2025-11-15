package az.less.mobile.di

import az.less.mobile.presentation.main.more.MoreViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoreViewModel)
}


