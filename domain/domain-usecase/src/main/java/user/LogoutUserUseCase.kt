package user

interface LogoutUserUseCase {
    suspend operator fun invoke(): Result<Unit>
}