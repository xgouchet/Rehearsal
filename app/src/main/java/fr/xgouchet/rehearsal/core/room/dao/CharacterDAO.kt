package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import fr.xgouchet.rehearsal.core.room.model.CharacterModel

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM character WHERE characterId = :id")
    fun get(id: String): CharacterModel?

    @Query("SELECT * FROM character WHERE name = :name AND scriptId = :scriptId")
    fun getByNameInScript(name: String, scriptId: Int): CharacterModel?


    @Query("SELECT * FROM character WHERE scriptId = :scriptId ORDER BY name ASC ")
    fun getAllFromScript(scriptId: Int): LiveData<List<CharacterModel>>

    @Insert(onConflict = REPLACE)
    fun insertOrReplace(character: CharacterModel): Long

    @Update
    fun update(character: CharacterModel): Int

    @Delete
    fun delete(character: CharacterModel)

    @Query("DELETE FROM character")
    fun deleteAll()
}
