package com.example.bookshelf.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.data.model.Book

@Composable
fun BookListScreen(
    retryAction: () -> Unit,
    onBookClick: (String) -> Unit,
    booksUiState: BooksUiState,
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onGetBooksClick: () -> Unit,
    onBooksRefresh: () -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { BookListScreenTopBar(
            userInput = userInput,
            onUserInputChange = onUserInputChange,
            onGetBooksClick = onGetBooksClick,
        ) },
        modifier = modifier
    ) { innerPadding ->
        when(booksUiState) {
            is BooksUiState.Error -> BookListScreenError(
                retryAction = retryAction,
                errorMessage = booksUiState.errorMessage,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
            is BooksUiState.Loading -> BookListScreenLoading(modifier = modifier)
            is BooksUiState.Success -> BookListScreenSuccess(
                onBookClick = onBookClick,
                books = booksUiState.books,
                isRefreshing = isRefreshing,
                onBooksRefresh = onBooksRefresh,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
            is BooksUiState.Waiting -> BookListScreenWaiting(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreenTopBar(
    onGetBooksClick: () -> Unit,
    userInput: String,
    onUserInputChange: (String) -> Unit,
    modifier : Modifier = Modifier
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = userInput,
                onValueChange = onUserInputChange,
                label = { Text(stringResource(R.string.enter_text)) },
                textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(12.dp)
            )
        },
        actions = {
            IconButton(onClick = onGetBooksClick) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        expandedHeight = TopAppBarDefaults.MediumAppBarExpandedHeight,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreenSuccess(
    onBookClick: (String) -> Unit,
    onBooksRefresh: () -> Unit,
    books: List<Book>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onBooksRefresh,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(dimensionResource(R.dimen.book_width)),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(books) { book ->
                BookCard(
                    onClick = { onBookClick(book.id) },
                    book = book
                )
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)

        if(book.volumeInfo.imageLinks?.thumbnail == null) {
            Image(
                painter = painterResource(R.drawable.book_placeholder),
                contentDescription = stringResource(R.string.there_no_book_preview),
                modifier = imageModifier
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks.thumbnail.replace("http", "https"))
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.book_photo),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.error),
                contentScale = ContentScale.FillBounds,
                modifier = imageModifier
            )
        }
    }
}

@Composable
fun BookListScreenWaiting(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.waiting), modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun BookListScreenError(
    retryAction: () -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(R.string.loading_failed) + "\n" + errorMessage,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BookListScreenLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(16.dp))
    }
}