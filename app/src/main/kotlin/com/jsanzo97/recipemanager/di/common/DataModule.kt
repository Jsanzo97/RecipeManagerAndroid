package com.jsanzo97.recipemanager.di.common

import androidx.room.Room
import com.jsanzo97.data.datastore.book.CloudBookDataStore
import com.jsanzo97.data.datastore.book.LocalBookDataStore
import com.jsanzo97.data.datastore.creation.CloudCreationDataStore
import com.jsanzo97.data.datastore.login.CloudLoginDataStore
import com.jsanzo97.data.repository.book.BookDataRepository
import com.jsanzo97.data.repository.creation.CreationDataRepository
import com.jsanzo97.data.repository.login.LoginDataRepository
import com.jsanzo97.domain.repository.book.BookRepository
import com.jsanzo97.domain.repository.creation.CreationRepository
import com.jsanzo97.domain.repository.login.LoginRepository
import com.jsanzo97.local.LocalDatabase
import com.jsanzo97.local.storage.book.BookStorage
import com.jsanzo97.remote.service.book.BookService
import com.jsanzo97.remote.service.creation.CreationService
import com.jsanzo97.remote.service.login.LoginService
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

private const val DATABASE_NAME = "localStorage.db"

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), LocalDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }


    /*  DAO */

    single {
        val database = get<LocalDatabase>()
        database.bookDao()
    }


    /* AUTHENTICATE SERVICE */

    single<LoginRepository> { LoginDataRepository(get(), Dispatchers.IO) }

    single<CloudLoginDataStore> {
        val retrofitUnauthenticated = get<Retrofit>(named(UNAUTHENTICATED_WS))
        LoginService(retrofitUnauthenticated.create())
    }


    /* RECIPE BOOK SERVICE */

    single<BookRepository> { BookDataRepository(get(), get(), Dispatchers.IO) }

    single<LocalBookDataStore> { BookStorage(get()) }

    single<CloudBookDataStore> {
        val retrofitUnauthenticated = get<Retrofit>(named(UNAUTHENTICATED_WS))
        BookService(retrofitUnauthenticated.create())
    }


    /* RECIPE CREATION SERVICE */

    single<CreationRepository> { CreationDataRepository(get(), get(), Dispatchers.IO) }

    single<CloudCreationDataStore> {
        val retrofitUnauthenticated = get<Retrofit>(named(UNAUTHENTICATED_WS))
        CreationService(retrofitUnauthenticated.create())
    }
}
