package org.d3if3091.tempconverter.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3091.tempconverter.R
import org.d3if3091.tempconverter.model.TempType
import org.d3if3091.tempconverter.navigation.Screen
import org.d3if3091.tempconverter.ui.theme.TempConverterTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navHostController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { navHostController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            tint = MaterialTheme.colorScheme.background,
                            contentDescription = stringResource(id = R.string.about_button_description)
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(modifier = Modifier.padding(padding))
    }
}

@Composable
private fun ScreenContent(modifier: Modifier) {
    val viewModel: MainViewModel = viewModel()
    val tempType = viewModel.tempType
    val context = LocalContext.current
    var selectedFromType by rememberSaveable {
        mutableStateOf("celsius")
    }
    var selectedToType by rememberSaveable {
        mutableStateOf("fahrenheit")
    }
    var tempResultType by rememberSaveable {
        mutableStateOf("")
    }
    var tempValueType by rememberSaveable {
        mutableStateOf("")
    }
    var tempValue by rememberSaveable {
        mutableStateOf("")
    }
    var tempResult by rememberSaveable {
        mutableStateOf("")
    }
    var tempInitial by rememberSaveable {
        mutableStateOf("")
    }
    var tempValueError by remember {
        mutableStateOf(false)
    }
    var expandedDropdownMenuFrom by remember { mutableStateOf(false) }
    var expandedDropdownMenuTo by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.overview),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TempTypeDropdown(
            label = stringResource(id = R.string.from),
            expanded = expandedDropdownMenuFrom,
            tempType = tempType,
            value = selectedFromType,
            onExpandedChange = { bool -> expandedDropdownMenuFrom = bool },
            onValueChange = { value ->
                selectedFromType = value
            })
        TempTypeDropdown(
            label = stringResource(id = R.string.to),
            expanded = expandedDropdownMenuTo,
            value = selectedToType,
            onExpandedChange = { bool -> expandedDropdownMenuTo = bool },
            tempType = tempType,
            onValueChange = { value ->
                selectedToType = value
            })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = tempValue,
            onValueChange = { tempValue = it },
            label = { Text(text = stringResource(id = R.string.temp_value)) },
            isError = tempValueError,
            singleLine = true,
            supportingText = {
                Text(
                    text = if (tempValueError) stringResource(id = R.string.temp_value_error) else stringResource(
                        id = R.string.temp_value_hint
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            trailingIcon = { Text(text = stringResource(id = tempType[selectedFromType]!!.symbolResId)) }
        )
        Button(
            modifier = Modifier.padding(vertical = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp),
            onClick = {
                tempValueError = (tempValue == "")
                if (tempValueError) return@Button
                tempResultType = selectedToType
                tempValueType = selectedFromType
                tempInitial = tempValue
                tempResult =
                    calculateConvertTemp(
                        tempType[selectedFromType]!!,
                        tempType[selectedToType]!!,
                        tempValue.toFloat()
                    ).toString()
            }) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (tempResult !== "") {
            Divider()
            Text(text = stringResource(id = R.string.input_result_template,tempInitial.toFloat(),
                stringResource(tempType[tempValueType]!!.symbolResId)
            ))
            Text(text = stringResource(id = R.string.result),modifier = Modifier.padding(top = 8.dp))
            Text(
                text = tempResult + " " + stringResource(tempType[tempResultType]!!.symbolResId),
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp),
                    onClick = {
                        shareData(
                            context = context, message = context.getString(
                                R.string.share_template,
                                context.getString(tempType[tempValueType]!!.titleResId),
                                context.getString(tempType[tempResultType]!!.titleResId),
                                tempInitial.toFloat(),
                                context.getString(tempType[tempValueType]!!.symbolResId),
                                tempResult.toFloat(),
                                context.getString(tempType[tempResultType]!!.symbolResId),
                            )
                        )
                    }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = stringResource(id = R.string.share))
                    Text(modifier = Modifier.padding(horizontal = 8.dp),text = stringResource(id = R.string.share))
                }
            }
        }
    }
}

fun calculateConvertTemp(
    from: TempType,
    to: TempType,
    value: Float
): Float {
    return from.formula[to.name]!!(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TempTypeDropdown(
    label: String,
    expanded: Boolean,
    value: String,
    tempType: Map<String, TempType>,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit
) {

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
    ) {
        OutlinedTextField(
            value = stringResource(id = tempType[value]!!.titleResId),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            label = { Text(text = label) }
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }) {
            tempType.keys.forEach { text ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = stringResource(tempType[text]!!.titleResId)) },
                    onClick = {
                        onExpandedChange(false)
                        onValueChange(text)
                    })
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainPreview() {
    TempConverterTheme {
        MainScreen(navHostController = rememberNavController())
    }
}