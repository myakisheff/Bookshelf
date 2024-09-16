package com.example.bookshelf.ui.screen

import com.example.bookshelf.data.model.Book

sealed interface BooksUiState {
    data class Success(val books: List<Book>) : BooksUiState
    data class Error(val errorMessage: String?) : BooksUiState
    data object Loading : BooksUiState
    data object Waiting : BooksUiState
}

sealed interface BookDetailsUiState {
    data class Success(val book: Book) : BookDetailsUiState
    data object Error : BookDetailsUiState
    data object Loading : BookDetailsUiState
    data object Waiting : BookDetailsUiState
}
