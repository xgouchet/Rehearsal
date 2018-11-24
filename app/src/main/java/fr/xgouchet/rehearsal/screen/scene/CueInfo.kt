package fr.xgouchet.rehearsal.screen.scene

data class CueInfo(
        val cueId : Long,
        val abstract : String,
        val isBookmarked: Boolean,
        val hasNote : Boolean
)
