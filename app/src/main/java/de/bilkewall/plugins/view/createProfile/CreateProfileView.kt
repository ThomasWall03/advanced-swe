package de.bilkewall.plugins.view.createProfile

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.bilkewall.cinder.R
import de.bilkewall.plugins.theme.*
import de.bilkewall.plugins.view.utils.ErrorCard

@Composable
fun CreateProfileView(
    navController: NavController,
    viewModel: CreateProfileAndroidViewModel,
) {
    var currentDialogIndex by remember { mutableIntStateOf(0) }
    var profileName by remember { mutableStateOf("") }
    var isAlcoholic by remember { mutableStateOf(true) }

    val adapter = viewModel.createProfileViewModel
    val isLoading by adapter.isLoading.collectAsState()
    val errorMessage by adapter.errorMessage.collectAsState()

    val drinkTypeFilterValues by adapter.drinkTypeFilterValues.collectAsState()
    val ingredientFilterValues by adapter.allIngredients.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchDrinkTypeFilterValues()

        viewModel.clearSelectedOptions()
        profileName = ""
    }

    when (currentDialogIndex) {
        0 ->
            FilterDialogName(
                title = stringResource(R.string.create_profile),
                onNext = { currentDialogIndex++ },
                profileName,
                setProfileName = { profileName = it },
            )

        1 ->
            FilterDialog(
                title = stringResource(R.string.whats_your_type_filterquestion),
                options = drinkTypeFilterValues,
                selectedOptions = adapter.selectedDrinkTypeOptions.collectAsState().value,
                onOptionSelected = { options ->
                    viewModel.updateDrinkTypeFilterValues(options)
                },
                onNext = { currentDialogIndex++ },
                onBack = { currentDialogIndex-- },
                enableNextButton =
                    adapter.selectedDrinkTypeOptions
                        .collectAsState()
                        .value.size > 1,
            )

        2 ->
            FilterDialog(
                title = stringResource(R.string.what_are_you_into_filterquestion),
                options = ingredientFilterValues,
                selectedOptions = adapter.selectedIngredientOptions.collectAsState().value,
                onOptionSelected = { options ->
                    Log.d("DEBUG", "Selected options: ${options.toList()}")
                    viewModel.updateIngredientFilterValues(options)
                },
                onNext = { currentDialogIndex++ },
                onBack = { currentDialogIndex-- },
                enableNextButton =
                    adapter.selectedIngredientOptions
                        .collectAsState()
                        .value.size > 5,
            )

        3 ->
            FilterDialogAlcohol(
                title = stringResource(R.string.what_s_your_kink_filterquestion),
                isAlcoholic = isAlcoholic,
                onAlcoholicFilterChange = { isAlcoholic = it },
                onNext = {
                    viewModel.saveProfile(
                        profileName,
                    )
                    navController.navigate("mainView") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onBack = { currentDialogIndex-- },
            )
    }

    AnimatedVisibility(errorMessage.isNotEmpty()) {
        ErrorCard(
            errorHeading = stringResource(R.string.generic_error_title),
            errorInformation = errorMessage,
        )
    }
}

@Composable
fun FilterDialogName(
    title: String,
    onNext: () -> Unit,
    profileName: String,
    setProfileName: (String) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(roundedCornerShape))
                .padding(24.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(columnPaddingCreateProfileView),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = columnPaddingCreateProfileView),
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                OutlinedTextField(
                    value = profileName,
                    onValueChange = { setProfileName(it) },
                    label = { Text(stringResource(R.string.enter_profile_name)) },
                    placeholder = { Text(stringResource(R.string.placeholder_profile_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                if (profileName.isNotBlank()) onNext()
                            },
                        ),
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                        ),
                )
            }

            Button(
                onClick = {
                    onNext()
                },
                enabled = profileName.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

@Composable
fun FilterDialog(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (List<String>) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    enableNextButton: Boolean,
) {
    var selectedOptionsState =
        remember { mutableStateListOf<String>().apply { addAll(selectedOptions) } }

    val atLeastOneSelected = selectedOptionsState.isNotEmpty()

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(roundedCornerShape),
                ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            CreateProfileTitleRow(
                onBack,
                title,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                        .wrapContentHeight(align = Alignment.CenterVertically),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(4f),
            ) {
                ScrollableFlowRow(
                    options = options,
                    selectedOptions = selectedOptionsState,
                    onOptionSelected = {
                        selectedOptionsState = it as SnapshotStateList<String>
                        onOptionSelected(it)
                    },
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Column {
                    Button(
                        onClick = {
                            selectedOptionsState.clear()
                            onOptionSelected(emptyList())
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = buttonPadding),
                        enabled = atLeastOneSelected,
                    ) {
                        Text(stringResource(R.string.reset_filter))
                    }

                    Button(
                        onClick = {
                            onNext()
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = buttonPadding/2),
                        enabled = enableNextButton,
                    ) {
                        Text(stringResource(R.string.next))
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateProfileTitleRow(
    onBack: () -> Unit,
    title: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style =
                MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScrollableFlowRow(
    options: List<String>,
    selectedOptions: MutableList<String>,
    onOptionSelected: (List<String>) -> Unit,
) {
    val scrollState = rememberScrollState()
    var searchText by remember { mutableStateOf("") }

    val filteredOptions =
        options.filter {
            it.contains(searchText, ignoreCase = true)
        }

    val allSelected = selectedOptions.size == filteredOptions.size

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp),
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(stringResource(R.string.search)) },
                modifier =
                    Modifier
                        .weight(1f),
                singleLine = true,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {
                    selectedOptions.clear()
                    selectedOptions.addAll(filteredOptions)
                    onOptionSelected(selectedOptions)
                },
                enabled = !allSelected,
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(stringResource(R.string.all_filter_option))
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                filteredOptions.forEach { option ->
                    val isSelected = selectedOptions.contains(option)
                    Text(
                        text = option,
                        style =
                            MaterialTheme.typography.bodyLarge.copy(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            ),
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .background(
                                    color =
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.1f,
                                            )
                                        } else {
                                            Color.Transparent
                                        },
                                    shape = RoundedCornerShape(roundedCornerShape),
                                ).border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(roundedCornerShape),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                ).clickable {
                                    if (isSelected) {
                                        selectedOptions.remove(option)
                                    } else {
                                        selectedOptions.add(option)
                                    }
                                    onOptionSelected(selectedOptions)
                                }.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDialogAlcohol(
    title: String,
    isAlcoholic: Boolean,
    onAlcoholicFilterChange: (Boolean) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(roundedCornerShape),
                ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            CreateProfileTitleRow(
                onBack,
                title,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .wrapContentHeight(align = Alignment.CenterVertically),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(4f)
                        .padding(vertical = columnPaddingCreateProfileView),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = isAlcoholic,
                        onCheckedChange = {
                            onAlcoholicFilterChange(it)
                        },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.alcoholic_drinks_filter_question),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = { onNext() },
                    modifier = Modifier.fillMaxWidth().padding(buttonPadding/2),
                    enabled = isAlcoholic,
                ) {
                    Text(stringResource(R.string.finish))
                }
            }
        }
    }
}
