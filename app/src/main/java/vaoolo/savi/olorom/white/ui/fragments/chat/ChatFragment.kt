package vaoolo.savi.olorom.white.ui.fragments.chat

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.databinding.FragmentChatBinding
import vaoolo.savi.olorom.white.data.models.BotMessage
import vaoolo.savi.olorom.white.data.models.Message
import vaoolo.savi.olorom.white.data.models.UserMessage
import vaoolo.savi.olorom.white.data.texts.TextResources
import vaoolo.savi.olorom.white.ui.adapters.ChatAdapter
import vaoolo.savi.olorom.white.utils.PreferenceHelper
import yuku.ambilwarna.AmbilWarnaDialog

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding(FragmentChatBinding::bind)
    private val chatAdapter = ChatAdapter()
    private val navArgs by navArgs<ChatFragmentArgs>()
    private var secondAndMoreMessages = false
    private var colorPicker = PreferenceHelper.isColorPicker
    private val listMessages = ArrayList(chatAdapter.currentList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
    }

    private fun initialize() {
        binding.rvListOfMessages.adapter = chatAdapter
        chatAdapter.submitList(listMessages)
        binding.chat.setBackgroundColor(colorPicker)
    }

    private fun setupListeners() = with(binding) {
        btnColorPicker.setOnClickListener {
            val colorDialog = AmbilWarnaDialog(
                requireContext(),
                colorPicker,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        colorPicker = color
                        binding.chat.setBackgroundColor(colorPicker)
                        PreferenceHelper.isColorPicker = color
                    }
                })

            colorDialog.show()
        }
        btnMore.setOnClickListener {
            findNavController().navigate(R.id.menuFragment)
        }
        btnSendMessage.setOnClickListener {
            val newMessage = etMessage.text.toString().trim()
            if (newMessage.isNotEmpty()) {
                addMessage(UserMessage("", newMessage))
                rvListOfMessages.scrollToPosition(listMessages.size - 1)
                btnSendMessage.isClickable = false
                binding.btnSendMessage.visibility = View.INVISIBLE
                startTimerAndSendMessage()
                etMessage.text.clear()
            } else {
                Toast.makeText(requireContext(), "Напишите Сообщение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMessage(message: Message) {
        listMessages.add(message)
        chatAdapter.submitList(listMessages)
    }

    private fun startTimerAndSendMessage() {
        val countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBar.visibility = View.VISIBLE
                val progress = ((2000 - millisUntilFinished) * 100 / 2000).toInt()
                binding.progressBar.progress = progress
            }

            override fun onFinish() {
                binding.progressBar.visibility = View.INVISIBLE
                binding.btnSendMessage.isClickable = true
                binding.btnSendMessage.visibility = View.VISIBLE
                addMessage(BotMessage("", sendMessageToBot()))
                chatAdapter.notifyDataSetChanged()
            }
        }
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2000

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressBar.progress = progress
        }

        animator.start()
        countDownTimer.start()
    }

    private fun sendMessageToBot(): String {
        val value = if (!secondAndMoreMessages) {
            when (navArgs.theme) {
                "Кризис экологии" -> TextResources.THEME_CRISIS_OF_ECOLOGY_TEXT
                "Интернет" -> TextResources.THEME_INTERNET_TEXT
                "Криптовалюты" -> TextResources.THEME_CRYPTOCURRENCIES_TEXT
                "Колонизация других планет" -> TextResources.THEME_COLONIZATION_OF_OTHER_PLANETS_TEXT
                "Будущее транспорта" -> TextResources.THEME_FUTURE_TRANSPORT_TEXT
                "Робототехника" -> TextResources.THEME_ROBOTICS_TEXT
                "Биотехнология" -> TextResources.THEME_BIOTECHNOLOGY_TEXT
                "Глобальные проблемы" -> TextResources.THEME_GLOBAL_PROBLEMS_TEXT
                "Цифровизация образования" -> TextResources.THEME_DIGITALIZATION_OF_EDUCATION_TEXT
                "Борьба с дезинформацией" -> TextResources.THEME_FIGHTING_DISINFORMATION_TEXT
                "Кибербезопасность" -> TextResources.THEME_CYBER_SECURITY_TEXT
                "Сельское хозяйство" -> TextResources.THEME_AGRICULTURE_TEXT
                else -> TextResources.THEME_CRISIS_OF_ECOLOGY_TEXT
            }
        } else {
            TextResources.SCRIPT_TEXT
        }
        secondAndMoreMessages = true
        return value
    }
}