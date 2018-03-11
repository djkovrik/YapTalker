package com.sedsoftware.yaptalker.presentation.features.posting

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
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
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.device.fileresolver.FilePathResolver
import com.sedsoftware.yaptalker.presentation.base.BaseFragment
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.FragmentLifecycle
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationSection
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import com.sedsoftware.yaptalker.presentation.features.posting.adapter.EmojiAdapter
import com.sedsoftware.yaptalker.presentation.features.posting.adapter.EmojiClickListener
import com.sedsoftware.yaptalker.presentation.features.posting.tags.MessageTagCodes
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.uber.autodispose.kotlin.autoDisposable
import kotlinx.android.synthetic.main.fragment_new_post.*
import kotlinx.android.synthetic.main.fragment_new_post_bottom_sheet.*
import javax.inject.Inject

@LayoutResource(value = R.layout.fragment_new_post)
class AddMessageFragment : BaseFragment(), AddMessageView, EmojiClickListener {

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

    private const val IMAGE_TYPE = "image/*"
    private const val PICK_IMAGE_REQUEST = 42
  }

  @Inject
  lateinit var emojiAdapter: EmojiAdapter

  @Inject
  lateinit var pathResolver: FilePathResolver

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

  private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

  private var chosenImagePath = ""

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)

    bottomSheetBehavior = BottomSheetBehavior.from(emojis_bottom_sheet)
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    with(emojis_list) {
      val linearLayout = GridLayoutManager(context, 2)
      layoutManager = linearLayout
      adapter = emojiAdapter
      setHasFixedSize(true)
    }

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

  override fun onBackPressed(): Boolean {
    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
      bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      return true
    }

    return false
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_post_editor, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
    when (item.itemId) {
      R.id.action_attach -> {
        presenter.onImageAttachButtonClicked()
        true
      }
      R.id.action_send -> {
        returnMessageText()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
      chosenImagePath = data?.let { pathResolver.getFilePathFromUri(data.data) } ?: ""
      handleAttachmentCardState()
    }
  }

  override fun appendEmojiItem(emoji: YapEntity) {
    emojiAdapter.addEmojiItem(emoji)
  }

  override fun clearEmojiList() {
    emojiAdapter.clearEmojiList()
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
    snackError(message)
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
            presenter.insertLinkTag(url, title)
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

  override fun showVideoLinkParametersDialog() {
    context?.let { ctx ->
      MaterialDialog.Builder(ctx)
        .title(R.string.post_insert_video)
        .positiveText(R.string.post_button_submit)
        .negativeText(R.string.post_button_dismiss)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .alwaysCallInputCallback()
        .input(R.string.post_insert_video_hint, 0, false, { _, _ -> })
        .onPositive { dialog, _ ->
          val url = dialog.inputEditText?.text.toString()
          presenter.insertVideoTag(url)
        }
        .show()
    }
  }

  override fun hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
  }

  override fun callForSmilesBottomSheet() {
    when (bottomSheetBehavior.state) {
      BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
      BottomSheetBehavior.STATE_HIDDEN -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      else -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
  }

  override fun showImagePickerDialog() {

    val getIntent = Intent(Intent.ACTION_GET_CONTENT)
    getIntent.type = IMAGE_TYPE

    val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickIntent.type = IMAGE_TYPE

    val chooserIntent = Intent.createChooser(getIntent, context?.getString(R.string.title_image_selection))
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

    startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST)
  }

  override fun onEmojiClicked(code: String) {
    presenter.insertEmoji(code)
  }

  private fun subscribeViews() {
    RxTextView
      .textChangeEvents(new_post_edit_text)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN }

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

    // Video
    RxView
      .clicks(new_post_button_video)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe {
        with(new_post_edit_text) {
          presenter.insertChosenTag(selectionStart, selectionEnd, MessageTagCodes.TAG_VIDEO)
        }
      }

    // Smiles
    RxView
      .clicks(new_post_button_smiles)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe { presenter.onSmilesButtonClicked() }

    // Attachment
    RxView
      .clicks(chosen_image_card)
      .autoDisposable(event(FragmentLifecycle.DESTROY))
      .subscribe {
        chosenImagePath = ""
        handleAttachmentCardState()
      }
  }

  private fun returnMessageText() {
    val message = new_post_edit_text.text.toString()
    val isEdited = editedText.isNotEmpty()
    if (message.isNotEmpty()) {
      presenter.sendMessageTextBackToView(message, isEdited, chosenImagePath)
    }
  }

  private fun handleAttachmentCardState() {
    if (chosenImagePath.isEmpty()) {
      chosen_image_card.hideView()
    } else {
      chosen_image_card.showView()
      chosen_image_name.text = chosenImagePath.substringAfterLast("/")
    }
  }
}
