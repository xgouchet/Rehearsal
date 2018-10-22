package fr.xgouchet.rehearsal.home

import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.ui.ListTransformer
import fr.xgouchet.rehearsal.core.ui.script.ScriptTransformer
import fr.xgouchet.rehearsal.core.ui.script.ScriptViewModel

class HomeActivity
    : ArchXActivity<HomeContract.Presenter, HomeContract.View, List<ScriptViewModel>>() {

    override fun getFabIcon(): Int? = R.drawable.ic_add

    override fun getPresenterKey(): String = "home"

    override fun instantiateFragment(): HomeContract.View {
        return HomeFragment()
    }

    override fun instantiatePresenter(): HomeContract.Presenter {
        val lifecycleOwner = this as LifecycleOwner
        val dataSource = HomeDataSource(applicationContext)
        val transformer = ListTransformer(ScriptTransformer())

        return HomePresenter(lifecycleOwner, dataSource, transformer)
    }

    override fun onFabClicked() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.type = APK_MIME_TYPE

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, OPEN_FOUNTAIN_REQUEST)
    }

    companion object {
        private const val OPEN_FOUNTAIN_REQUEST = 3
    }
}