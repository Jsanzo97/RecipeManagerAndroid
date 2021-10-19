package com.jsanzo97.data.datastore.login

import arrow.core.Option
import com.jsanzo97.data.error.CloudDataError

interface CloudLoginDataStore {

    suspend fun login(username: String, password: String): Option<CloudDataError>
    suspend fun signUp(username: String, password: String): Option<CloudDataError>
}