package fr.xgouchet.rehearsal.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import fr.xgouchet.rehearsal.core.room.model.CharacterDbModel
import io.reactivex.Flowable

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM character WHERE characterId = :characterId")
    fun get(characterId: Long): CharacterDbModel?

    @Query("SELECT * FROM character WHERE name = :name AND scriptId = :scriptId")
    fun getByNameInScript(name: String, scriptId: Long): CharacterDbModel?

    @Query("SELECT * FROM character WHERE scriptId = :scriptId ORDER BY name ASC ")
    fun getAllFromScript(scriptId: Long): Flowable<List<CharacterDbModel>>

    @Query("SELECT character.* FROM character CROSS JOIN scene ON scene.scriptId = character.scriptId WHERE scene.sceneId = :sceneId ORDER BY name ASC ")
    fun getAllFromScene(sceneId: Long): Flowable<List<CharacterDbModel>>

    @Insert(onConflict = REPLACE)
    fun insertOrReplace(character: CharacterDbModel): Long

    @Update
    fun update(character: CharacterDbModel): Int
}
