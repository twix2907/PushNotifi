package com.example.pushnotifi

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.pushnotifi.ui.theme.PushNotifiTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Declare the launcher at the top of your Activity/Fragment:
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // TODO: Inform user that that your app will not show notifications.
            }
        }

        fun askNotificationPermission() {
            // This is only necessary for API level >= 33 (TIRAMISU)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    // FCM SDK (and your app) can post notifications.
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
        if (!Settings.canDrawOverlays(this)) {
            val intent: Intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.setData(Uri.parse("package:$packageName"))
            startActivity(intent)
        }
        enableEdgeToEdge()
        setContent {
            PushNotifiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    askNotificationPermission()
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result

                        // Log and toast

                        Log.d(TAG, token)
                        Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PushNotifiTheme {
        Greeting("Android")
    }
}