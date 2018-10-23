package fr.xgouchet.rehearsal.scene

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.ui.ItemListFragment

class SceneFragment
    : ItemListFragment(),
        SceneContract.View {

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.scene, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_cast -> {
                (presenter as? SceneContract.Presenter)?.onCastActionSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // endregion



    // region SceneContract.View

    override fun onItemSelected(item: Any) {
        (presenter as? SceneContract.Presenter)?.onItemSelected(item)
    }

    // endregion
}
