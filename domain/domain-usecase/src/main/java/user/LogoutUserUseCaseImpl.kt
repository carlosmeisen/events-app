package user

class LogoutUserUseCaseImpl(
    private val userRepository: UserRepository
) : LogoutUserUseCase {
    override suspend fun invoke(): Result<Unit> {
        return userRepository.logoutUser()
    }
}