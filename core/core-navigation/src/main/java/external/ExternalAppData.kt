package external

import android.net.Uri

/**
 * Sealed class defining the types of data that can be used to launch external applications.
 * Each subclass represents a specific external action.
 */
sealed class ExternalAppData {
    /**
     * Data for sending an email.
     * @param email The recipient email address.
     * @param subject Optional subject for the email.
     * @param body Optional body text for the email.
     */
    data class Email(
        val email: String,
        val subject: String? = null,
        val body: String? = null
    ) : ExternalAppData()

    /**
     * Data for opening a web link in a browser.
     * @param uri The URI of the web page to open (e.g., "https://www.example.com").
     */
    data class WebLink(val uri: String) : ExternalAppData()

    /**
     * Data for initiating a phone call.
     * @param phoneNumber The phone number to dial (e.g., "123-456-7890").
     */
    data class PhoneCall(val phoneNumber: String) : ExternalAppData()

    /**
     * Data for sharing text content with other applications.
     * @param text The text content to share.
     * @param subject Optional subject for the shared content (e.g., for email/messages).
     * @param type The MIME type of the content (e.g., "text/plain", "image/jpeg").
     */
    data class ShareContent(
        val text: String,
        val subject: String? = null,
        val type: String = "text/plain"
    ) : ExternalAppData()

    /**
     * Data for launching a specific external application, potentially with a deep link URI.
     * @param packageName The package name of the app to launch (e.g., "com.google.android.youtube").
     * @param uri Optional deep link URI to navigate within the specific app.
     */
    data class SpecificApp(
        val packageName: String,
        val uri: String? = null
    ) : ExternalAppData()

    /**
     * Data for opening a map location.
     * @param latitude The latitude coordinate.
     * @param longitude The longitude coordinate.
     * @param label Optional label to display for the location.
     */
    data class MapLocation(
        val latitude: Double,
        val longitude: Double,
        val label: String? = null
    ) : ExternalAppData()

    /**
     * Data for opening a file with an appropriate viewer.
     * @param fileUri The URI of the file to open (e.g., content:// URI from a FileProvider).
     * @param mimeType The MIME type of the file (e.g., "application/pdf", "image/png").
     */
    data class OpenFile(
        val fileUri: Uri,
        val mimeType: String
    ) : ExternalAppData()

    // Add other specific external app interactions as needed (e.g., opening camera, picking image, etc.)
}