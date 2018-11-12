package fr.xgouchet.rehearsal.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import fr.xgouchet.rehearsal.core.room.model.CharacterModel

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM character WHERE name = :name AND scriptId = :scriptId")
    fun getByNameInScript(name: String, scriptId: Int): CharacterModel?


    @Query("SELECT * FROM character WHERE scriptId = :scriptId ORDER BY name ASC ")
    fun getAllFromScript(scriptId: Int): LiveData<List<CharacterModel>>

    @Query("SELECT character.* FROM character CROSS JOIN scene ON scene.scriptId = character.scriptId WHERE scene.sceneId = :sceneId ORDER BY name ASC ")
    fun getAllFromScene(sceneId: Int): LiveData<List<CharacterModel>>

    @Insert(onConflict = REPLACE)
    fun insertOrReplace(character: CharacterModel): Long

    @Update
    fun update(character: CharacterModel): Int
}
