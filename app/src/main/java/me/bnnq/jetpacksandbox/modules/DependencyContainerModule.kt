package me.bnnq.jetpacksandbox.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.bnnq.jetpacksandbox.services.StubAuthenticationService
import me.bnnq.jetpacksandbox.services.StubChatRepository
import me.bnnq.jetpacksandbox.services.StubMessageRepository
import me.bnnq.jetpacksandbox.services.abstractions.IAuthenticationService
import me.bnnq.jetpacksandbox.services.abstractions.IChatRepository
import me.bnnq.jetpacksandbox.services.abstractions.IMessageRepository

@Module
@InstallIn(SingletonComponent::class)
object DependencyContainerModule {
//    @Provides
//    fun provideAuthenticationService(@ApplicationContext context : Context) : IAuthenticationService {
//        return LocalAuthenticationService(context)
//    }

    private var authenticationService : IAuthenticationService? = null
    @Provides
    fun provideAuthenticationService() : IAuthenticationService {
        if (authenticationService == null)
            authenticationService = StubAuthenticationService()

        return authenticationService!!
    }

    private var chatRepository : IChatRepository? = null
    @Provides
    fun provideChatRepository() : IChatRepository {
        if (chatRepository == null)
            chatRepository = StubChatRepository()

        return chatRepository!!
    }

    private var messageRepository : IMessageRepository? = null
    @Provides
    fun provideMessageRepository() : IMessageRepository {
        if (messageRepository == null)
            messageRepository = StubMessageRepository()

        return messageRepository!!
    }

}