package com.example.bookshelf.data.repository

import com.example.bookshelf.data.model.Book
import com.example.bookshelf.data.model.Response
import com.example.bookshelf.data.network.BookApiService

interface BookRepository {
    suspend fun getBookById(id: String): Book
    suspend fun getBooksByQuery(query: String, startIndex: String, maxResults: String): Response
}

class NetworkBookRepository(
    private val bookApiService: BookApiService
): BookRepository {
    override suspend fun getBookById(id: String): Book =
        bookApiService.getBookById(id = id)

    override suspend fun getBooksByQuery(query: String, startIndex: String, maxResults: String): Response =
        bookApiService.getBooksByQuery(q = query, startIndex = startIndex, maxResults = maxResults)
}