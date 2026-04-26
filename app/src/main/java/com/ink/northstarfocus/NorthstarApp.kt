package com.ink.northstarfocus

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ink.northstarfocus.ui.theme.Apricot
import com.ink.northstarfocus.ui.theme.Cedar
import com.ink.northstarfocus.ui.theme.Coral
import com.ink.northstarfocus.ui.theme.Cream
import com.ink.northstarfocus.ui.theme.Porcelain
import com.ink.northstarfocus.ui.theme.Seafoam
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class NorthstarView(
    val label: String,
    val subtitle: String,
    val icon: ImageVector
) {
    Overview(
        label = "Overview",
        subtitle = "See today’s priorities, pick a focus block, and stay on track.",
        icon = Icons.Rounded.Home
    ),
    Planner(
        label = "Planner",
        subtitle = "Add tasks, mark priorities, and manage what needs attention.",
        icon = Icons.AutoMirrored.Rounded.List
    ),
    Reflect(
        label = "Reflect",
        subtitle = "Review routines, capture progress, and prepare for tomorrow.",
        icon = Icons.Rounded.Favorite
    )
}

private enum class DayMode(
    val label: String,
    val headline: String,
    val detail: String
) {
    Balanced(
        label = "Balanced",
        headline = "Keep the day steady and easy to follow.",
        detail = "Work through the top priorities, protect your energy, and keep the list realistic."
    ),
    Sprint(
        label = "Sprint",
        headline = "Use short focus blocks to move one important task forward.",
        detail = "A smaller list and faster decisions can help you build momentum."
    ),
    Reset(
        label = "Reset",
        headline = "Lighten the load and finish the essentials first.",
        detail = "A calmer plan still counts as progress when you close important loops."
    )
}

private enum class MissionCategory(val label: String) {
    Project("Project"),
    Study("Study"),
    Wellness("Wellness")
}

private enum class MissionFilter(val label: String) {
    All("All"),
    Priority("Priority"),
    Done("Completed")
}

private enum class FocusSession(
    val label: String,
    val minutes: Int,
    val guidance: String
) {
    Quick15("15 min", 15, "Best for a small admin task or a quick review."),
    Deep25("25 min", 25, "A strong default for focused work without fatigue."),
    Extend45("45 min", 45, "Useful when you already know the next steps."),
    Intensive60("60 min", 60, "Choose this when you need uninterrupted project time.")
}

private data class Mission(
    val id: Int,
    val title: String,
    val category: MissionCategory,
    val isPriority: Boolean,
    val isDone: Boolean
)

private data class Ritual(
    val id: Int,
    val title: String,
    val duration: String,
    val isDone: Boolean
)

private val starterMissions = listOf(
    Mission(1, "Finalize the mobile app layout and spacing", MissionCategory.Project, true, false),
    Mission(2, "Prepare a short demo flow for the presentation video", MissionCategory.Study, true, false),
    Mission(3, "Take a short stretch break after the first work block", MissionCategory.Wellness, false, true)
)

private val starterRituals = listOf(
    Ritual(1, "Check the top priority before starting", "3 min", true),
    Ritual(2, "Pause for water and an eye break", "5 min", false),
    Ritual(3, "Write the first task for tomorrow", "2 min", false)
)

@Composable
fun NorthstarApp() {
    val todayLabel = remember {
        SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
    }

    var currentViewName by rememberSaveable { mutableStateOf(NorthstarView.Overview.name) }
    var currentModeName by rememberSaveable { mutableStateOf(DayMode.Balanced.name) }
    var focusMode by rememberSaveable { mutableStateOf(true) }
    var draftMission by rememberSaveable { mutableStateOf("") }
    var selectedCategoryName by rememberSaveable { mutableStateOf(MissionCategory.Project.name) }
    var draftPriority by rememberSaveable { mutableStateOf(true) }
    var currentFilterName by rememberSaveable { mutableStateOf(MissionFilter.All.name) }
    var selectedSessionName by rememberSaveable { mutableStateOf(FocusSession.Deep25.name) }
    var todayNote by rememberSaveable { mutableStateOf("") }
    var tomorrowNote by rememberSaveable { mutableStateOf("") }
    var missions by remember { mutableStateOf(starterMissions) }
    var rituals by remember { mutableStateOf(starterRituals) }

    val currentView = NorthstarView.valueOf(currentViewName)
    val currentMode = DayMode.valueOf(currentModeName)
    val selectedCategory = MissionCategory.valueOf(selectedCategoryName)
    val currentFilter = MissionFilter.valueOf(currentFilterName)
    val selectedSession = FocusSession.valueOf(selectedSessionName)

    val visibleMissions = missions
        .filter { mission ->
            when (currentFilter) {
                MissionFilter.All -> true
                MissionFilter.Priority -> mission.isPriority
                MissionFilter.Done -> mission.isDone
            }
        }
        .filter { mission ->
            if (focusMode && currentFilter != MissionFilter.Done) {
                !mission.isDone
            } else {
                true
            }
        }
        .sortedWith(compareBy<Mission> { it.isDone }.thenByDescending { it.isPriority })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Cream, Porcelain, Color.White)
                )
            )
    ) {
        DecorativeOrb(
            size = 220.dp,
            offsetX = 210.dp,
            offsetY = (-48).dp,
            color = Apricot.copy(alpha = 0.18f)
        )
        DecorativeOrb(
            size = 180.dp,
            offsetX = (-56).dp,
            offsetY = 520.dp,
            color = Seafoam.copy(alpha = 0.14f)
        )

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                ) {
                    NorthstarView.values().forEach { view ->
                        NavigationBarItem(
                            selected = currentView == view,
                            onClick = { currentViewName = view.name },
                            icon = { Icon(view.icon, contentDescription = view.label) },
                            label = { Text(view.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                AppHeader(
                    dateLabel = todayLabel,
                    activeView = currentView
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (currentView) {
                    NorthstarView.Overview -> OverviewScreen(
                        mode = currentMode,
                        onModeSelected = { currentModeName = it.name },
                        focusMode = focusMode,
                        onFocusModeChanged = { focusMode = it },
                        missions = missions,
                        rituals = rituals,
                        selectedSession = selectedSession,
                        onSessionSelected = { selectedSessionName = it.name },
                        todayNote = todayNote,
                        onTodayNoteChanged = { todayNote = it },
                        modifier = Modifier.fillMaxSize()
                    )

                    NorthstarView.Planner -> PlannerScreen(
                        draftMission = draftMission,
                        onDraftMissionChanged = { draftMission = it },
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategoryName = it.name },
                        draftPriority = draftPriority,
                        onDraftPriorityChanged = { draftPriority = it },
                        focusMode = focusMode,
                        filter = currentFilter,
                        onFilterSelected = { currentFilterName = it.name },
                        missions = visibleMissions,
                        onAddMission = {
                            val cleanTitle = draftMission.trim()
                            if (cleanTitle.isNotEmpty()) {
                                val nextId = (missions.maxOfOrNull { it.id } ?: 0) + 1
                                missions = listOf(
                                    Mission(
                                        id = nextId,
                                        title = cleanTitle,
                                        category = selectedCategory,
                                        isPriority = draftPriority,
                                        isDone = false
                                    )
                                ) + missions
                                draftMission = ""
                            }
                        },
                        onTogglePriority = { missionId ->
                            missions = missions.map { mission ->
                                if (mission.id == missionId) {
                                    mission.copy(isPriority = !mission.isPriority)
                                } else {
                                    mission
                                }
                            }
                        },
                        onToggleDone = { missionId ->
                            missions = missions.map { mission ->
                                if (mission.id == missionId) {
                                    mission.copy(isDone = !mission.isDone)
                                } else {
                                    mission
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    NorthstarView.Reflect -> ReflectScreen(
                        rituals = rituals,
                        completedMissions = missions.count { it.isDone },
                        totalMissions = missions.size,
                        tomorrowNote = tomorrowNote,
                        onTomorrowNoteChanged = { tomorrowNote = it },
                        onToggleRitual = { ritualId ->
                            rituals = rituals.map { ritual ->
                                if (ritual.id == ritualId) {
                                    ritual.copy(isDone = !ritual.isDone)
                                } else {
                                    ritual
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun AppHeader(
    dateLabel: String,
    activeView: NorthstarView
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = dateLabel,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Northstar Focus",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = activeView.subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
        )
    }
}

@Composable
private fun OverviewScreen(
    mode: DayMode,
    onModeSelected: (DayMode) -> Unit,
    focusMode: Boolean,
    onFocusModeChanged: (Boolean) -> Unit,
    missions: List<Mission>,
    rituals: List<Ritual>,
    selectedSession: FocusSession,
    onSessionSelected: (FocusSession) -> Unit,
    todayNote: String,
    onTodayNoteChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val nextMission = missions.firstOrNull { !it.isDone } ?: missions.firstOrNull()
    val completedRituals = rituals.count { it.isDone }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            HeroCard(
                mode = mode,
                focusMode = focusMode,
                onFocusModeChanged = onFocusModeChanged
            )
        }
        item {
            SectionLabel("Work style")
        }
        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DayMode.values().forEach { option ->
                    FilterChip(
                        selected = mode == option,
                        onClick = { onModeSelected(option) },
                        label = { Text(option.label) }
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatTile(
                    modifier = Modifier.weight(1f),
                    value = missions.size.toString(),
                    label = "tasks"
                )
                StatTile(
                    modifier = Modifier.weight(1f),
                    value = missions.count { it.isDone }.toString(),
                    label = "completed"
                )
                StatTile(
                    modifier = Modifier.weight(1f),
                    value = completedRituals.toString(),
                    label = "habits"
                )
            }
        }
        item {
            FocusSessionCard(
                selectedSession = selectedSession,
                onSessionSelected = onSessionSelected
            )
        }
        item {
            TodayNoteCard(
                todayNote = todayNote,
                onTodayNoteChanged = onTodayNoteChanged
            )
        }
        item {
            NextMissionCard(nextMission = nextMission)
        }
    }
}

@Composable
private fun HeroCard(
    mode: DayMode,
    focusMode: Boolean,
    onFocusModeChanged: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent,
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Cedar, Seafoam, Apricot)
                    )
                )
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SupportBadge(text = "Daily focus")
                Text(
                    text = mode.headline,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = mode.detail,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.92f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Focus mode",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (focusMode) {
                                "Completed tasks stay hidden so the current list stays clear."
                            } else {
                                "All tasks remain visible, including completed work."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Switch(
                        checked = focusMode,
                        onCheckedChange = onFocusModeChanged,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Cedar,
                            checkedTrackColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FocusSessionCard(
    selectedSession: FocusSession,
    onSessionSelected: (FocusSession) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Focus session",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Choose how long you want your next work block to be.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FocusSession.values().forEach { session ->
                    FilterChip(
                        selected = selectedSession == session,
                        onClick = { onSessionSelected(session) },
                        label = { Text(session.label) }
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${selectedSession.minutes}-minute block selected",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = selectedSession.guidance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayNoteCard(
    todayNote: String,
    onTodayNoteChanged: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Today’s focus note",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Capture the one thing you do not want to lose sight of today.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
            )
            OutlinedTextField(
                value = todayNote,
                onValueChange = onTodayNoteChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Focus note") },
                placeholder = { Text("Write your main goal for today") }
            )
        }
    }
}

@Composable
private fun StatTile(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
private fun NextMissionCard(nextMission: Mission?) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionLabel("Current priority")
            if (nextMission == null) {
                Text(
                    text = "No active task is waiting right now. You can plan the next step in the planner view.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = nextMission.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Category: ${nextMission.category.label}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                )
                Text(
                    text = if (nextMission.isPriority) {
                        "This task is marked as high priority and should lead the next focus block."
                    } else {
                        "This is the next active item in the task list."
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun PlannerScreen(
    draftMission: String,
    onDraftMissionChanged: (String) -> Unit,
    selectedCategory: MissionCategory,
    onCategorySelected: (MissionCategory) -> Unit,
    draftPriority: Boolean,
    onDraftPriorityChanged: (Boolean) -> Unit,
    focusMode: Boolean,
    filter: MissionFilter,
    onFilterSelected: (MissionFilter) -> Unit,
    missions: List<Mission>,
    onAddMission: () -> Unit,
    onTogglePriority: (Int) -> Unit,
    onToggleDone: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Add task",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = draftMission,
                        onValueChange = onDraftMissionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Task title") },
                        placeholder = { Text("Enter a clear task name") }
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MissionCategory.values().forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { onCategorySelected(category) },
                                label = { Text(category.label) }
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Mark as priority",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Priority tasks stay easy to find when you filter the board.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Switch(
                            checked = draftPriority,
                            onCheckedChange = onDraftPriorityChanged
                        )
                    }
                    Button(
                        onClick = onAddMission,
                        enabled = draftMission.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add task")
                    }
                }
            }
        }

        if (focusMode) {
            item {
                Text(
                    text = "Focus mode is on, so completed tasks are hidden from this active list.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MissionFilter.values().forEach { option ->
                    FilterChip(
                        selected = filter == option,
                        onClick = { onFilterSelected(option) },
                        label = { Text(option.label) }
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }

        if (missions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tasks in this view.",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Add a new task above or switch filters to view another part of the board.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                        )
                    }
                }
            }
        } else {
            items(missions, key = { it.id }) { mission ->
                MissionCard(
                    mission = mission,
                    onTogglePriority = { onTogglePriority(mission.id) },
                    onToggleDone = { onToggleDone(mission.id) }
                )
            }
        }
    }
}

@Composable
private fun MissionCard(
    mission: Mission,
    onTogglePriority: () -> Unit,
    onToggleDone: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryBadge(category = mission.category)
                if (mission.isPriority) {
                    SupportBadge(
                        text = "Priority",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Text(
                text = mission.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (mission.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                text = when {
                    mission.isDone -> "Completed and ready to stay out of the way."
                    mission.isPriority -> "This is one of the most important tasks on the board."
                    else -> "A clear next step that can be finished with steady progress."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AssistChip(
                    onClick = onTogglePriority,
                    label = {
                        Text(if (mission.isPriority) "Priority set" else "Make priority")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (mission.isPriority) {
                                Icons.Rounded.Star
                            } else {
                                Icons.Rounded.StarBorder
                            },
                            contentDescription = null
                        )
                    }
                )
                Button(onClick = onToggleDone) {
                    Icon(
                        imageVector = if (mission.isDone) {
                            Icons.Rounded.RadioButtonUnchecked
                        } else {
                            Icons.Rounded.CheckCircle
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (mission.isDone) "Reopen" else "Complete")
                }
            }
        }
    }
}

@Composable
private fun ReflectScreen(
    rituals: List<Ritual>,
    completedMissions: Int,
    totalMissions: Int,
    tomorrowNote: String,
    onTomorrowNoteChanged: (String) -> Unit,
    onToggleRitual: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedRituals = rituals.count { it.isDone }
    val openMissions = totalMissions - completedMissions
    val completionRate = if (totalMissions == 0) 0 else (completedMissions * 100) / totalMissions
    val ritualRate = if (rituals.isEmpty()) 0 else (completedRituals * 100) / rituals.size

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(30.dp),
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Daily stats",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Track how much of today’s work is complete and how well your routines are holding up.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatTile(
                            modifier = Modifier.weight(1f),
                            value = completedMissions.toString(),
                            label = "done"
                        )
                        StatTile(
                            modifier = Modifier.weight(1f),
                            value = openMissions.toString(),
                            label = "left"
                        )
                        StatTile(
                            modifier = Modifier.weight(1f),
                            value = completedRituals.toString(),
                            label = "habits"
                        )
                    }
                }
            }
        }
        item {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Completion overview",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tasks finished: $completionRate%   |   Ritual consistency: $ritualRate%",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                    )
                    Text(
                        text = if (completionRate >= 70) {
                            "You cleared most of the important work today and kept good momentum."
                        } else if (completionRate >= 40) {
                            "Progress is moving. A smaller next step can help close the day well."
                        } else {
                            "The day still has open work. Pick one task to close before stopping."
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        item {
            SectionLabel("Routine checklist")
        }
        items(rituals, key = { it.id }) { ritual ->
            RitualCard(
                ritual = ritual,
                onToggle = { onToggleRitual(ritual.id) }
            )
        }
        item {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Note for tomorrow",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Leave one useful reminder so tomorrow’s first task is easier to begin.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f)
                    )
                    OutlinedTextField(
                        value = tomorrowNote,
                        onValueChange = onTomorrowNoteChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tomorrow note") },
                        placeholder = { Text("Write the first step for the next session") }
                    )
                }
            }
        }
    }
}

@Composable
private fun RitualCard(
    ritual: Ritual,
    onToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(26.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = ritual.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = ritual.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = ritual.isDone,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
private fun CategoryBadge(category: MissionCategory) {
    val containerColor = when (category) {
        MissionCategory.Project -> Seafoam.copy(alpha = 0.28f)
        MissionCategory.Study -> Apricot.copy(alpha = 0.28f)
        MissionCategory.Wellness -> Coral.copy(alpha = 0.22f)
    }

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = category.label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SupportBadge(text: String) {
    SupportBadge(
        text = text,
        containerColor = Color.White.copy(alpha = 0.18f),
        contentColor = Color.White
    )
}

@Composable
private fun SupportBadge(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = contentColor
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun DecorativeOrb(
    size: Dp,
    offsetX: Dp,
    offsetY: Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
