package com.example.sideeffecthandlers

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.sideeffecthandlers.notification.TaskNotificationService
import com.example.sideeffecthandlers.ui.theme.SideEffectHandlersTheme
import com.example.sideeffecthandlers.view.HomeScreen
import com.example.sideeffecthandlers.view.SplashScreen
import com.example.sideeffecthandlers.viewmodel.TaskViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val taskViewModel by viewModels<TaskViewModel>()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SideEffectHandlersTheme {

                val postNotificationPermission =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                val taskNotificationService = TaskNotificationService(applicationContext)

                LaunchedEffect(Unit) {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    }
                }

                Scaffold(topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ), title = { Text(text = "Task Timer App") }
                    )
                }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                        ,
                        color = MaterialTheme.colorScheme.background
                    ) {
                        var showSplashScreen by remember { mutableStateOf(true) }
                        if (showSplashScreen) {
                            SplashScreen(onTimeout = { showSplashScreen = false })
                        } else {
                             HomeScreen(taskViewModel,taskNotificationService,this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}
