package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.xgouchet.rehearsal.core.room.model.PropCueDbModel
import fr.xgouchet.rehearsal.core.room.model.PropDbModel
import io.reactivex.Flowable

@Dao
interface PropDAO {

    @Query("SELECT * FROM prop WHERE propId = :propId")
    fun get(propId: Long): PropDbModel?

    @Query("SELECT * FROM propcue JOIN prop ON propcue.propId = prop.propId WHERE cueId = :cueId ")
    fun getAllFromCue(cueId: Long): List<PropDbModel>

    @Query("SELECT * FROM prop WHERE scriptId = :scriptId ORDER BY name ASC ")
    fun getAllFromScript(scriptId: Long): Flowable<List<PropDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(prop: PropDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(propCue: PropCueDbModel): Long

    @Query("SELECT * FROM propcue WHERE cueId = :cueId AND propId = :propId")
    fun getLink(propId: Long, cueId: Long): PropCueDbModel?

    @Query("SELECT * FROM propcue WHERE cueId = :cueId")
    fun getAllLinksFromCue(cueId: Long): List<PropCueDbModel>

    @Delete
    fun delete(propLink: PropCueDbModel)

}
