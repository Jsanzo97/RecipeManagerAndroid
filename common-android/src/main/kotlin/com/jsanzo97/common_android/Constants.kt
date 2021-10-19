package com.jsanzo97.common_android

import android.Manifest

const val SHARED_PREFERENCES_KEY = "userSharedPrefs"
const val USERNAME_KEY = "username"
const val LAST_USERNAME_LOGGED = "lastUsernameLogged"
const val LAST_LOG_IN = "lastLogIn"
const val LAST_BOOK_UPDATE = "lastBookUpdate"

const val DAY_IN_SECONDS = 86400L

const val DIALOG_FRAGMENT_TAG = "CustomFragment"

val PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

object Location {
    const val INTERVAL = 1000L
    const val FASTEST_INTERVAL = 500L

    const val REQUEST_LOCATION = 0
    const val REQUEST_PERMISSIONS_CODE = 1

    const val CHANNEL_ID = "loginNotificationsChannel"
    const val NOTIFICATION_ID = 1

}

