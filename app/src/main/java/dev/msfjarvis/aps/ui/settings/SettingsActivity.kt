/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.Maxr1998.modernpreferences.Preference
import de.Maxr1998.modernpreferences.PreferencesAdapter
import de.Maxr1998.modernpreferences.helpers.screen
import de.Maxr1998.modernpreferences.helpers.subScreen
import dev.msfjarvis.aps.R
import dev.msfjarvis.aps.databinding.ActivityPreferenceRecyclerviewBinding
import dev.msfjarvis.aps.util.extensions.viewBinding

class SettingsActivity : AppCompatActivity() {

  private val miscSettings = MiscSettings(this)
  private val autofillSettings = AutofillSettings(this)
  private val passwordSettings = PasswordSettings(this)
  private val repositorySettings = RepositorySettings(this)
  private val generalSettings = GeneralSettings(this)
  private val pgpSettings = PGPSettings(this)

  private val binding by viewBinding(ActivityPreferenceRecyclerviewBinding::inflate)
  private val preferencesAdapter: PreferencesAdapter
    get() = binding.preferenceRecyclerView.adapter as PreferencesAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    Preference.Config.dialogBuilderFactory = { context -> MaterialAlertDialogBuilder(context) }
    val screen =
      screen(this) {
        subScreen {
          titleRes = R.string.pref_category_general_title
          iconRes = R.drawable.app_settings_alt_24px
          generalSettings.provideSettings(this)
        }
        subScreen {
          titleRes = R.string.pref_category_autofill_title
          iconRes = R.drawable.ic_wysiwyg_24px
          autofillSettings.provideSettings(this)
        }
        subScreen {
          titleRes = R.string.pref_category_passwords_title
          iconRes = R.drawable.ic_password_24px
          passwordSettings.provideSettings(this)
        }
        subScreen {
          titleRes = R.string.pref_category_repository_title
          iconRes = R.drawable.ic_call_merge_24px
          repositorySettings.provideSettings(this)
        }
        subScreen {
          titleRes = R.string.pref_category_misc_title
          iconRes = R.drawable.ic_miscellaneous_services_24px
          miscSettings.provideSettings(this)
        }
        subScreen {
          titleRes = R.string.pref_category_pgp_title
          iconRes = R.drawable.ic_lock_open_24px
          pgpSettings.provideSettings(this)
        }
      }
    val adapter = PreferencesAdapter(screen)
    adapter.onScreenChangeListener =
      PreferencesAdapter.OnScreenChangeListener { subScreen, entering ->
        supportActionBar?.title =
          if (!entering) {
            getString(R.string.action_settings)
          } else {
            getString(subScreen.titleRes)
          }
      }
    savedInstanceState
      ?.getParcelable<PreferencesAdapter.SavedState>("adapter")
      ?.let(adapter::loadSavedState)
    binding.preferenceRecyclerView.adapter = adapter
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putParcelable("adapter", preferencesAdapter.getSavedState())
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home ->
        if (!preferencesAdapter.goBack()) {
          super.onOptionsItemSelected(item)
        } else {
          true
        }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onBackPressed() {
    if (!preferencesAdapter.goBack()) super.onBackPressed()
  }
}
