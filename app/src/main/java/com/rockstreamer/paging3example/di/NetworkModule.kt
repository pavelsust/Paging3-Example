package com.rockstreamer.paging3example.di

import com.rockstreamer.paging3example.BuildConfig
import com.rockstreamer.paging3example.data.network.Api
import com.rockstreamer.paging3example.data.network.MovieAppService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { headerInterceptor() }
    single { okhttpClient(get()) }
    single { retrofit(get()) }
    single { apiService(get()) }
    single { createMovieAppService(get()) }
}

fun createMovieAppService(
    api: Api
) : MovieAppService = MovieAppService(api)

fun apiService(
    retrofit: Retrofit
) : Api =
    retrofit.create(Api::class.java)

fun retrofit(
    okHttpClient: OkHttpClient
) : Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun okhttpClient(
    headerInterceptor: Interceptor
) : OkHttpClient =
    OkHttpClient.Builder()
    .addInterceptor(headerInterceptor)
    .build()

fun headerInterceptor() : Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
}