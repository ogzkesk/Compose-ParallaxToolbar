package com.example.collapsedtoolbar.page

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collapsedtoolbar.ParallaxLayout
import com.example.collapsedtoolbar.R
import com.example.collapsedtoolbar.ui.theme.CollapsedToolbarTheme
import kotlinx.coroutines.launch

@Composable
fun FirstPage() {

    val scrollState = rememberScrollState()

    ParallaxLayout(
        scrollState = scrollState,
        title = null,
        image = null,
        headerContentShadow = null,
        defaultHeight = 400.dp,
        headerContent = { isCollapsed, contentAlpha ->
            MPagerContent(modifier = Modifier.alpha(contentAlpha))
            AnimatedVisibility(
                visible = isCollapsed,
                enter = fadeIn(tween(400)),
                exit = fadeOut(tween(400))
            ) {
                MToolbar()
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Product title",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            repeat(20) {
                Text(text = stringResource(R.string.lorem))
            }
        }
    }
}

@Composable
fun MPagerContent(modifier: Modifier = Modifier) {
    val imageList = remember {
        listOf(
            R.drawable.product_1,
            R.drawable.product_2,
            R.drawable.product_3,
            R.drawable.product_4
        )
    }
    val pagerState = rememberPagerState { imageList.size }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState
        ) { i ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(imageList[i]),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            repeat(imageList.size) { i ->
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(
                            if (pagerState.currentPage == i)
                                Color.LightGray
                            else
                                Color.DarkGray.copy(alpha = 0.6F)
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.1F),
                    contentColor = Color.White
                ),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.canScrollBackward) {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.1F),
                    contentColor = Color.White
                ),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.canScrollForward) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MToolbar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = "Product", style = MaterialTheme.typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Settings, null)
            }
        }
    )
}

@Preview(
    name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ThirdPagePreview() {
    CollapsedToolbarTheme {
        Surface {
            FirstPage()
        }
    }
}