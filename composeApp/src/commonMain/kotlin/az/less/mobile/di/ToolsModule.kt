package az.less.mobile.di

import az.less.mobile.analytics.AnalyticsWrapper
import az.less.mobile.analytics.AnalyticsWrapperImpl
import org.koin.dsl.module

val toolsModule = module {
    single<AnalyticsWrapper> { AnalyticsWrapperImpl() }
}