package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.xgouchet.rehearsal.core.room.model.RangeDbModel
import io.reactivex.Flowable

@Dao
interface RangeDao {


    @Transaction
    @Query("SELECT * FROM range WHERE rehearsalId = :rehearsalId")
    fun getAllInRehearsal(rehearsalId: Long): Flowable<List<RangeDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(range: RangeDbModel): Long
}
