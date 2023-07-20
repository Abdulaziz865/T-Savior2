package vaoolo.savi.olorom.white.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.databinding.ItemThemeBinding

class ThemeAdapter(private val onClickListener: (id: String) -> Unit) :
    ListAdapter<String, ThemeAdapter.ThemeViewHolder>(diffUtil) {

    private var context: Context? = null

    fun safeContext(context: Context?) {
        this.context = context
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ThemeViewHolder(private val binding: ItemThemeBinding) :
        ViewHolder(binding.root) {

        fun onBind(item: String) {
            binding.nameTheme.text = item
            if (bindingAdapterPosition == selectedPosition) {
                binding.nameTheme.setTextColor(Color.parseColor("#BAFFFFFF"))
                binding.theme.strokeWidth = 1
                binding.theme.strokeColor = Color.WHITE
            } else {
                binding.nameTheme.setTextColor(Color.WHITE)
                binding.theme.strokeWidth = 0
                binding.theme.strokeColor = Color.TRANSPARENT
            }
        }

        init {

            itemView.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                selectedPosition = bindingAdapterPosition

                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)

                val item = binding.nameTheme.text.toString()
                getItem(bindingAdapterPosition)?.apply { onClickListener(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        return ThemeViewHolder(
            ItemThemeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }

        if (holder.bindingAdapterPosition == selectedPosition) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.item_animation)
            holder.itemView.startAnimation(animation)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}