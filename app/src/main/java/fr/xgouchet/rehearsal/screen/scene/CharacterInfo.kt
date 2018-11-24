package fr.xgouchet.rehearsal.screen.scene

data class CharacterInfo(
        val characterId : Long,
        val characterName: String
) {

    override fun toString(): String {
        return characterName
    }
}
