package com.rockstreamer.paging3example.di

import com.rockstreamer.paging3example.data.network.MovieAppService
import com.training.pagingsample.data.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single { createRepository(get()) }
}

fun createRepository(
    movieAppService: MovieAppService
) : Repository = Repository(movieAppService)