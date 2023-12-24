package me.bnnq.messenger.modules

import androidx.navigation.NavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import me.bnnq.messenger.services.ApiAuthenticationService
import me.bnnq.messenger.services.ApiChatRepository
import me.bnnq.messenger.services.ApiMessageRepository
import me.bnnq.messenger.services.abstractions.IAuthenticationService
import me.bnnq.messenger.services.abstractions.IChatRepository
import me.bnnq.messenger.services.abstractions.IMessageRepository
import me.bnnq.messenger.utils.ServiceCoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyContainerModule
{
//    @Provides
//    fun provideAuthenticationService(@ApplicationContext context : Context) : IAuthenticationService {
//        return LocalAuthenticationService(context)
//    }

    @Provides
    @Singleton
    fun provideAuthenticationService(): IAuthenticationService
    {
        return ApiAuthenticationService()
    }

    @Provides
    @Singleton
    fun provideChatRepository(): IChatRepository
    {
        return ApiChatRepository()
    }

    @Provides
    @Singleton
    fun provideMessageRepository(): IMessageRepository
    {
        return ApiMessageRepository()
    }

    @Provides
    fun provideServiceCoroutineScope(dispatcher: CoroutineDispatcher): ServiceCoroutineScope
    {
        return ServiceCoroutineScope(dispatcher)
    }

}