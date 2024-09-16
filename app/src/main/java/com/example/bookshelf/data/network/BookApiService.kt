package com.example.bookshelf.data.network

import com.example.bookshelf.data.model.Book
import com.example.bookshelf.data.model.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes/{id}")
    suspend fun getBookById(@Path("id") id: String) : Book
    @GET("volumes")
    suspend fun getBooksByQuery(
        @Query("q") q: String,
        @Query("startIndex") startIndex: String,
        @Query("maxResults") maxResults: String
    ) : Response
}