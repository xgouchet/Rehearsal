package fr.xgouchet.rehearsal.cast

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.ui.Item

class CastActivity
    : ArchXActivity<CastContract.Presenter, CastContract.View, List<Item.ViewModel>>() {

    private var scriptId: Int = 0

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        scriptId = intent.getIntExtra(EXTRA_SCRIPT_ID, 0)

        if (scriptId <= 0) {
            finish()
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/$scriptId"

    override fun instantiateFragment(): CastContract.View {
        return CastFragment()
    }

    override fun instantiatePresenter(): CastContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = CastDataSource(applicationContext, scriptId)
        val dataSink = CastDataSink(applicationContext)
        val transformer = CastViewModelTransformer()

        return CastPresenter( dataSink, lifecycleOwner, dataSource,transformer)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "cast"

        private const val EXTRA_SCRIPT_ID = "fr.xgouchet.rehearsal.extra.SCRIPT_ID"

        @JvmStatic
        fun createIntent(context: Context, scriptId: Int): Intent {
            val intent = Intent(context, CastActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT_ID, scriptId)

            return intent
        }
    }
}
