package com.enesky.evimiss.di

import android.content.Context
import com.enesky.evimiss.utils.AuthUtil
import com.enesky.evimiss.utils.GoogleSignInUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Created by Enes Kamil YILMAZ on 24/02/2022
 */

@Module
@InstallIn(ActivityComponent::class)
object ActivityComponentsModule {

    @Provides
    @ActivityScoped
    fun provideGoogleSignInUtil(@ActivityContext context: Context) = GoogleSignInUtil(context)

    @Provides
    @ActivityScoped
    fun provideAuthUtil(@ActivityContext context: Context) = AuthUtil(context)

}
