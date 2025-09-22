@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package com.example.healthcaredispenser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthcaredispenser.navigation.Routes
import com.example.healthcaredispenser.ui.theme.HintGray
import com.example.healthcaredispenser.ui.theme.LoginGreen
import com.example.healthcaredispenser.ui.theme.SignBg

// === 피그마 비율 토큰 ===
private object HabitsUI {
    val ScreenSide = 20.dp
    val TitleTop = 8.dp
    val TitleSize = 22.sp
    val SubTitleTop = 6.dp
    val SubTitleSize = 14.sp

    val CardHeight = 64.dp
    val CardRadius = 14.dp
    val CardGap = 14.dp
    val IconSize = 26.dp

    val BtnWidth = 140.dp
    val BtnHeight = 52.dp
    val BtnRadius = 14.dp
    val BtnHorizontal = 24.dp
    val BtnVertical = 20.dp
}

// 습관 항목
private data class HabitItem(val label: String, val icon: ImageVector)

@Composable
fun HabitsScreen(
    navController: NavController
) {
    val items = remember {
        listOf(
            HabitItem("술을 자주 마셔요", Icons.Outlined.LocalBar),
            HabitItem("채식위주로 식사해요", Icons.Outlined.Spa),
            HabitItem("운동/땀 많아요", Icons.Outlined.FitnessCenter),
            HabitItem("수면이 불규칙해요", Icons.Outlined.NightsStay),
            HabitItem("카페인을 많이 섭취해요", Icons.Outlined.Coffee),
            HabitItem("스트레스가 많아요", Icons.Outlined.SentimentDissatisfied),
            HabitItem("짠 음식을 즐겨요", Icons.Outlined.Fastfood)
        )
    }

    val selected = remember { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(Unit) { items.forEach { selected[it.label] = false } }

    val chosen = selected.filterValues { it }.keys.toList()
    val canProceed = chosen.size >= 3

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Top + WindowInsetsSides.Horizontal
        ),
        topBar = {
            BackBar(onBack = { navController.popBackStack() })
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HabitsUI.BtnHorizontal, vertical = HabitsUI.BtnVertical),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        // ✅ 선택 습관 저장 → 프로필 추가 화면으로 이동
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("chosenHabits", ArrayList(chosen))
                        navController.navigate(Routes.PROFILE_ADD)
                    },
                    enabled = canProceed,
                    modifier = Modifier
                        .width(HabitsUI.BtnWidth)
                        .height(HabitsUI.BtnHeight)
                        .offset(y = (-12).dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoginGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFBFBFBF),
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(HabitsUI.BtnRadius)
                ) {
                    Text("다음", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = HabitsUI.ScreenSide),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(HabitsUI.TitleTop))

            Text(
                text = "생활습관 선택",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = HabitsUI.TitleSize,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(Modifier.height(HabitsUI.SubTitleTop))
            Text(
                text = "최소 3개 이상 선택해주세요.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = HabitsUI.SubTitleSize,
                color = HintGray
            )

            Spacer(Modifier.height(18.dp))

            items.forEachIndexed { idx, item ->
                val isOn = selected[item.label] == true
                HabitRow(
                    label = item.label,
                    icon = item.icon,
                    selected = isOn,
                    onToggle = { selected[item.label] = !isOn }
                )
                if (idx != items.lastIndex) Spacer(Modifier.height(HabitsUI.CardGap))
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun HabitRow(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onToggle: () -> Unit
) {
    val bg = if (selected) SignBg else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(HabitsUI.CardHeight)
            .clip(RoundedCornerShape(HabitsUI.CardRadius))
            .border(1.dp, LoginGreen, RoundedCornerShape(HabitsUI.CardRadius))
            .background(bg)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = LoginGreen, modifier = Modifier.size(HabitsUI.IconSize))
        Spacer(Modifier.width(16.dp))
        Text(text = label, color = Color.Black, fontSize = 16.sp, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BackBar(
    onBack: () -> Unit,
    startPadding: Dp = 16.dp,
    topPadding: Dp = 16.dp,
    endPadding: Dp = 16.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = startPadding, top = topPadding, end = endPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로", tint = Color.Black)
        }
    }
}
