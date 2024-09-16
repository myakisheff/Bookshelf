package com.example.bookshelf.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val items: List<Book>
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)