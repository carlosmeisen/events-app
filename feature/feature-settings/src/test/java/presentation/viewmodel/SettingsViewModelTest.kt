package presentation.viewmodel

import GenericLogger
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
    }
}
