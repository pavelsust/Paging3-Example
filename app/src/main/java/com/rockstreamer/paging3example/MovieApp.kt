package com.rockstreamer.paging3example

import android.app.Application
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import com.rockstreamer.paging3example.di.networkModule
import com.rockstreamer.paging3example.di.repositoryModule
import com.rockstreamer.paging3example.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AudienceNetworkAds.initialize(this)
        MobileAds.initialize(this)
        startKoin{
            androidContext(this@MovieApp)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}