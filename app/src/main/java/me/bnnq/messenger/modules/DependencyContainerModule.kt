package me.bnnq.messenger.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.bnnq.messenger.services.StubAuthenticationService
import me.bnnq.messenger.services.StubChatRepository
import me.bnnq.messenger.services.StubMessageRepository
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.IChatRepository
import me.bnnq.messenger.services.abstractions.IMessageRepository

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