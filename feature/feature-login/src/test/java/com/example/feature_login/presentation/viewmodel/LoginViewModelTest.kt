package com.example.feature_login.presentation.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
// JUnit 5 extension for coroutines testing
@ExtendWith(MainCoroutineExtension::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher() // Changed from TestCoroutineDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test login success`() = runTest {
        // Initial state
        assertNull(viewModel.loginResultMessage.value)
        assertFalse(viewModel.isLoading.value)

        // Set correct credentials
        viewModel.onUsernameChange("testuser")
        viewModel.onPasswordChange("password123")

        // Trigger login
        viewModel.onLoginClicked()

        // Advance time to immediately after isLoading is set to true
        advanceUntilIdle()
        assertTrue(viewModel.isLoading.value, "isLoading should be true immediately after onLoginClicked")

        // Advance time past the delay(1000)
        advanceTimeBy(1000)
        advanceUntilIdle() // Ensure all coroutines complete

        // Assertions after login attempt
        assertEquals("Login Successful!", viewModel.loginResultMessage.value)
        assertFalse(viewModel.isLoading.value, "isLoading should be false after login completion")
    }

    @Test
    fun `test login failure with incorrect credentials`() = runTest {
        // Initial state
        assertNull(viewModel.loginResultMessage.value)
        assertFalse(viewModel.isLoading.value)

        // Set incorrect credentials
        viewModel.onUsernameChange("wronguser")
        viewModel.onPasswordChange("wrongpassword")

        // Trigger login
        viewModel.onLoginClicked()

        // Advance time to immediately after isLoading is set to true
        advanceUntilIdle()
        assertTrue(viewModel.isLoading.value, "isLoading should be true immediately after onLoginClicked")

        // Advance time past the delay(1000)
        advanceTimeBy(1000)
        advanceUntilIdle() // Ensure all coroutines complete

        // Assertions after login attempt
        assertEquals("Error: Invalid username or password.", viewModel.loginResultMessage.value)
        assertFalse(viewModel.isLoading.value, "isLoading should be false after login completion")
    }

    @Test
    fun `test isLoading state updates correctly during login`() = runTest {
        assertFalse(viewModel.isLoading.value, "Initial isLoading state should be false.")

        viewModel.onUsernameChange("testuser")
        viewModel.onPasswordChange("password123")

        val job = launch {  // Launch the click in a separate job to observe isLoading states
            viewModel.onLoginClicked()
        }

        advanceUntilIdle() // Runs tasks scheduled before the delay
        assertTrue(viewModel.isLoading.value, "isLoading should be true right after login is triggered.")

        advanceTimeBy(1000) // Advance past the delay
        advanceUntilIdle() // Ensure coroutines after delay complete

        assertFalse(viewModel.isLoading.value, "isLoading should be false after login process completes.")
        job.cancel() // Clean up the job
    }

    @Test
    fun `login messages are cleared on input change`() = runTest {
        // Set initial error message by attempting a failed login
        viewModel.onUsernameChange("user")
        viewModel.onPasswordChange("pass")
        viewModel.onLoginClicked()
        advanceTimeBy(1001) // Ensure login attempt completes
        advanceUntilIdle()
        assertNotNull(viewModel.loginResultMessage.value, "Login message should be present after failed login.")

        // Change username and check if message is cleared
        viewModel.onUsernameChange("newuser")
        assertNull(viewModel.loginResultMessage.value, "Login message should be cleared after username change.")

        // Set another error message
        viewModel.onLoginClicked()
        advanceTimeBy(1001)
        advanceUntilIdle()
        assertNotNull(viewModel.loginResultMessage.value, "Login message should be present after failed login again.")

        // Change password and check if message is cleared
        viewModel.onPasswordChange("newpass")
        assertNull(viewModel.loginResultMessage.value, "Login message should be cleared after password change.")
    }
}

// A JUnit 5 extension to handle Main dispatcher for coroutines
@ExperimentalCoroutinesApi
class MainCoroutineExtension(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher() // Changed from TestCoroutineDispatcher
) : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: org.junit.jupiter.api.extension.ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: org.junit.jupiter.api.extension.ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
