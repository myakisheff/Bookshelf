package com.example.bookshelf.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookshelf.R
import com.example.bookshelf.data.model.Book
import com.example.bookshelf.ui.theme.BookshelfTheme

@Composable
fun BookDetailsScreen(
    retryAction: () -> Unit,
    onBackClick: () -> Unit,
    bookDetailsUiState: BookDetailsUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { BookDetailsTopBar(
            onBackClick = onBackClick
        ) }
    ) { innerPadding ->
        when(bookDetailsUiState) {
            is BookDetailsUiState.Error -> BookDetailsScreenError(
                retryAction = retryAction,
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
            is BookDetailsUiState.Loading -> BookDetailsScreenLoading(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
            is BookDetailsUiState.Success -> BookDetailsScreenContent(
                book = bookDetailsUiState.book,
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
            is BookDetailsUiState.Waiting -> BookDetailsScreenWaiting(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailsTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.book_details)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
    )
}

@Composable
private fun BookDetailsScreenContent(
    book: Book,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if(book.volumeInfo.imageLinks?.thumbnail != null) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(book.volumeInfo.imageLinks.thumbnail.replace("http", "https"))
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.book_photo),
                placeholder = painterResource(R.drawable.loading),
                error = painterResource(R.drawable.error),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.book_placeholder),
                contentDescription = stringResource(R.string.there_no_book_preview),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        book.volumeInfo.title.let {
            Text(
                text = it ?: stringResource(R.string.no_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        book.volumeInfo.publisher.let {
            Text(
                text = it ?: stringResource(R.string.no_publisher),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        book.volumeInfo.publishedDate.let {
            Text(
                text = it ?: stringResource(R.string.no_published_date),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        book.volumeInfo.description.let {
            Text(text = it ?: stringResource(R.string.no_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }
        Text(
            text = stringResource(R.string.authors),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        book.volumeInfo.authors?.forEach {
            Text(
                text = it,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth())
        } ?: Text(
                text = stringResource(R.string.no_authors),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
    }
}

@Composable
fun BookDetailsScreenError(
    retryAction: () -> Unit,
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
            text = stringResource(R.string.loading_failed),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun BookDetailsScreenLoading(
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

@Composable
fun BookDetailsScreenWaiting(
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

@Preview
@Composable
fun BookDetailsScreenPreview() {
    BookshelfTheme {
        BookDetailsScreen(
            retryAction = {},
            onBackClick = {},
            bookDetailsUiState = BookDetailsUiState.Loading
        )
    }
}