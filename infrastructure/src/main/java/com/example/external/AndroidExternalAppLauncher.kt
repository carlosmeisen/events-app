package com.example.external

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import external.ExternalAppData
import external.ExternalAppLauncher

/**
 * Android-specific implementation of [ExternalAppLauncher].
 * Uses Android Intents to launch other applications.
 * @param context The application context needed to launch intents.
 */
class AndroidExternalAppLauncher(private val context: Context) : ExternalAppLauncher {

    override fun launchApp(externalAppData: ExternalAppData) {
        // Build the appropriate Intent based on the type of ExternalAppData
        val intent: Intent = when (externalAppData) {
            is ExternalAppData.Email -> {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri() // Ensures only email apps handle this
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(externalAppData.email))
                    externalAppData.subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
                    externalAppData.body?.let { putExtra(Intent.EXTRA_TEXT, it) }
                }
            }

            is ExternalAppData.WebLink -> {
                Intent(Intent.ACTION_VIEW, externalAppData.uri.toUri())
            }

            is ExternalAppData.PhoneCall -> {
                // ACTION_DIAL opens the dialer with the number pre-filled.
                // ACTION_CALL would directly initiate a call (requires CALL_PHONE permission).
                Intent(Intent.ACTION_DIAL, "tel:${externalAppData.phoneNumber}".toUri())
            }

            is ExternalAppData.ShareContent -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = externalAppData.type
                    putExtra(Intent.EXTRA_TEXT, externalAppData.text)
                    externalAppData.subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
                }
            }

            is ExternalAppData.SpecificApp -> {
                // Try to get a launch intent for the specific package
                val launchIntent =
                    context.packageManager.getLaunchIntentForPackage(externalAppData.packageName)
                launchIntent?.apply {
                    // If a URI is provided, it's likely a deep link for that specific app
                    externalAppData.uri?.let { this.data = it.toUri() }
                }
                    ?: // If the app is not installed, direct to the Play Store
                    Intent(
                        Intent.ACTION_VIEW,
                        "market://details?id=${externalAppData.packageName}".toUri()
                    )
            }

            is ExternalAppData.MapLocation -> {
                // Format for Google Maps URI: geo:latitude,longitude?q=latitude,longitude(label)
                val uri =
                    "geo:${externalAppData.latitude},${externalAppData.longitude}?q=${externalAppData.latitude},${externalAppData.longitude}(${externalAppData.label ?: "Location"})"
                Intent(Intent.ACTION_VIEW, uri.toUri())
            }

            is ExternalAppData.OpenFile -> {
                Intent(Intent.ACTION_VIEW).apply {
                    // Set both data (Uri) and type (MIME type)
                    setDataAndType(externalAppData.fileUri, externalAppData.mimeType)
                    // Grant read permission to the receiving app for content URIs
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
        }

        // It is generally good practice to add FLAG_ACTIVITY_NEW_TASK when launching
        // an activity from a non-activity context (e.g., application context provided by Koin).
        // This ensures the new activity is launched in its own task.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            // Verify that an activity can handle this intent before starting it.
            // This helps prevent ActivityNotFoundException crashes.
            if (intent.resolveActivity(context.packageManager) != null) {
                ContextCompat.startActivity(context, intent, null)
            } else {
                // Log or show a message if no app can handle the intent
                println("No app found to handle intent: $intent for data: $externalAppData")
                // TODO: Show a user-friendly message, e.g., Toast.makeText(context, "No app found to open this.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ActivityNotFoundException) {
            // This catches scenarios where resolveActivity returned true, but starting failed
            // (e.g., a race condition or a specific permission issue after initial check).
            println("Activity not found for intent: $intent. Error: ${e.message}")
            // TODO: Show a user-friendly message
        } catch (e: Exception) {
            // Catch any other general exceptions during the launch process
            println("Error opening external app with intent: $intent. Error: ${e.message}")
            // TODO: Show a user-friendly message
        }
    }
}