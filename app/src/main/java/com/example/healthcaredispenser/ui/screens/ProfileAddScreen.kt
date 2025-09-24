@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.healthcaredispenser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcaredispenser.ui.theme.BorderGray
import com.example.healthcaredispenser.ui.theme.HintGray
import com.example.healthcaredispenser.ui.theme.LoginGreen
import com.example.healthcaredispenser.ui.theme.SignBg

// 피그마 감성에 가깝게 약간 타이트한 토큰
private object AddUI {
    val ScreenSide = 20.dp
    val CardRadius = 12.dp
    val CardPad = 14.dp
    val FieldGap = 10.dp
    val SectionGap = 16.dp
    val TitleTop = 8.dp
    val FieldCorner = 10.dp
    val BtnHeight = 48.dp
}

@Composable
fun ProfileAddScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }

    var genderExpanded by rememberSaveable { mutableStateOf(false) }
    var gender by rememberSaveable { mutableStateOf("남성") }

    var isPregnant by rememberSaveable { mutableStateOf(false) }
    var hasLiver by rememberSaveable { mutableStateOf(false) }
    var hasKidney by rememberSaveable { mutableStateOf(false) }
    var hasCardio by rememberSaveable { mutableStateOf(false) }

    // 정수만 허용
    fun onlyDigits(old: String, new: String) =
        if (new.all { it.isDigit() } || new.isBlank()) new else old

    val valid = name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { BackBar(onBack = onBackClick) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AddUI.ScreenSide, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    modifier = Modifier
                        .height(AddUI.BtnHeight)
                        .weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("이전") }

                Button(
                    onClick = onNextClick,
                    enabled = valid,
                    modifier = Modifier
                        .height(AddUI.BtnHeight)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoginGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFBFBFBF),
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("다음") }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = AddUI.ScreenSide)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(AddUI.TitleTop))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AddUI.CardRadius))
                    .background(SignBg)
                    .border(1.dp, BorderGray, RoundedCornerShape(AddUI.CardRadius))
                    .padding(AddUI.CardPad)
            ) {
                SectionHeader(
                    icon = Icons.Outlined.Person,
                    title = "개인 프로필",
                    subtitle = "맞춤형 건강관리를 위한 개인정보를 입력해주세요."
                )

                Spacer(Modifier.height(AddUI.SectionGap))

                // 이름 / 나이
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LabeledField(
                        label = "이름",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "홍길동",
                        modifier = Modifier.weight(1f),
                        corner = AddUI.FieldCorner
                    )
                    LabeledField(
                        label = "나이",
                        value = age,
                        onValueChange = { age = onlyDigits(age, it) },
                        placeholder = "21",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        corner = AddUI.FieldCorner
                    )
                }

                Spacer(Modifier.height(AddUI.FieldGap))

                // 키 / 체중
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LabeledField(
                        label = "키 (cm)",
                        value = height,
                        onValueChange = { height = onlyDigits(height, it) },
                        placeholder = "170",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        corner = AddUI.FieldCorner
                    )
                    LabeledField(
                        label = "체중 (kg)",
                        value = weight,
                        onValueChange = { weight = onlyDigits(weight, it) },
                        placeholder = "65",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        corner = AddUI.FieldCorner
                    )
                }

                Spacer(Modifier.height(AddUI.FieldGap))

                // 성별 드롭다운
                Text("성별", fontSize = 13.sp, color = Color.Black)
                Spacer(Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = !genderExpanded }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(AddUI.FieldCorner),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LoginGreen,
                            unfocusedBorderColor = BorderGray,
                            cursorColor = LoginGreen
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("남성") },
                            onClick = { gender = "남성"; genderExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("여성") },
                            onClick = { gender = "여성"; genderExpanded = false }
                        )
                    }
                }

                Spacer(Modifier.height(AddUI.SectionGap))

                SectionHeader(
                    icon = Icons.Outlined.Info,
                    title = "특이사항",
                    subtitle = "해당하는 항목에 체크해주세요."
                )

                Spacer(Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            CheckRow("임산부", isPregnant) { isPregnant = it }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            CheckRow("간질환", hasLiver) { hasLiver = it }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            CheckRow("신장질환", hasKidney) { hasKidney = it }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            CheckRow("심혈관질환", hasCardio) { hasCardio = it }
                        }
                    }
                }
            }
        }
    }
}

/* 섹션 아이콘 + 텍스트 */
@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = LoginGreen)
        Column {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, fontSize = 12.sp, color = HintGray)
        }
    }
}

/* 라벨 + 필드 */
@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    corner: Dp = 10.dp
) {
    Column(modifier) {
        Text(label, fontSize = 13.sp, color = Color.Black)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text(placeholder, color = HintGray) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(corner),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LoginGreen,
                unfocusedBorderColor = BorderGray,
                cursorColor = LoginGreen
            )
        )
    }
}

/* 체크박스 1줄 */
@Composable
private fun CheckRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = LoginGreen,
                uncheckedColor = BorderGray
            )
        )
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 15.sp)
    }
}

/* 공통 BackBar */
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
