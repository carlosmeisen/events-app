package appinitialization

interface InitializeAppUseCase {
    suspend operator fun invoke(): Result<InitializationData>
}