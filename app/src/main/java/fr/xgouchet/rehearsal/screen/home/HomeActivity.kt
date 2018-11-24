package fr.xgouchet.rehearsal.screen.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import fr.xgouchet.archx.ArchXActivity
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.RuntimeSchedulerProvider
import fr.xgouchet.rehearsal.core.source.AllScriptsSource
import fr.xgouchet.rehearsal.ui.Item
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeActivity
    : ArchXActivity<HomeContract.Presenter, HomeContract.View, List<Item.ViewModel>>() {

    private var disposable: Disposable? = null


    // region ArchXActivity

    override fun getPresenterKey(): String = SCREEN_NAME

    override fun instantiateFragment(): HomeContract.View {
        return HomeFragment()
    }

    override fun instantiatePresenter(): HomeContract.Presenter {
        val dataSource = AllScriptsSource(applicationContext)
        val transformer = HomeViewModelTransformer()

        return HomePresenter(dataSource, transformer, RuntimeSchedulerProvider)
    }

    // endregion

    // region ArchXActivity / FAB

    override fun getFabIcon(): Int? = R.drawable.ic_add

    override fun onFabClicked() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, OPEN_FOUNTAIN_REQUEST)
    }

    // endregion

    // region Activity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == OPEN_FOUNTAIN_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                importDocument(uri)
            }
        }
    }

    // endregion

    // region Internal

    private fun importDocument(uri: Uri) {
        disposable?.dispose()

        Toast.makeText(this, "Importing…", Toast.LENGTH_LONG).show()

        disposable = Single.create(ImportFountainDocument(applicationContext, uri))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Toast.makeText(this, "Import successful", Toast.LENGTH_LONG).show()
                            Timber.i("#import @result:$it")
                        },
                        {
                            Toast.makeText(this, "Import failed", Toast.LENGTH_LONG).show()
                            Timber.i(it, "#error #import")
                        }
                )

    }

    // endregion

    companion object {
        private const val SCREEN_NAME = "home"

        private const val OPEN_FOUNTAIN_REQUEST = 3
    }
}
