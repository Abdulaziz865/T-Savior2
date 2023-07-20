package vaoolo.savi.olorom.white.ui.fragments.theme

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.databinding.FragmentThemeBinding
import vaoolo.savi.olorom.white.ui.adapters.ThemeAdapter

@AndroidEntryPoint
class ThemeFragment : Fragment(R.layout.fragment_theme) {

    private val binding by viewBinding(FragmentThemeBinding::bind)
    private var newTheme: String? = null
    private val themeAdapter = ThemeAdapter(this::onClickItem)
    private val listOfThemes = arrayListOf(
        "Кризис экологии",
        "Интернет",
        "Криптовалюты",
        "Колонизация других планет",
        "Будущее транспорта",
        "Робототехника",
        "Биотехнология",
        "Глобальные проблемы",
        "Цифровизация образования",
        "Борьба с дезинформацией",
        "Кибербезопасность",
        "Сельское хозяйство"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListener()
    }

    private fun initialize() {
        binding.rvListOfTheme.adapter = themeAdapter
        themeAdapter.submitList(listOfThemes)
        themeAdapter.safeContext(requireContext())
    }

    private fun setupListener() {
        binding.btnTheme.setOnClickListener {
            if (newTheme != null){
                findNavController().navigate(ThemeFragmentDirections.actionThemeFragmentToChatFragment(newTheme))
            }
            else {
                Toast.makeText(requireContext(), "Тема не выбрана!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickItem(id: String) {
        newTheme = id
    }
}