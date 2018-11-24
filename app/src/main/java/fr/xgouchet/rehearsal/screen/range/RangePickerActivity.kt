package fr.xgouchet.rehearsal.screen.range

import android.content.Context
import android.content.Intent
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Rehearsal
import fr.xgouchet.rehearsal.core.source.AllCuesInSceneSource
import fr.xgouchet.rehearsal.core.source.AllScenesInScriptSource
import fr.xgouchet.rehearsal.screen.rehearsal.RehearsalActivity
import fr.xgouchet.rehearsal.ui.DateFormatter
import fr.xgouchet.rehearsal.ui.Item

class RangePickerActivity
    : ArchXActivity<RangePickerContract.Presenter, RangePickerContract.View, List<Item.ViewModel>>() {


    private lateinit var rehearsal: Rehearsal

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        val intentRehearsal = intent.getParcelableExtra<Rehearsal>(EXTRA_REHEARSAL)

        if (intentRehearsal == null) {
            finish()
        } else {
            rehearsal = intentRehearsal
            title = DateFormatter.formatDate(intentRehearsal.dueDate)
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/${rehearsal.rehearsalId}"

    override fun instantiateFragment(): RangePickerContract.View {
        return RangePickerFragment()
    }

    override fun instantiatePresenter(): RangePickerContract.Presenter {
        val sceneDataSource = AllScenesInScriptSource(applicationContext, rehearsal.scriptId)
        val cueDataSourceProvider = { sceneId: Long -> AllCuesInSceneSource(applicationContext, sceneId) }

        return RangePickerPresenter(rehearsal.rehearsalId, sceneDataSource, cueDataSourceProvider, RuntimeSchedulerProvider)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "range_picker"

        const val EXTRA_RANGE = "fr.xgouchet.rehearsal.extra.RANGE"
        private const val EXTRA_REHEARSAL = "fr.xgouchet.rehearsal.extra.REHEARSAL"

        @JvmStatic
        fun createIntent(context: Context, rehearsal: Rehearsal): Intent {
            val intent = Intent(context, RangePickerActivity::class.java)

            intent.putExtra(EXTRA_REHEARSAL, rehearsal)

            return intent
        }

    }
}
