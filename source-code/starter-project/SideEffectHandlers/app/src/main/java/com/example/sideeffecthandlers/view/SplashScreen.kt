package com.example.sideeffecthandlers.view

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.sideeffecthandlers.R

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    // This will handle the delay outside of Compose's side-effect APIs
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
        onTimeout()
    }, 2000)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_timer),
            contentDescription = "",
        )
    }

}