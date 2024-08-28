package com.example.sideeffecthandlers.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sideeffecthandlers.notification.TaskNotificationService
import com.example.sideeffecthandlers.ui.theme.Background
import com.example.sideeffecthandlers.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel,
    taskNotificationService: TaskNotificationService,
) {
    val tasks = taskViewModel.tasks.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    taskViewModel.getTasks()

    fun showBottomSheet() {
        showBottomSheet = true
    }

    fun hideBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    BackHandler(sheetState.isVisible) {
        hideBottomSheet()
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { hideBottomSheet() },
            sheetState = sheetState
        ) {
            BottomSheet({
                hideBottomSheet()
            }, {
                taskViewModel.addTask(it)
                hideBottomSheet()
            })
        }
    }

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
                showBottomSheet()
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



