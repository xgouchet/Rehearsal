package fr.xgouchet.rehearsal.scene

data class CharacterInfo(
        val characterId: Int,
        val characterName: String
) {

    override fun toString(): String {
        return characterName
    }
}
