package presentation.viewmodel

import GenericLogger
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import language.GetLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCase
import model.LanguagePreference
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageViewModelTest {

    private lateinit var viewModel: LanguageViewModel
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase = mock()
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase = mock()
    private val logger: GenericLogger = mock()
    private val testDispatcher = StandardTestDispatcher()

    // Use a MutableSharedFlow to control emissions for getLanguagePreferenceUseCase
    private val languagePreferenceFlow = MutableSharedFlow<LanguagePreference>(replay = 1)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getLanguagePreferenceUseCase()).thenReturn(languagePreferenceFlow.asSharedFlow())
        // Emit initial preference
        languagePreferenceFlow.tryEmit(LanguagePreference("en"))
        viewModel = LanguageViewModel(getLanguagePreferenceUseCase, saveLanguagePreferenceUseCase, logger)
    }

    @Test
    fun `initial state loads language preference from use case`() = runTest {
        viewModel.languageState.test {
            val initialState = awaitItem() // Should be current value from StateFlow
            assertThat(initialState.currentLanguageCode).isEqualTo("en")
            assertThat(initialState.isLoading).isFalse() // isLoading becomes false after first emission
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onLanguageSelected saves preference and updates state via flow`() = runTest {
        val newLangCode = "pt-BR"
        whenever(saveLanguagePreferenceUseCase(LanguagePreference(newLangCode))).thenReturn(Result.success(Unit))

        viewModel.languageState.test {
            // Initial state
            assertThat(awaitItem().currentLanguageCode).isEqualTo("en")

            // Action: Select a new language
            viewModel.onLanguageSelected(newLangCode)

            // Advance dispatcher to ensure coroutines launched by onLanguageSelected start
            advanceUntilIdle()

            // Expect loading state
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            // Language code might still be old one here, or updated by early emission from preferenceFlow
             assertThat(loadingState.currentLanguageCode).isEqualTo("en")


            // Simulate the language preference flow emitting the new value
            languagePreferenceFlow.emit(LanguagePreference(newLangCode))

            // Advance dispatcher again for collection of new preference
            advanceUntilIdle()

            // Expect final state with new language
            val finalState = awaitItem()
            assertThat(finalState.currentLanguageCode).isEqualTo(newLangCode)
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.error).isNull()

            verify(saveLanguagePreferenceUseCase).invoke(LanguagePreference(newLangCode))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onLanguageSelected handles save failure`() = runTest {
        val langCode = "es"
        val exception = RuntimeException("Save failed")
        whenever(saveLanguagePreferenceUseCase(LanguagePreference(langCode))).thenReturn(Result.failure(exception))
        // languagePreferenceFlow will not emit a new value since save failed

        viewModel.languageState.test {
            assertThat(awaitItem().currentLanguageCode).isEqualTo("en") // Initial

            viewModel.onLanguageSelected(langCode)

            advanceUntilIdle() // Process save coroutine

            // Expect loading state then error state
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.currentLanguageCode).isEqualTo("en")

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo("Failed to save language preference")
            assertThat(errorState.currentLanguageCode).isEqualTo("en") // Should remain old one

            verify(logger).logError("LanguageViewModel", "Failed to save language", exception)
            cancelAndConsumeRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
