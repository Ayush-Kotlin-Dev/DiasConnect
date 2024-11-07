package com.ayush.diasconnect.order_success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ayush.diasconnect.R
import kotlinx.coroutines.delay


data class OrderSuccessScreen(
    val onDismiss: () -> Unit
) : Screen {

    @Composable
    override fun Content() {
        OrderSuccessContent(
            modifier = Modifier.fillMaxSize(),
            onDismiss = onDismiss
        )
    }
}

@Composable
fun OrderSuccessContent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
){

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.orderconfirm))

    val progress by animateLottieCompositionAsState(composition = composition)

    LaunchedEffect(key1 = true){
        delay(3000)
        onDismiss()

    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier.size(400.dp)
        )
        Button(onClick = onDismiss) {
            Text("Continue Shopping")
        }
    }


}