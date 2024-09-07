package com.example.collapsingtoolbar

data class ImageData(
    val pageRef: String,
    val title: String,
    val url: String
)

val images = listOf(
    ImageData(
        "page 0",
        "Image 1",
        "https://picsum.photos/600/600?random=1"
    ),
    ImageData(
        "page 1",
        "Image 2",
        "https://science.nasa.gov/wp-content/uploads/2023/11/9827327865_98e0f0dc2d_o.jpg"
    ),
    ImageData(
        "page 2",
        "Image 3",
        "https://images.template.net/wp-content/uploads/2017/01/Nature-Photography.jpg"
    ),
    ImageData(
        "page 3",
        "Image 4",
        "https://static.sadhguru.org/d/46272/1633199491-1633199490440.jpg"
    ),
)