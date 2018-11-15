package fr.xgouchet.rehearsal.range

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.ui.Item

class RangePickerActivity
    : ArchXActivity<RangePickerContract.Presenter, RangePickerContract.View, List<Item.ViewModel>>() {


    private var scriptId: Int = 0

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        scriptId = intent.getIntExtra(EXTRA_SCRIPT_ID, 0)

        if (scriptId <= 0) {
            finish()
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$scriptId"

    override fun instantiateFragment(): RangePickerContract.View {
        return RangePickerFragment()
    }

    override fun instantiatePresenter(): RangePickerContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val sceneDataSource = RangeSceneDataSource(applicationContext, scriptId)
        val cueDataSourceProvider = { sceneId: Int -> RangeCueDataSource(applicationContext, sceneId) }

        return RangePickerPresenter(scriptId, lifecycleOwner, sceneDataSource, cueDataSourceProvider)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "range_picker"

        private const val EXTRA_SCRIPT_ID = "fr.xgouchet.rehearsal.extra.SCRIPT_ID"
        const val EXTRA_RANGE = "fr.xgouchet.rehearsal.extra.RANGE"

        @JvmStatic
        fun createIntent(context: Context, scriptId: Int): Intent {
            val intent = Intent(context, RangePickerActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT_ID, scriptId)

            return intent
        }

    }
}
