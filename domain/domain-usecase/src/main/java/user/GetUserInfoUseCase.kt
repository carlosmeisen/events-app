package user

interface GetUserInfoUseCase {
    suspend operator fun invoke(): Result<User?>
}