package fr.xgouchet.rehearsal.screen.props

import android.content.Context
import android.content.Intent
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.source.AllPropsInScriptSource
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class PropsActivity
    : ArchXActivity<PropsContract.Presenter, PropsContract.View, List<Item.ViewModel>>() {

    private lateinit var script: Script

    // region ArchXActivity

    override fun handleIntent(intent: Intent) {
        val intentScript = intent.getParcelableExtra<Script>(EXTRA_SCRIPT)

        if (intentScript == null) {
            finish()
        } else {
            script = intentScript
            title = MarkdownConverter.parse(intentScript.title)
        }
    }

    override fun getPresenterKey(): String = "$SCREEN_NAME/${script.scriptId}"

    override fun instantiateFragment(): PropsContract.View {
        return PropsFragment()
    }

    override fun instantiatePresenter(): PropsContract.Presenter {
        val dataSource = AllPropsInScriptSource(applicationContext, script.scriptId)
        val transformer = PropViewModelTransformer()

        return PropsPresenter(dataSource, transformer, RuntimeSchedulerProvider)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "props"

        private const val EXTRA_SCRIPT = "fr.xgouchet.rehearsal.extra.SCRIPT"

        @JvmStatic
        fun createIntent(context: Context, script: Script): Intent {
            val intent = Intent(context, PropsActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT, script)

            return intent
        }
    }
}
