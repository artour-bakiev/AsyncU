package bakiev.artour.asyncu.di

import android.content.Context
import bakiev.artour.asyncu.services.DefaultGoogleAuthenticationService
import bakiev.artour.asyncu.services.GoogleAuthenticationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {
    @Provides
    fun provideGoogleAuthenticationService(@ApplicationContext context: Context): GoogleAuthenticationService =
        DefaultGoogleAuthenticationService(context)
}
