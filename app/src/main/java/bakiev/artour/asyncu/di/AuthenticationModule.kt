package bakiev.artour.asyncu.di

import android.content.Context
import bakiev.artour.asyncu.services.DefaultGoogleSignInService
import bakiev.artour.asyncu.services.GoogleSignInService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    fun provideGoogleSignInTest(@ApplicationContext context: Context): GoogleSignInService =
        DefaultGoogleSignInService(context)
}
