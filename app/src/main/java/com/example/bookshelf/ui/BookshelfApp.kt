package com.example.bookshelf.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookshelf.ui.screen.BookDetailsScreen
import com.example.bookshelf.ui.screen.BookListScreen
import com.example.bookshelf.ui.screen.BooksViewModel

@Composable
fun BookshelfApp() {
    val navController: NavHostController = rememberNavController()

    val viewModel: BooksViewModel = viewModel(factory = BooksViewModel.Factory)
    val booksUiState = viewModel.booksUiState
    val bookDetailUiState = viewModel.bookDetailsUiState

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = BookshelfScreen.BOOK_LIST.name
        ) {
            composable(route = BookshelfScreen.BOOK_LIST.name) {
                BookListScreen(
                    retryAction = viewModel::getBooksByUserInput,
                    booksUiState = booksUiState,
                    userInput = viewModel.userInput,
                    onBookClick = { bookId ->
                        viewModel.getBookById(bookId)
                        navController.navigate(BookshelfScreen.BOOK_DETAILS.name)
                    },
                    onUserInputChange = viewModel::updateUserInput,
                    onGetBooksClick = { viewModel.getBooksByUserInput() },
                    isRefreshing = viewModel.isRefreshing,
                    onBooksRefresh = viewModel::getBooksByUserInput,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            composable(route = BookshelfScreen.BOOK_DETAILS.name) {
                BookDetailsScreen(
                    retryAction = {  },
                    onBackClick = navController::navigateUp,
                    bookDetailsUiState = bookDetailUiState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }

}