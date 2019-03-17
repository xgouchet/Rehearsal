package fr.xgouchet.rehearsal.core.converter

import fr.xgouchet.rehearsal.core.model.Prop
import fr.xgouchet.rehearsal.core.room.AppDatabase
import fr.xgouchet.rehearsal.core.room.model.PropDbModel

class PropDbConverter
    : DbConverter<Prop, PropDbModel> {
    override fun write(appModel: Prop): PropDbModel {
        return PropDbModel(
                propId = appModel.propId,
                scriptId = appModel.scriptId,
                name = appModel.name
        )
    }

    override fun read(dataBaseModel: PropDbModel, appDatabase: AppDatabase): Prop {
        return Prop(
                propId = dataBaseModel.propId,
                scriptId = dataBaseModel.scriptId,
                name = dataBaseModel.name
        )
    }
}
