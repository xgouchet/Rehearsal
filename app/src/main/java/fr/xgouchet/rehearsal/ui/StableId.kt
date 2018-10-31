package fr.xgouchet.rehearsal.ui

object StableId {

    fun getStableId(index: Int,
                    subIndex: Int,
                    type: Int)
            : Long {
        val typeL = type.toShort().toLong().shl(48)
        val subIndexL = subIndex.toShort().toLong().shl(32)
        return typeL or subIndexL or index.toLong()
    }

    fun getSubIndex(id: Long) : Int{

        return id.shr(32).and(0x0FFFF).toInt()
    }
}
