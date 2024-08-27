package com.example.sideeffecthandlers.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.sideeffecthandlers.R
import com.example.sideeffecthandlers.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun BottomSheet(
    onDismissClicked: () -> Unit,
    onCreateClick: (Task) -> Unit,
) {
    var pickerValues by remember { mutableStateOf(Triple(0, 0, 0)) }
    var taskName by remember { mutableStateOf("") }
    var isErrorInName by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun resetBottomSheetValues() {
        taskName = ""
        pickerValues = Triple(0, 0, 0)
        keyboardController?.hide()
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
                    resetBottomSheetValues()
                    isErrorInName = false
                    onDismissClicked()
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

        DurationPicker(pickerValues.first, pickerValues.second, pickerValues.third, {
            pickerValues = pickerValues.copy(first = it)
        }, {
            pickerValues = pickerValues.copy(second = it)
        }) {
            pickerValues = pickerValues.copy(third = it)
        }

        val context = LocalContext.current

        Button(
            onClick = {
                if (taskName.isEmpty()) {
                    isErrorInName = true
                } else {
                    onCreateClick(
                        Task(
                            0,
                            taskName,
                            pickerValues.first,
                            pickerValues.second,
                            pickerValues.third,
                            "Paused"
                        )
                    )
                    // Reset values after creating the task
                    resetBottomSheetValues()
                }
            }, modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Create")
        }
    }
}