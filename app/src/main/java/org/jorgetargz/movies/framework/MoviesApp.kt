package org.jorgetargz.movies.framework

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}