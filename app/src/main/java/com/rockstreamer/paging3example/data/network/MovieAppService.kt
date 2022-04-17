package com.rockstreamer.paging3example.data.network

import com.rockstreamer.paging3example.data.model.Result
import com.rockstreamer.paging3example.data.network.response.MovieListResponse

class MovieAppService(private val api: Api) : BaseService() {

    suspend fun fetchPopularMovies(page: Int) : Result<MovieListResponse> {
        return createCall { api.getPopularMovies(page) }
    }
}