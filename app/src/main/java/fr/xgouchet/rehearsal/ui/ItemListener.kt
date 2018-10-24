package fr.xgouchet.rehearsal.ui

/**
 * The parameters are :
 *  - the data
 *  - the action
 *  - an optional action value parameter (for editors)
 */
typealias ItemListener = (Any, String, String?) -> Unit

const val ACTION_DEFAULT = ""
const val ACTION_VALUE_CHANGED = "changed"
