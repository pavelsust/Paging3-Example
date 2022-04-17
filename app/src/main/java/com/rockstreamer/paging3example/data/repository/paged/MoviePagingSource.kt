package com.rockstreamer.paging3example.data.repository.paged

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rockstreamer.paging3example.data.model.Movie
import com.training.pagingsample.data.repository.Repository

class MoviePagingSource(
    private val repository: Repository
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = repository.getPopularMovies(nextPage)

            LoadResult.Page(
                data = movieListResponse.results!!,
                prevKey = if (nextPage == 1) null else nextPage - 1 ,
                nextKey = if (nextPage < movieListResponse.totalPages!!)
                    movieListResponse.page?.plus(1) else null
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
