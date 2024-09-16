package com.example.bookshelf.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.network.HttpException
import com.example.bookshelf.BooksApplication
import com.example.bookshelf.data.model.Book
import com.example.bookshelf.data.repository.NetworkBookRepository
import kotlinx.coroutines.launch
import java.io.IOException

class BooksViewModel(
    private val booksRepository: NetworkBookRepository
): ViewModel() {
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Waiting)
        private set

    var bookDetailsUiState: BookDetailsUiState by mutableStateOf(BookDetailsUiState.Waiting)
        private set

    private var books by mutableStateOf(emptyList<Book>())
    private var countOfBooks by mutableIntStateOf(20)
    private var countOResponses by mutableIntStateOf(0)

    var isRefreshing by mutableStateOf(false)

    var userInput by mutableStateOf("")
        private set

    fun updateUserInput(input: String) {
        userInput = input
        books = emptyList()
        countOResponses = 0
    }

    fun getBookById(id: String) {
        bookDetailsUiState = BookDetailsUiState.Loading
        viewModelScope.launch {
            bookDetailsUiState = try {
                val book = booksRepository.getBookById(id)
                BookDetailsUiState.Success(book = book)
            } catch (e: IOException) {
                BookDetailsUiState.Error
            } catch (e: HttpException) {
                BookDetailsUiState.Error
            }
        }
    }

    fun getBooksByUserInput() {
        viewModelScope.launch {
            booksUiState = try {
                val res = booksRepository.getBooksByQuery(
                    query = userInput,
                    startIndex = ((countOResponses * countOfBooks) - 0).toString(),
                    maxResults = countOfBooks.toString()
                )
                books = res.items + books
                countOResponses++
                BooksUiState.Success(books = books)
            } catch (e: IOException) {
                BooksUiState.Error(errorMessage = e.message)
            } catch (e: retrofit2.HttpException) {
                BooksUiState.Error(errorMessage = e.message)
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BooksApplication)
                val repository = application.container.bookRepository
                BooksViewModel(booksRepository = repository)
            }
        }
    }
}