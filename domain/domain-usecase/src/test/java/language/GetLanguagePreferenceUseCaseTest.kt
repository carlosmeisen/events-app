package language

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import model.LanguagePreference
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import repository.UserPreferenceRepository

class GetLanguagePreferenceUseCaseTest {
    private val repository: UserPreferenceRepository = mock()
    private val useCase = GetLanguagePreferenceUseCase(repository)

    @Test
    fun `invoke returns language preference from repository`() = runTest {
        val expectedPreference = LanguagePreference("fr")
        whenever(repository.getLanguagePreference()).thenReturn(flowOf(expectedPreference))

        useCase().test {
            assertThat(awaitItem()).isEqualTo(expectedPreference)
            awaitComplete()
        }
    }
}
