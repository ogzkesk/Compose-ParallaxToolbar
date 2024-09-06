package com.example.collapsedtoolbar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collapsedtoolbar.page.FirstPage
import com.example.collapsedtoolbar.page.FourthPage
import com.example.collapsedtoolbar.page.SecondPage
import com.example.collapsedtoolbar.page.ThirdPage
import com.example.collapsedtoolbar.ui.theme.CollapsedToolbarTheme

class MainActivity : ComponentActivity() {

    private var pageIndex: Int = 0

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            CollapsedToolbarTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeContent,
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                if(pageIndex == images.size - 1) pageIndex = 0 else pageIndex++
                                navController.navigate("page $pageIndex")
                            }
                        ) {
                            Icon(Icons.Default.ArrowForward, null)
                        }
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = images.first().pageRef
                    ) {
                        images.forEach { image ->
                            composable(route = image.pageRef) {
                                when (image.pageRef) {
                                    "page 0" -> FirstPage()
                                    "page 1" -> SecondPage(imageData = image)
                                    "page 2" -> ThirdPage(imageData = image)
                                    "page 3" -> FourthPage(imageData = image)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}