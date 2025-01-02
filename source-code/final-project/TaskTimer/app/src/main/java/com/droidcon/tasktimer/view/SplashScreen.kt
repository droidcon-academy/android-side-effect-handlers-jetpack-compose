package com.droidcon.tasktimer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.droidcon.tasktimer.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    // Use produceState to define a state for continuous degree rotation.
    val degree = produceState(initialValue = 0) {
        while (true) {
            delay(30)
            value = (value + 10) % 360
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        // This remembers the updated state of any variable
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(Unit) {
            delay(2000)
            currentOnTimeout()
        }

        Image(painterResource(id = R.drawable.ic_timer), contentDescription = "",modifier = Modifier.rotate(degree.value.toFloat()))
    }
}