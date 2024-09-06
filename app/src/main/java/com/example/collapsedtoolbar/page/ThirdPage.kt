package com.example.collapsedtoolbar.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collapsedtoolbar.ImageData
import com.example.collapsedtoolbar.ParallaxLayout
import com.example.collapsedtoolbar.R
import com.example.collapsedtoolbar.ui.theme.CollapsedToolbarTheme

@Composable
fun ThirdPage(imageData: ImageData) {

    val scrollState = rememberScrollState()

    ParallaxLayout(
        scrollState = scrollState,
        image = imageData.url,
        title = imageData.title,
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            repeat(20) {
                Text(text = stringResource(R.string.lorem))
            }
        }
    }
}


@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ThirdPagePreview() {
    CollapsedToolbarTheme {
        Surface {
            ThirdPage(ImageData("", "", ""))
        }
    }
}