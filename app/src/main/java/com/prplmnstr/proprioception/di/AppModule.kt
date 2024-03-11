package com.prplmnstr.proprioception.di

import android.content.Context
import androidx.room.Room
import com.prplmnstr.proprioception.database.AppDatabase
import com.prplmnstr.proprioception.database.PatientDao
import com.prplmnstr.proprioception.database.RecordDao
import com.prplmnstr.proprioception.repository.PatientRepository
import com.prplmnstr.proprioception.utils.Constants
import com.prplmnstr.proprioception.utils.bluetooth.BluetoothService
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
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context): AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE)
            .build()
    }
    @Provides
    @Singleton
    fun providePatientDao(database: AppDatabase): PatientDao {
        return database.patientDao()
    }

    @Provides
    @Singleton
    fun provideRecordDao(database: AppDatabase): RecordDao {
        return database.recordDao()
    }

    @Provides
    @Singleton
    fun provideBluetoothService(@ApplicationContext context: Context): BluetoothService{
        return BluetoothService(context)
    }

//    @Provides
//    @Singleton
//    fun providePatientRepository(patientDao: PatientDao):PatientRepository{
//        return PatientRepository(patientDao)
//    }

}
