package com.example.sideeffecthandlers.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker

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