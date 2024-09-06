package com.example.collapsedtoolbar

import android.content.Context
import android.util.Range
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * A composable that implements a parallax toolbar effect. The toolbar
 * background image and title smoothly transition as the user scrolls.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param scrollState The scroll state of the content that the toolbar
 *        responds to.
 * @param title The title to be displayed in the toolbar.
 * @param image The image to be used as the background of the toolbar.
 *        Can be either a resource ID or any object supported by Glide.
 * @param defaultHeight The default height of the header when fully
 *        expanded.
 * @param defaultToolbarHeight The height of the collapsed toolbar.
 * @param collapsedTitlePadding The padding applied to the title when the
 *        toolbar is collapsed.
 * @param imageContentScale The content scale to be applied to the background
 *        image.
 * @param titleTextStyle The text style to be applied to the title.
 * @param titleTextColor The color of the title text.
 * @param headerContentShadow The shadow to be applied to the bottom of header
 *        for highlighting the title.
 * @param navigationIcon A composable function that defines the navigation
 *        icon for the toolbar.
 * @param actions A composable function that defines the actions to be
 *        displayed in the toolbar.
 * @param headerContent Composable function that allows defining custom
 *        content of the header
 * @param content The content to be displayed below the toolbar.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ParallaxLayout(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    title: String?,
    image: Any?,
    defaultHeight: Dp = 350.dp,
    defaultToolbarHeight: Dp = 64.dp,
    collapsedTitlePadding: Dp = 0.dp,
    toolbarBrushEnabled: Boolean = true,
    imageContentScale: ContentScale = ContentScale.Crop,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    titleTextColor: Color = Color.White,
    headerContentShadow: Brush? = remember {
        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7F)))
    },
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    headerContent: @Composable ((isCollapsed: Boolean, contentAlpha: Float) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (image != null && headerContent != null) {
        throw IllegalStateException("image and headerContent can't be used together. Use only one of them.")
    }

    val density = LocalDensity.current
    val context = LocalContext.current
    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()


    val toolbarContentPadding = 8.dp
    val toolbarHeight = defaultToolbarHeight + statusBarHeight

    var toolbarGradient: Brush by remember {
        mutableStateOf(SolidColor(Color.Transparent))
    }

    LaunchedEffect(Unit) {
        if (toolbarBrushEnabled.not()) {
            return@LaunchedEffect
        }
        generatePaletteBrush(context, image) { brush ->
            toolbarGradient = brush
        }
    }


    val headerHeight by remember {
        derivedStateOf {
            val height = defaultHeight - with(density) { scrollState.value.toDp() }
            if (height >= toolbarHeight) height else toolbarHeight
        }
    }


    val contentAlpha by remember {
        derivedStateOf {
            val alpha = getOptimalPoint(
                value = headerHeight,
                valueRange = Range(toolbarHeight, defaultHeight),
                targetRange = Range(0F, 1F),
            )
            toolbarGradient.setAlpha(alpha.reversed())
            return@derivedStateOf alpha
        }
    }

    var initialNavIconWidth by remember {
        mutableStateOf(0.dp)
    }

    var initialActionsWidth by remember {
        mutableStateOf(0.dp)
    }

    var initialTitleTextSize by remember {
        mutableStateOf(Size.Zero)
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(toolbarGradient)
        ) {

            if (image != null && image is Int) {
                Image(
                    painter = painterResource(image),
                    contentScale = imageContentScale,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(contentAlpha)
                )
            } else {
                GlideImage(
                    model = image,
                    contentScale = imageContentScale,
                    contentDescription = null,
                    transition = CrossFade,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(contentAlpha)
                )
            }

            if (headerContent != null) {
                headerContent(
                    headerHeight == toolbarHeight,
                    contentAlpha
                )
            }

            if (headerContentShadow != null) {
                Box(
                    modifier = Modifier
                        .alpha(contentAlpha)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(defaultHeight / 2)
                        .background(headerContentShadow)
                )
            }

            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .height(defaultToolbarHeight)
                    .padding(toolbarContentPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navigationIcon != null) {
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned {
                                if (initialNavIconWidth == 0.dp) {
                                    initialNavIconWidth = with(density) { it.size.width.toDp() }
                                }
                            }
                    ) {
                        navigationIcon()
                    }
                }
                if (actions != null) {
                    Row(
                        modifier = Modifier
                            .onGloballyPositioned {
                                if (initialActionsWidth == 0.dp) {
                                    initialActionsWidth = with(density) { it.size.width.toDp() }
                                }
                            }
                    ) {
                        actions()
                    }
                }
            }

            if (title != null) {

                Text(
                    modifier = Modifier
                        .statusBarsPadding()
                        .align(Alignment.BottomStart)
                        .onGloballyPositioned {
                            if (initialTitleTextSize == Size.Zero) {
                                initialTitleTextSize = it.size.toSize()
                            }
                        }
                        .graphicsLayer {
                            val textScale = getOptimalPoint(
                                headerHeight,
                                Range(toolbarHeight, defaultHeight),
                                Range(0.7F, 1F)
                            )
                            val headerScale = getOptimalPoint(
                                headerHeight,
                                Range(toolbarHeight, defaultHeight),
                                Range(0F, 1F)
                            )
                            val titlePadding = 12.dp.toPx() * headerScale
                            val scaleDiff =
                                (initialTitleTextSize.width - (initialTitleTextSize.width * textScale)) / 2
                            val startPadding = toolbarContentPadding.toPx() - scaleDiff
                            val textPadding =
                                headerScale.reversed() * initialNavIconWidth.toPx() + collapsedTitlePadding.toPx()

                            scaleX = textScale
                            scaleY = textScale
                            translationX = lerp(
                                start = startPadding.toDp() + titlePadding.toDp(),
                                stop = (startPadding + textPadding).toDp(),
                                fraction = headerScale.reversed()
                            ).toPx()
                            translationY = -defaultToolbarHeight.value / 2 - titlePadding
                        },
                    text = title,
                    color = titleTextColor,
                    style = titleTextStyle,
                    maxLines = getOptimalPoint(
                        headerHeight,
                        Range(toolbarHeight, defaultHeight),
                        Range(1F, 3F)
                    ).roundToInt(),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        content()
    }
}


/**
 * converts a value from [valueRange] to corresponding value in [targetRange]
 */
fun getOptimalPoint(
    value: Dp,
    valueRange: Range<Dp>,
    targetRange: Range<Float>,
): Float {
    if (valueRange.contains(valueRange).not()) {
        throw IllegalStateException("value must be between valueRange")
    }
    val progress = (value - valueRange.lower) / (valueRange.upper - valueRange.lower)
    return targetRange.lower + (targetRange.upper - targetRange.lower) * progress
}


/**
 * returns reversed value between 0F and 1F
 */
fun Float.reversed(): Float {
    if (this > 1F || this < 0F) {
        throw IllegalStateException("Value must be between 0F and 1F")
    }
    return 1F - this
}


/**
 * Applies alpha [value] to the [Brush]
 */
fun Brush.setAlpha(value: Float) {
    this.applyTo(this.intrinsicSize, Paint(), value)
}


/**
 * Creates a horizontal gradient [Brush] from a [String] *url or [Int] *drawableRes
 */
suspend fun generatePaletteBrush(
    context: Context,
    image: Any?,
    maxColors: Int = 3,
    onResult: (Brush) -> Unit,
) {
    withContext(Dispatchers.IO) {

        if (image == null) {
            return@withContext
        }

        val bitmap = when (image) {
            is Int -> {
                val drawable = context.getDrawable(image)
                drawable?.toBitmapOrNull() ?: return@withContext
            }

            else -> {
                val drawable = Glide.with(context).load(image).submit().get()
                drawable.toBitmapOrNull() ?: return@withContext
            }
        }

        Palette.from(bitmap).generate { palette ->
            val colors = palette?.swatches
                ?.mapNotNull { Color(it.rgb) }
                ?.take(maxColors)
                ?: return@generate

            onResult.invoke(Brush.horizontalGradient(colors))
        }
    }
}
