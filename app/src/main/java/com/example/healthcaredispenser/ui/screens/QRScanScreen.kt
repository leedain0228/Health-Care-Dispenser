@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.healthcaredispenser.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.healthcaredispenser.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import androidx.compose.foundation.border


@Composable
fun QRScanScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onSave: (String) -> Unit = {}
) {

    // 색상 (Welcome/Signup과 동일 팔레트)
    val green = Color(0xFF2E7D32)
    val borderGray = Color(0xFFD0D5DD)

    var scanned by remember { mutableStateOf("") }
    val context = LocalContext.current

    // ZXing 스캐너 런처
    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        // result.contents가 null 아니면 성공
        result.contents?.let { scanned = it }
    }


    // 카메라 권한 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val options = ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("QR 코드를 프레임 안에 맞춰주세요")
                .setBeepEnabled(true)
                .setOrientationLocked(true) // 화면 회전 잠금
            scanLauncher.launch(options)
        } else {
            // 권한 거부 시 별도 처리 필요하면 여기에서
        }
    }

    fun startScan() {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            val options = ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                .setPrompt("QR 코드를 프레임 안에 맞춰주세요")
                .setBeepEnabled(true)
                .setOrientationLocked(true)
                .setCaptureActivity(com.example.healthcaredispenser.qr.CustomCaptureActivity::class.java)
            scanLauncher.launch(options)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .offset(y = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 안내 문구
        Text(
            text = "디스펜서의 QR코드를 스캔해\n기기를 등록하세요.",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(28.dp))

        // 가운데 큰 프레임 (초록 테두리 + 카메라 아이콘)
        Box(
            modifier = Modifier
                .size(280.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(BorderStroke(2.dp, green), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.photo_camera),
                contentDescription = "카메라",
                tint = Color.Black,
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // 스캔 버튼
        Button(
            onClick = { startScan() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green)
        ) {
            Text("QR 코드 스캔하기", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))

        // 하단 취소 / 저장
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, borderGray),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("취소", fontSize = 18.sp)
            }

            Button(
                onClick = { onSave(scanned) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = scanned.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = green)
            ) {
                Text("저장", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}