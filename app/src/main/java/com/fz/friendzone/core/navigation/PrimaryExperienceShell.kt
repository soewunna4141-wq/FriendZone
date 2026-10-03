package com.fz.friendzone.core.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fz.friendzone.R

@Composable
fun PrimaryExperienceShell(
    onLogoClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    var backPressCount by remember {
        mutableIntStateOf(0)
    }

    var refreshKey by remember {
        mutableIntStateOf(0)
    }

    BackHandler {
        if (backPressCount == 0) {
            backPressCount = 1
            refreshKey += 1
        } else {
            (context as? Activity)?.finish()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        key(refreshKey) {
            content()
        }

        PrimaryFriendZoneLogoButton(
            onClick = onLogoClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 12.dp,
                    top = 12.dp
                )
        )
    }
}

@Composable
private fun PrimaryFriendZoneLogoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goldBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFF8E1),
            Color(0xFFE7C35A),
            Color(0xFFFFE79A)
        )
    )

    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .background(
                brush = goldBrush,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
    ) {
        TextButton(
            onClick = onClick
        ) {
            Image(
                painter = painterResource(
                    id = R.mipmap.ic_launcher
                ),
                contentDescription = "FriendZone",
                modifier = Modifier.size(28.dp)
            )

            Text(
                text = "FriendZone",
                modifier = Modifier.padding(start = 7.dp),
                fontSize = 16.sp,
                color = Color(0xFF5B4300)
            )
        }
    }
}
