package presentation.viewmodel

import GenericLogger
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import internal.AppDestination
import internal.NavigationCommand
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import language.SaveLanguagePreferenceUseCase
import model.LanguagePreference
import org.junit.After
import org.junit.Before
import org.junit.Test
import user.GetUserInfoUseCase
import user.LogoutUserUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import user.GetUserInfoUseCase
import user.LogoutUserUseCase
import user.User 

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase
    private lateinit var logoutUserUseCase: LogoutUserUseCase
    private lateinit var saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase
    private lateinit var navigationChannel: SendChannel<NavigationCommand>
    private lateinit var logger: GenericLogger

    // ViewModel under test
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher for tests

        getUserInfoUseCase = mockk(relaxed = true)
        logoutUserUseCase = mockk(relaxed = true)
        saveLanguagePreferenceUseCase = mockk(relaxed = true)
        navigationChannel = mockk(relaxed = true) // relaxed = true to ignore SendChannel's void functions like trySend
        logger = mockk(relaxed = true) // relaxed = true to ignore logging calls

        viewModel = SettingsViewModel(
            getUserInfoUseCase = getUserInfoUseCase,
            logoutUserUseCase = logoutUserUseCase,
            saveLanguagePreferenceUseCase = saveLanguagePreferenceUseCase,
            navigationChannel = navigationChannel,
            logger = logger

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase
    private lateinit var logoutUserUseCase: LogoutUserUseCase
    private lateinit var navigationChannel: Channel<NavigationCommand> // Use a real Channel for testing send/receive
    private lateinit var logger: GenericLogger

    // Mock User instance for tests
    private val mockUser = User(userName = "Test User", email = "test@example.com", authToken = "testToken", id = "123")


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUserInfoUseCase = mockk()
        logoutUserUseCase = mockk()
        navigationChannel = Channel<NavigationCommand>(Channel.UNLIMITED) // Use UNLIMITED to avoid suspension on send
        logger = mockk(relaxed = true) // Relaxed mock for logger as its calls are not critical for these tests

        // Default mock behavior for init block -  ViewModel loads initial settings
        // Let's assume user is initially logged out for most tests, can be overridden in specific tests
        coEvery { getUserInfoUseCase() } returns Result.success(null)

        viewModel = SettingsViewModel(
            getUserInfoUseCase,
            logoutUserUseCase,
            navigationChannel,
            logger

        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher after tests
    }

    @Test
    fun `confirmLanguageChange calls saveLanguagePreferenceUseCase with correct language code`() = runTest(testDispatcher) {
        val targetLanguageCode = "fr"
        val expectedPreference = LanguagePreference(targetLanguageCode)
        coEvery { saveLanguagePreferenceUseCase(any()) } returns Result.success(Unit)

        viewModel.confirmLanguageChange(targetLanguageCode)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure coroutine completes

        val languagePreferenceSlot = slot<LanguagePreference>()
        coVerify { saveLanguagePreferenceUseCase(capture(languagePreferenceSlot)) }
        assertEquals(expectedPreference.languageCode, languagePreferenceSlot.captured.languageCode)
    }

    @Test
    fun `confirmLanguageChange navigates to Home after successful language update`() = runTest(testDispatcher) {
        val targetLanguageCode = "es"
        coEvery { saveLanguagePreferenceUseCase(LanguagePreference(targetLanguageCode)) } returns Result.success(Unit)
        val expectedCommand = NavigationCommand.To(AppDestination.Home)

        viewModel.confirmLanguageChange(targetLanguageCode)
        testDispatcher.scheduler.advanceUntilIdle()


        val commandSlot = slot<NavigationCommand>()
        verify { navigationChannel.trySend(capture(commandSlot)) } // For SendChannel, trySend is often used.
                                                                    // If it was a Flow, collect would be different.

        // Verify the captured command matches the expected one.
        // This requires NavigationCommand and AppDestination to have proper equals implementations
        // or to check their properties individually.
        assert(commandSlot.captured is NavigationCommand.To)
        assertEquals(AppDestination.Home, (commandSlot.captured as NavigationCommand.To).destination)
    }

    @Test
    fun `confirmLanguageChange logs error if saveLanguagePreferenceUseCase fails`() = runTest(testDispatcher) {
        val targetLanguageCode = "de"
        val exception = RuntimeException("Failed to save language")
        coEvery { saveLanguagePreferenceUseCase(LanguagePreference(targetLanguageCode)) } returns Result.failure(exception)

        viewModel.confirmLanguageChange(targetLanguageCode)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { logger.logError(any(), coMatch { it.contains("Failed to update language preference to $targetLanguageCode") }, any()) }
        // Verify that navigation did NOT occur
        verify(exactly = 0) { navigationChannel.trySend(any()) }
        Dispatchers.resetMain()
        navigationChannel.close() // Close the channel after tests
    }

    @Test
    fun `onLogoutConfirmationRequested_setsShowDialogToTrue`() = runTest {
        // Action
        viewModel.onLogoutConfirmationRequested()

        // Assertion
        val uiState = viewModel.settingsState.first()
        assertTrue("showLogoutConfirmationDialog should be true", uiState.showLogoutConfirmationDialog)
    }

    @Test
    fun `onLogoutDialogDismissed_setsShowDialogToFalse`() = runTest {
        // Arrange: ensure dialog is shown first
        viewModel.onLogoutConfirmationRequested()
        assertTrue("Pre-condition: showLogoutConfirmationDialog should be true", viewModel.settingsState.first().showLogoutConfirmationDialog)

        // Action
        viewModel.onLogoutDialogDismissed()

        // Assertion
        val uiState = viewModel.settingsState.first()
        assertFalse("showLogoutConfirmationDialog should be false", uiState.showLogoutConfirmationDialog)
    }

    @Test
    fun `onLogoutConfirmed_whenLogoutSucceeds_updatesStateAndNavigatesHome`() = runTest(testDispatcher) {
        // Arrange: User is initially logged in
        coEvery { getUserInfoUseCase() } returns Result.success(mockUser)
        // Re-initialize ViewModel to pick up the new mock for getUserInfoUseCase in its init block
        viewModel = SettingsViewModel(getUserInfoUseCase, logoutUserUseCase, navigationChannel, logger)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure init completes

        // Ensure initial state is logged in
        var initialState = viewModel.settingsState.first()
        assertTrue("Pre-condition: User should be logged in", initialState.isUserLoggedIn)
        assertEquals("Pre-condition: Username should be set", mockUser.userName, initialState.userName)

        coEvery { logoutUserUseCase() } returns Result.success(Unit)

        // Action
        viewModel.onLogoutConfirmed()
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Assertions
        val uiState = viewModel.settingsState.first()
        assertFalse("isUserLoggedIn should be false after logout", uiState.isUserLoggedIn)
        assertNull("userName should be null after logout", uiState.userName)
        assertNull("userEmail should be null after logout", uiState.userEmail)
        assertFalse("showLogoutConfirmationDialog should be false", uiState.showLogoutConfirmationDialog)

        // Verify navigation
        val receivedCommand = navigationChannel.tryReceive().getOrNull()
        assertEquals("Navigation command should be to Home", AppDestination.Home, (receivedCommand as? NavigationCommand.To)?.destination)
    }

    @Test
    fun `onLogoutConfirmed_whenLogoutFails_updatesStateAndDoesNotNavigate`() = runTest(testDispatcher) {
        // Arrange: User is initially logged in
        coEvery { getUserInfoUseCase() } returns Result.success(mockUser)
        // Re-initialize ViewModel to pick up the new mock for getUserInfoUseCase in its init block
        viewModel = SettingsViewModel(getUserInfoUseCase, logoutUserUseCase, navigationChannel, logger)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure init completes

        // Ensure initial state is logged in
        var initialState = viewModel.settingsState.first()
        assertTrue("Pre-condition: User should be logged in", initialState.isUserLoggedIn)
        assertEquals("Pre-condition: Username should be set", mockUser.userName, initialState.userName)


        coEvery { logoutUserUseCase() } returns Result.failure(Exception("Logout failed"))

        // Action
        viewModel.onLogoutConfirmed()
        testDispatcher.scheduler.advanceUntilIdle() // Allow coroutines to complete

        // Assertions
        val uiState = viewModel.settingsState.first()
        // User should still be considered logged in if logout failed, or adjust based on actual logic
        assertTrue("isUserLoggedIn should still be true if logout failed", uiState.isUserLoggedIn)
        assertEquals("userName should remain if logout failed", mockUser.userName, uiState.userName)
        assertFalse("showLogoutConfirmationDialog should be false", uiState.showLogoutConfirmationDialog)

        // Verify navigation was not attempted or failed
        val receivedCommand = navigationChannel.tryReceive().getOrNull()
        assertNull("Navigation command should be null if logout failed", receivedCommand)

    }
}
