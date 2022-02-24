package com.enesky.evimiss.di

import android.content.Context
import com.enesky.evimiss.data.repo.CalendarDataSource
import com.enesky.evimiss.data.repo.CalendarRepository
import com.enesky.evimiss.utils.PermissionsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by Enes Kamil YILMAZ on 14/02/2022
 */

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    fun provideCalendarDataSource(
        @ApplicationContext context: Context
    ) = CalendarDataSource(context.contentResolver)

    @Provides
    fun provideCalendarRepository(
        calendarDataSource: CalendarDataSource
    ) = CalendarRepository(calendarDataSource)

    @Provides
    fun providePermissionsUtil(@ApplicationContext context: Context) = PermissionsUtil(context)

}