package com.sedsoftware.yaptalker.presentation.features.posting

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.view.RxView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_new_post.new_post_button_bold
import kotlinx.android.synthetic.main.fragment_new_post.new_post_button_italic
import kotlinx.android.synthetic.main.fragment_new_post.new_post_button_link
import kotlinx.android.synthetic.main.fragment_new_post.new_post_button_underlined
import kotlinx.android.synthetic.main.fragment_new_post.new_post_edit_text
import kotlinx.android.synthetic.main.fragment_new_post.new_post_topic_title
import javax.inject.Inject

class AddMessageFragment : BaseFragment(), AddMessageView {

  companion object {
    fun getNewInstance(pair: Triple<String, String, String>): AddMessageFragment {
      val fragment = AddMessageFragment()
      val args = Bundle()
      args.putString(TOPIC_TITLE_KEY, pair.first)
      args.putString(QUOTED_TEXT_KEY, pair.second)
      args.putString(EDITED_TEXT_KEY, pair.third)
      fragment.arguments = args
      return fragment
    }

    private const val TOPIC_TITLE_KEY = "TOPIC_TITLE_KEY"
    private const val QUOTED_TEXT_KEY = "QUOTED_TEXT_KEY"
    private const val EDITED_TEXT_KEY = "EDITED_TEXT_KEY"
  }

  override val layoutId: Int
    get() = R.layout.fragment_new_post


  @Inject
  @InjectPresenter
  lateinit var presenter: AddMessagePresenter

  @ProvidePresenter
  fun provideAddMessagePresenter() = presenter

  private val currentTopicTitle: String by lazy {
    arguments?.getString(TOPIC_TITLE_KEY) ?: ""
  }

  private val quotedText: String by lazy {
    arguments?.getString(QUOTED_TEXT_KEY) ?: ""
  }

  private val editedText: String by lazy {
    arguments?.getString(EDITED_TEXT_KEY) ?: ""
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)

    if (currentTopicTitle.isNotEmpty()) {
      new_post_topic_title.text = currentTopicTitle
    }

    if (quotedText.isNotEmpty()) {
      new_post_edit_text.append(quotedText)
    } else if (editedText.isNotEmpty()) {
      new_post_edit_text.append(editedText)
    }

    subscribeViews()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_post_editor, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
      when (item.itemId) {
        R.id.action_send -> {
          returnMessageText()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }

  override fun updateCurrentUiState() {
    presenter.setAppbarTitle("")
    presenter.setNavDrawerItem(NavigationSection.FORUMS)
  }

  override fun insertTag(tag: String) {
    new_post_edit_text.text.insert(new_post_edit_text.selectionStart, tag)
  }

  override fun insertTags(openingTag: String, closingTag: String) {
    new_post_edit_text.text.insert(new_post_edit_text.selectionStart, openingTag)
    new_post_edit_text.text.insert(new_post_edit_text.selectionEnd, closingTag)
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun showLinkParametersDialogs() {

    var url = ""
    var title: String

    val titleDialog = context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.post_insert_link_title)
          .positiveText(R.string.post_button_submit)
          .negativeText(R.string.post_button_dismiss)
          .inputType(InputType.TYPE_CLASS_TEXT)
          .alwaysCallInputCallback()
          .input(R.string.post_insert_link_title_hint, 0, false, { _, _ -> })
          .onPositive { secondDialog, _ ->
            title = secondDialog.inputEditText?.text.toString()

            if (url.isNotEmpty() || title.isNotEmpty()) {
              presenter.insertVideoTag(url, title)
            }
          }
    }

    val linkDialog = context?.let { ctx ->
      MaterialDialog.Builder(ctx)
          .title(R.string.post_insert_link)
          .positiveText(R.string.post_button_submit)
          .negativeText(R.string.post_button_dismiss)
          .inputType(InputType.TYPE_CLASS_TEXT)
          .alwaysCallInputCallback()
          .input(R.string.post_insert_link_hint, 0, false, { firstDialog, firstInput ->
            firstDialog.getActionButton(DialogAction.POSITIVE).isEnabled = firstInput.toString().startsWith("http")
          })
          .onPositive { firstDialog, _ ->
            url = firstDialog.inputEditText?.text.toString()
            titleDialog?.show()
          }
    }

    linkDialog?.show()
  }

  override fun hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
  }

  private fun subscribeViews() {
    // B
    RxView
        .clicks(new_post_button_bold)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            presenter.insertChosenTag(selectionStart, selectionEnd, MessageTagCodes.TAG_B)
          }
        }

    // I
    RxView
        .clicks(new_post_button_italic)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            presenter.insertChosenTag(selectionStart, selectionEnd, MessageTagCodes.TAG_I)
          }
        }

    // U
    RxView
        .clicks(new_post_button_underlined)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            presenter.insertChosenTag(selectionStart, selectionEnd, MessageTagCodes.TAG_U)
          }
        }

    // Link
    RxView
        .clicks(new_post_button_link)
        .autoDisposable(event(FragmentLifecycle.DESTROY))
        .subscribe {
          with(new_post_edit_text) {
            presenter.insertChosenTag(selectionStart, selectionEnd, MessageTagCodes.TAG_LINK)
          }
        }
  }

  private fun returnMessageText() {
    val message = new_post_edit_text.text.toString()
    val isEdited = editedText.isNotEmpty()
    if (message.isNotEmpty()) {
      presenter.sendMessageTextBackToView(message, isEdited)
    }
  }
}
