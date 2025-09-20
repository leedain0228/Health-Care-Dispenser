@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.healthcaredispenser.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthcaredispenser.navigation.Routes
import com.example.healthcaredispenser.ui.theme.LoginGreen
import com.example.healthcaredispenser.ui.theme.HintGray

data class TempProfile(
    val name: String,
    val habits: List<String> = emptyList()
)

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val profiles = remember {
        mutableStateListOf(
            TempProfile("suehah"),
            TempProfile("dain"),
            TempProfile("sehyeon")
        )
    }

    val newProfile = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<TempProfile?>("newProfile", null)
        ?.collectAsState(initial = null)?.value

    LaunchedEffect(newProfile) {
        newProfile?.let {
            profiles += it
            navController.currentBackStackEntry?.savedStateHandle?.set("newProfile", null)
        }
    }

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로", tint = Color.Black)
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(96.dp))

            // 제목
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "어떤 프로필로 시작할까요?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(28.dp))

            // 프로필 3개
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                profiles.take(3).forEach { p ->
                    ProfileAvatarCanvas(
                        diameter = 88.dp,
                        stroke = 2.dp,
                        color = LoginGreen,
                        label = p.name,
                        onClick = { /* TODO */ }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // + 버튼
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { navController.navigate(Routes.HABITS) },
                    shape = CircleShape,
                    color = LoginGreen,
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "프로필 추가",
                            tint = Color.White
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text("프로필 추가", fontSize = 14.sp, color = Color.Black)
            }

            Spacer(Modifier.height(150.dp))
        }
    }
}

@Composable
private fun ProfileAvatarCanvas(
    diameter: Dp,
    stroke: Dp,
    color: Color,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(diameter)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokePx = stroke.toPx()
                val radius = size.minDimension / 2f - strokePx / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                // 바깥 원
                drawCircle(
                    color = color,
                    radius = radius,
                    center = center,
                    style = Stroke(width = strokePx)
                )

                // 머리
                val headRadius = radius * 0.32f
                val headCenter = Offset(center.x, center.y - radius * 0.28f)
                drawCircle(
                    color = color,
                    radius = headRadius,
                    center = headCenter,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )

                // 어깨 (더 아래 위치)
                val shoulderRadius = radius * 0.62f
                val y = center.y + radius * 0.7f
                drawArc(
                    color = color,
                    startAngle = 200f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(center.x - shoulderRadius, y - shoulderRadius),
                    size = androidx.compose.ui.geometry.Size(shoulderRadius * 2, shoulderRadius * 2),
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(label, fontSize = 14.sp, color = Color.Black)
    }
}
