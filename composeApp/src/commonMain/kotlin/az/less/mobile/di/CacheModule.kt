package az.less.mobile.di

import az.less.mobile.data.repository.UserRepository
import az.less.mobile.preferences.createPlatformDataStore
import org.koin.dsl.module

val cacheModule = module {
    single { createPlatformDataStore() }
    single { UserRepository(get()) }
}