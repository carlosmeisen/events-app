package language

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import model.LanguagePreference
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import repository.UserPreferenceRepository

class SaveLanguagePreferenceUseCaseTest {
    private val repository: UserPreferenceRepository = mock()
    private val useCase = SaveLanguagePreferenceUseCase(repository)

    @Test
    fun `invoke calls repository save and returns success`() = runTest {
        val preferenceToSave = LanguagePreference("es")
        // `repository.saveLanguagePreference` is a suspend fun returning Unit,
        // so no specific `thenReturn` needed for success path unless it can throw.
        // Mockito will do nothing by default for suspend fun returning Unit.

        val result = useCase(preferenceToSave)

        assertThat(result.isSuccess).isTrue()
        verify(repository).saveLanguagePreference(preferenceToSave)
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val preferenceToSave = LanguagePreference("de")
        val expectedException = RuntimeException("Database error")
        whenever(repository.saveLanguagePreference(preferenceToSave)).thenThrow(expectedException)

        val result = useCase(preferenceToSave)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(expectedException)
        verify(repository).saveLanguagePreference(preferenceToSave)
    }
}
