package fr.xgouchet.rehearsal.screen.script

import android.content.Context
import android.content.Intent
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.model.Script
import fr.xgouchet.rehearsal.core.sink.SingleScriptSink
import fr.xgouchet.rehearsal.core.source.AllScenesInScriptSource
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.MarkdownConverter

class ScriptActivity
    : ArchXActivity<ScriptContract.Presenter, ScriptContract.View, List<Item.ViewModel>>() {


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

    override fun instantiateFragment(): ScriptContract.View {
        return ScriptFragment()
    }

    override fun instantiatePresenter(): ScriptContract.Presenter {
        val dataSource = AllScenesInScriptSource(applicationContext, script.scriptId)
        val scriptDataSink = SingleScriptSink(applicationContext)
        val transformer = ScriptViewModelTransformer()

        return ScriptPresenter(script, dataSource, scriptDataSink, transformer, RuntimeSchedulerProvider)
    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "script"

        private const val EXTRA_SCRIPT = "fr.xgouchet.rehearsal.extra.SCRIPT"

        @JvmStatic
        fun createIntent(context: Context, script: Script): Intent {
            val intent = Intent(context, ScriptActivity::class.java)

            intent.putExtra(EXTRA_SCRIPT, script)

            return intent
        }
    }
}
