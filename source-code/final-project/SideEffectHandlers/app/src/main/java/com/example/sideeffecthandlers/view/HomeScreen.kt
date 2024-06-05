package com.example.sideeffecthandlers.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.example.sideeffecthandlers.notification.TaskNotificationService
import com.example.sideeffecthandlers.ui.theme.Background
import com.example.sideeffecthandlers.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel,
    taskNotificationService: TaskNotificationService,
    lifecycleOwner: LifecycleOwner
) {

    val tasks = taskViewModel.tasks.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    // Remember a CoroutineScope across recompositions.
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    taskViewModel.getTasks()

    ModalBottomSheetLayout(sheetState = sheetState, sheetContent = {
        BottomSheet(
            sheetState,
            coroutineScope = coroutineScope, lifecycleOwner
        ) {

            taskViewModel.addTask(it)
            coroutineScope.launch { sheetState.hide() }
        }
    }, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            LazyColumn(contentPadding = PaddingValues(4.dp)) {
                items(tasks.value.size) {
                    ItemTask(task = tasks.value[it], taskNotificationService) { it2 ->
                        taskViewModel.updateTask(it2)
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (sheetState.isVisible) sheetState.hide()
                        else sheetState.show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, "Add", tint = Color.White)
            }
        }
    }
}



