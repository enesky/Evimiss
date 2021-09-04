package com.enesky.evimiss.ui.scaffold

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.enesky.evimiss.R
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.ui.theme.secondary

/**
 * Created by Enes Kamil YILMAZ on 04/09/2021
 */

@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    androidx.compose.material.FloatingActionButton(
        onClick = onClick,
        backgroundColor = primaryDark,
        contentColor = secondary
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "FloatingActionButton's +",
        )
    }
}