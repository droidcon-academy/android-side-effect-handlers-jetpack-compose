package com.example.sideeffecthandlers.view

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.sideeffecthandlers.R
import com.example.sideeffecthandlers.notification.TaskNotificationService
import com.example.sideeffecthandlers.model.Task
import com.example.sideeffecthandlers.ui.theme.Background
import com.example.sideeffecthandlers.viewmodel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BottomSheet(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    lifecycleOwner: LifecycleOwner,
    onCreateClick: (Task) -> Unit,
    ) {
    var pickerValueHours by remember { mutableStateOf(0) }
    var pickerValueMinutes by remember { mutableStateOf(0) }
    var pickerValueSeconds by remember { mutableStateOf(0) }
    var taskName by remember { mutableStateOf("") }
    var isErrorInName by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Derive a state from other states. It's useful when you want to combine or compute a state based on existing ones.
    val isErrorInDuration by remember {
        derivedStateOf {
            pickerValueHours == 0 && pickerValueMinutes == 0 && pickerValueSeconds == 0
        }
    }

    DisposableEffect(lifecycleOwner) {
        Log.d("TaskTimerApp","BottomSheet Created")

        val lifecycleObserver = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.d("TaskTimerApp","BottomSheet Destroyed")
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            Log.d("TaskTimerApp","DisposableEffect Disposed")
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "New Task Timer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    pickerValueHours = 0
                    pickerValueMinutes = 0
                    pickerValueSeconds = 0
                    taskName = ""
                    isErrorInName = false
                    keyboardController?.hide()
                },

                ) {
                Icon(Icons.Filled.Close, "Close")
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        OutlinedTextField(
            value = taskName,
            onValueChange = {
                taskName = it
                isErrorInName = it.isEmpty()
            },
            label = {
                Text(text = "Task Name")
            },
            isError = isErrorInName,
            trailingIcon = {
                if (isErrorInName)
                    Icon(
                        painterResource(id = R.drawable.ic_error),
                        "error",
                        tint = MaterialTheme.colorScheme.error
                    )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() },
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = MaterialTheme.colorScheme.primary)
        )

        if (isErrorInName) {
            Text(
                text = "Please add task name",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        DurationPicker(pickerValueHours, pickerValueMinutes, pickerValueSeconds, {
            pickerValueHours = it
        }, {
            pickerValueMinutes = it
        }) {
            pickerValueSeconds = it
        }

        val context = LocalContext.current
        Button(
            onClick = {
                if (taskName == "") {
                    isErrorInName = true
                } else if (isErrorInDuration) {
                    Toast.makeText(context, "Please select duration", Toast.LENGTH_SHORT).show()
                } else {

                    onCreateClick(
                        Task(
                            0,
                            taskName,
                            pickerValueHours,
                            pickerValueMinutes,
                            pickerValueSeconds,
                            "Paused"
                        )
                    )
                    // Reset values after creating the task
                    pickerValueHours = 0
                    pickerValueMinutes = 0
                    pickerValueSeconds = 0
                    taskName = ""
                    keyboardController?.hide()
                }
            }, modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Create")
        }
    }

}


@Composable
fun DurationPicker(
    pickerValueHours: Int,
    pickerValueMinutes: Int,
    pickerValueSeconds: Int,
    onPickerHoursChange: (Int) -> Unit,
    onPickerMinutesChange: (Int) -> Unit,
    onPickerSecondsChange: (Int) -> Unit,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Select Duration:",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 4.dp)
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
        )

        NumberPicker(
            value = pickerValueHours,
            range = 0..23,
            dividersColor = MaterialTheme.colorScheme.primary,
            onValueChange = {
                onPickerHoursChange(it)
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = "h"
        )
        NumberPicker(
            value = pickerValueMinutes,
            range = 0..59,
            dividersColor = MaterialTheme.colorScheme.primary,
            onValueChange = {
                onPickerMinutesChange(it)
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = "m"
        )
        NumberPicker(
            value = pickerValueSeconds,
            range = 0..59,
            dividersColor = MaterialTheme.colorScheme.primary,
            onValueChange = {
                onPickerSecondsChange(it)
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = "s"
        )
    }

}