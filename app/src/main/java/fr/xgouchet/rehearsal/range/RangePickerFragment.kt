package fr.xgouchet.rehearsal.range

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import fr.xgouchet.rehearsal.R
import fr.xgouchet.rehearsal.core.room.model.RangeModel
import fr.xgouchet.rehearsal.ui.ACTION_DEFAULT
import fr.xgouchet.rehearsal.ui.ACTION_VALUE_CHANGED
import fr.xgouchet.rehearsal.ui.Item
import fr.xgouchet.rehearsal.ui.ItemListFragment

class RangePickerFragment
    : ItemListFragment(),
        RangePickerContract.View {

    private var isValid: Boolean = false

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.form, menu)

        menu.findItem(R.id.action_validate).isVisible = isValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_validate) {
            (presenter as? RangePickerContract.Presenter)?.onValidate()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // endregion

    // region ItemListFragment

    override fun onItemAction(item: Item.ViewModel, action: String, value: String?): Boolean {
        var consumed = true
        when (action) {
            ACTION_DEFAULT -> (presenter as? RangePickerContract.Presenter)?.onItemSelected(item)
            else -> consumed = false
        }
        return consumed
    }

    // endregion

    // region RangePickerContract.View


    override fun showValidate(valid: Boolean) {
        this.isValid = valid
        activity?.invalidateOptionsMenu()
    }

    override fun showScenePicker(scenes: List<Pair<Int, String>>) {
        val currentActivity = activity ?: return
        val sceneValues = scenes.map { it.second }.toTypedArray()

        val builder = AlertDialog.Builder(currentActivity)
                .setTitle(R.string.rangePicker_prompt_scene)
                .setIcon(R.drawable.ic_scene)
                .setItems(sceneValues) { d, w ->
                    val sceneId = scenes[w].first
                    (presenter as? RangePickerContract.Presenter)?.onScenePicked(sceneId)
                    d.dismiss()
                }


        val dialog = builder.create()
        dialog.show()
    }

    override fun showCuePicker(requestId: Int, cues: List<Pair<Int, String>>) {
        val currentActivity = activity ?: return
        val sceneValues = cues.map { it.second }.toTypedArray()

        val builder = AlertDialog.Builder(currentActivity)
                .setTitle(R.string.rangePicker_prompt_cue)
                .setIcon(R.drawable.ic_cue_dialog)
                .setItems(sceneValues) { d, w ->
                    val cueId = cues[w].first
                    (presenter as? RangePickerContract.Presenter)?.onCuePicked(requestId, cueId)
                    d.dismiss()
                }


        val dialog = builder.create()
        dialog.show()
    }

    override fun navigateBackWithResult(rangeModel: RangeModel) {
        val currentActivity = activity ?: return

        val data = Intent()
        data.putExtra(RangePickerActivity.EXTRA_RANGE, rangeModel)

        currentActivity.setResult(Activity.RESULT_OK, data)
        currentActivity.finish()
    }

    // endregion
}
