## Jetpack compose collapsing toolbar with parallax effect


https://github.com/user-attachments/assets/0eb89f00-3e26-4cef-a9ea-1246c7545be7


## Necessary dependencies:

```kotlin
    implementation("androidx.palette:palette-ktx:<version>")
    implementation("com.github.bumptech.glide:compose:<version>")
```

### With header content

```kotlin
    val scrollState = rememberScrollState()

    ParallaxLayout(
        scrollState = scrollState,
        title = null,
        image = null,
        headerContentShadow = null,
        defaultHeight = 400.dp,
        headerContent = { isCollapsed, contentAlpha ->
            MPagerContent(modifier = Modifier.alpha(contentAlpha))
        },
    ) {
        UIContent(modifier = Modifier.verticalScroll(scrollState)){
          ...
        }
      }
```

### OR

```kotlin
    val scrollState = rememberScrollState()

    ParallaxLayout(
        scrollState = scrollState,
        image = "imageUrl" // or drawableRes,
        title = "title",
    ) {
         UIContent(modifier = Modifier.verticalScroll(scrollState)){
          ...
        }
    }
```



