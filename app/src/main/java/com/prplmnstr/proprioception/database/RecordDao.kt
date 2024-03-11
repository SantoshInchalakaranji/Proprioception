package com.prplmnstr.proprioception.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.model.Record
import com.prplmnstr.proprioception.utils.Constants


@Dao
interface RecordDao {

    @Insert
    suspend fun insertRecord(record: Record):Long

    @Update
    suspend fun updateRecord(record: Record):Int

    @Delete
    suspend fun deleteRecord(record: Record): Int

    @Query("SELECT * FROM ${Constants.RECORD_TABLE} WHERE patientId = :patientId")
     fun getAllRecords(patientId: Int): LiveData<List<Record>>
}