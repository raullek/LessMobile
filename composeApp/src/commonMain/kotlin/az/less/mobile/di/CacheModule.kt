package az.less.mobile.di

import az.less.mobile.data.repository.SessionLocalRepositoryImpl
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.preferences.createPlatformDataStore
import az.less.mobile.presentation.theme.ThemeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val cacheModule = module {
    single { createPlatformDataStore() }
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single<SessionLocalRepository> { SessionLocalRepositoryImpl(get(), get()) }
    single { ThemeManager(get(), CoroutineScope(SupervisorJob() + Dispatchers.Main)) }
}
