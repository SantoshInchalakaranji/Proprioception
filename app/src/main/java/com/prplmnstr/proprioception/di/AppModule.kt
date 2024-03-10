package com.prplmnstr.proprioception.di

import android.content.Context
import androidx.room.Room
import com.prplmnstr.proprioception.database.AppDatabase
import com.prplmnstr.proprioception.database.PatientDao
import com.prplmnstr.proprioception.repository.PatientRepository
import com.prplmnstr.proprioception.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(@ApplicationContext context:Context): AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE)
            .build()
    }
    @Provides
    fun providePatientDao(database: AppDatabase): PatientDao {
        return database.patientDao()
    }

//    @Provides
//    @Singleton
//    fun providePatientRepository(patientDao: PatientDao):PatientRepository{
//        return PatientRepository(patientDao)
//    }

}
