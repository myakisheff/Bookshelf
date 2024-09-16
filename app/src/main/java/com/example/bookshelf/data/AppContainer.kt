package com.example.bookshelf.data

import com.example.bookshelf.data.network.BookApiService
import com.example.bookshelf.data.repository.NetworkBookRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val bookRepository : NetworkBookRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val json = Json{ignoreUnknownKeys = true}

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val bookApiService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }

    override val bookRepository: NetworkBookRepository by lazy {
        NetworkBookRepository(bookApiService)
    }
}