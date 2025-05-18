package de.bilkewall.plugins.view.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import de.bilkewall.cinder.R
import de.bilkewall.domain.Drink
import de.bilkewall.domain.Profile
import de.bilkewall.plugins.theme.*
import de.bilkewall.plugins.view.drinkDetail.DetailViewCard
import de.bilkewall.plugins.view.utils.CustomLoadingIndicator
import de.bilkewall.plugins.view.utils.ErrorCard

@Composable
fun MainView(
    navController: NavController,
    mainViewModel: MainAndroidViewModel,
    bottomBar: @Composable () -> Unit,
) {
    val adapter = mainViewModel.viewModel
    val profiles by adapter.allProfiles.collectAsState(initial = emptyList())
    val currentProfile by adapter.currentProfile.collectAsState(
        initial = null,
    )

    val currentDrink by adapter.currentDrink.collectAsState(initial = Drink())
    val isLoading by adapter.isLoading.collectAsState()
    val errorMessage by adapter.errorMessage.collectAsState()

    val isInitialLoad by adapter.isInitialLoad.collectAsState()
    val allDrinksMatched by adapter.allDrinksMatched.collectAsState()
    val noMoreDrinksAvailable by adapter.noMoreDrinksAvailable.collectAsState()

    var isFlipped by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (currentProfile != null) {
                TopBar(
                    profiles = profiles,
                    currentProfile = currentProfile!!,
                    onProfileSelected = { selectedProfile ->
                        mainViewModel.setCurrentProfile(selectedProfile)
                    },
                    onAddProfile = {
                        navController.navigate("createProfileView")
                    },
                    onDeleteProfile = { profile ->
                        mainViewModel.deleteProfile(profile)
                    },
                )
            }
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LaunchedEffect(Unit) {
                mainViewModel.evaluateCurrentDrink()
            }

            if (!noMoreDrinksAvailable && !allDrinksMatched) {
                ImageCard(
                    currentDrink,
                    onCheckClick = {
                        mainViewModel.handleMatchingRequest(
                            true,
                            currentDrink.drinkId,
                            currentProfile!!.profileId,
                        )
                        isFlipped = false
                    },
                    onCrossClick = {
                        mainViewModel.handleMatchingRequest(
                            false,
                            currentDrink.drinkId,
                            currentProfile!!.profileId,
                        )
                        isFlipped = false
                    },
                    isFlipped,
                    setIsFlipped = { isFlipped = it },
                    isLoading = isLoading,
                )
            } else if (isInitialLoad) {
                CustomLoadingIndicator()
            } else if (noMoreDrinksAvailable && !allDrinksMatched) {
                ErrorCard(
                    errorHeading = stringResource(R.string.no_more_drinks_title),
                    errorInformation = stringResource(R.string.no_more_drinks_information),
                    bottomComposable = {
                        Button(
                            onClick = {
                                mainViewModel.toggleFilterBypass(true)
                                mainViewModel.evaluateCurrentDrink()
                            },
                            shape = RoundedCornerShape(errorCardRoundedButton),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        ) {
                            Text(text = stringResource(R.string.no_more_drinks_button))
                        }
                    },
                )
            } else {
                ErrorCard(
                    errorHeading =
                    stringResource(
                        R.string.all_drinks_are_matched_title,
                    ),
                    errorInformation =
                    stringResource(
                        R.string.all_drinks_matched_information,
                    ),
                )
            }
        }
    }
}

@Composable
fun ImageCard(
    drink: Drink,
    onCheckClick: () -> Unit,
    onCrossClick: () -> Unit,
    isFlipped: Boolean,
    setIsFlipped: (Boolean) -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .clickable { setIsFlipped(!isFlipped) }
            .clip(RoundedCornerShape(imageCardRoundedShape))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Card(
            modifier =
            Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(imageCardRoundedShape),
            elevation = CardDefaults.cardElevation(),
        ) {
            AnimatedVisibility(!isFlipped) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = drink.thumbnailUrl,
                        contentDescription = drink.drinkName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .align(Alignment.BottomCenter),
                    ) {
                        Box(
                            modifier =
                            Modifier
                                .fillMaxSize()
                                .background(
                                    brush =
                                    Brush.verticalGradient(
                                        colors =
                                        listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.4f),
                                            Color.Black.copy(alpha = 0.6f),
                                            Color.Black.copy(alpha = 0.7f),
                                            Color.Black.copy(alpha = 1f),
                                        ),
                                    ),
                                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                ),
                        )
                    }
                }
            }
            AnimatedVisibility(isFlipped) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    DetailViewCard(
                        drink = drink,
                        bottomSpacer = 100.dp,
                    )
                }
            }
        }

        // Bottom Button Row with Cocktail Name
        BottomButtonRow(isFlipped, drink, onCrossClick, isLoading, onCheckClick)
    }
}

@Composable
private fun BottomButtonRow(
    isFlipped: Boolean,
    drink: Drink,
    onCrossClick: () -> Unit,
    isLoading: Boolean,
    onCheckClick: () -> Unit,
) {
    Row(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedVisibility(!isFlipped) {
                Text(
                    text = drink.drinkName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier =
                    Modifier
                        .padding(bottom = 8.dp),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = onCrossClick,
                    modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                        )
                        .padding(iconPadding)
                        .border(iconButtonBorderWidth, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .padding(iconPadding/2),
                    enabled = !isLoading,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        modifier = Modifier.size(iconSize),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                IconButton(
                    onClick = onCheckClick,
                    modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                        )
                        .padding(iconPadding)
                        .border(iconButtonBorderWidth, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .padding(iconPadding/2),
                    enabled = !isLoading,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cherry),
                        contentDescription = "Like Icon",
                        modifier = Modifier.size(iconSize),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    profiles: List<Profile>,
    currentProfile: Profile,
    onProfileSelected: (Profile) -> Unit,
    onAddProfile: () -> Unit,
    onDeleteProfile: (Profile) -> Unit,
) {
    var showProfileDropdown by remember { mutableStateOf(false) }
    val dropdownInteractionSource = remember { MutableInteractionSource() }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
            ) {
                // Profile Box
                Column(
                    modifier =
                    Modifier
                        .clickable { showProfileDropdown = !showProfileDropdown }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .width(profileBoxWidth),
                ) {
                    ProfileRow(
                        currentProfile,
                        R.drawable.ic_launcher_foreground,
                        onDeleteProfile,
                        false,
                    )
                    Box(
                        modifier =
                        Modifier
                            .offset(y = 5.dp),
                    ) {
                        ProfileDropDown(
                            showProfileDropdown,
                            setProfileDropdownState = { showProfileDropdown = it },
                            profiles,
                            onProfileSelected,
                            dropdownInteractionSource,
                            onAddProfile = onAddProfile,
                            onDeleteProfile,
                        )
                    }
                }
                // User Icon
                Spacer(modifier = Modifier.weight(0.5f))
                Box(modifier = Modifier.padding(end = 5.dp)) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(50.dp),
                    )
                }
            }
        },
    )
}

@Composable
private fun ProfileDropDown(
    showProfileDropdown: Boolean,
    setProfileDropdownState: (Boolean) -> Unit,
    profiles: List<Profile>,
    onProfileSelected: (Profile) -> Unit,
    dropdownInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onAddProfile: () -> Unit,
    onDeleteProfile: (Profile) -> Unit,
) {
    DropdownMenu(
        expanded = showProfileDropdown,
        onDismissRequest = { setProfileDropdownState(false) },
        modifier =
        Modifier
            .width(profileBoxWidth)
            .background(MaterialTheme.colorScheme.onPrimary),
    ) {
        profiles.forEach { profile ->
            DropdownMenuItem(
                onClick = {
                    onProfileSelected(profile)
                    setProfileDropdownState(false)
                },
                text = {
                    ProfileRow(
                        profile,
                        R.drawable.ic_launcher_foreground,
                        onDeleteProfile,
                        profiles.size > 1,
                    )
                },
                interactionSource = dropdownInteractionSource,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = profileDropDownPadding, vertical = profileDropDownPadding/2)
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(profileDropDownRoundedShape),
                    )
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(profileDropDownRoundedShape),
                    ),
            )
        }
        // Add profile item (action button)
        DropdownMenuItem(
            onClick = {
                if (profiles.size < 8) {
                    onAddProfile()
                }
                setProfileDropdownState(false)
            },
            text = {
                val messageProfile =
                    if (profiles.size < 8) {
                        Profile(0, stringResource(R.string.add_new_profile))
                    } else {
                        Profile(0, stringResource(R.string.maximum_profiles_reached))
                    }
                ProfileRow(messageProfile, R.drawable.profile_plus_icon, {}, false)
            },
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = profileDropDownPadding, vertical = profileDropDownPadding/2)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(profileDropDownRoundedShape),
                ),
        )
    }
}

@Composable
private fun ProfileRow(
    profile: Profile,
    painterResourceId: Int,
    onDelete: (Profile) -> Unit,
    showDeleteButton: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp),
    ) {
        Icon(
            painter = painterResource(id = painterResourceId),
            contentDescription = null,
            modifier =
            Modifier
                .size(30.dp)
                .offset(x = (-4).dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ),
        )
        Text(
            text = profile.profileName,
            modifier =
            Modifier
                .weight(1f)
                .padding(start = 4.dp),
        )
        if (showDeleteButton) {
            IconButton(
                onClick = { onDelete(profile) },
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete profile",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
