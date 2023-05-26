package bakiev.artour.asyncu.di

import android.content.Context
import bakiev.artour.asyncu.services.DefaultGoogleDrive
import bakiev.artour.asyncu.services.GoogleAuthenticationService
import bakiev.artour.asyncu.services.GoogleDrive
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GoogleDriveModule {
    @Provides
    fun provideGoogleDrive(
        @ApplicationContext context: Context,
        googleAuthenticationService: GoogleAuthenticationService
    ): GoogleDrive =
        DefaultGoogleDrive(context, googleAuthenticationService)
}
