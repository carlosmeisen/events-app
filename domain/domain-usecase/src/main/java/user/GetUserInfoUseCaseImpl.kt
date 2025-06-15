package user

import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCaseImpl(
    private val userRepository: UserRepository
): GetUserInfoUseCase  {
    override suspend fun invoke(): Result<User?> {
        return userRepository.getUserInfo()
    }
}