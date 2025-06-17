package presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.feature_login.R
import presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = koinViewModel()) {
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val loginMessage by loginViewModel.loginResultMessage.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.login_button_text), // Assuming "Login" title also uses this
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { loginViewModel.onUsernameChange(it) },
                label = { Text(stringResource(id = R.string.login_username_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                enabled = !isLoading
            )

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text(stringResource(id = R.string.login_password_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading
            )

            Button(
                onClick = { loginViewModel.onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.login_button_text))
            }

            Text(
                text = stringResource(id = R.string.login_or_connect_with_text),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { /* TODO: Implement Facebook login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                enabled = !isLoading
            ) {
                Icon(
                    imageVector = Icons.Filled.Face, // Placeholder icon
                    contentDescription = "Facebook Login",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(id = R.string.login_facebook_button_text))
            }

            Button(
                onClick = { /* TODO: Implement Google login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Added padding to separate from Facebook button
                enabled = !isLoading
            ) {
                Icon(
                    imageVector = Icons.Filled.Email, // Placeholder icon
                    contentDescription = "Google Login",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(id = R.string.login_google_button_text))
            }

            // Box to hold either ProgressIndicator or LoginMessage, ensuring consistent space
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .defaultMinSize(minHeight = 48.dp), // Adjust minHeight as needed for your text/indicator size
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    loginMessage?.let { message ->
                        val displayMessage = when (message) {
                            "Login Successful!" -> stringResource(id = R.string.login_success_message)
                            "Error: Invalid username or password." -> stringResource(id = R.string.login_error_invalid_credentials)
                            else -> message // Or use R.string.login_generic_error_message for unknown errors
                        }
                        Text(
                            text = displayMessage,
                            color = if (message.startsWith("Error:")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
