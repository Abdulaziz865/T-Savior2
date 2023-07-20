package vaoolo.savi.olorom.white.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vaoolo.savi.olorom.R
import vaoolo.savi.olorom.white.data.models.BotMessage
import vaoolo.savi.olorom.white.data.models.Message
import vaoolo.savi.olorom.white.data.models.UserMessage

class ChatAdapter() :
    ListAdapter<Message, ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_USER_MESSAGE -> {
                val userView = inflater.inflate(R.layout.item_user_message, parent, false)
                UserMessageViewHolder(userView)
            }
            TYPE_BOT_MESSAGE -> {
                val botView = inflater.inflate(R.layout.item_bot_message, parent, false)
                BotMessageViewHolder(botView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)

        when (holder) {
            is UserMessageViewHolder -> {
                holder.bind(message as UserMessage)
            }
            is BotMessageViewHolder -> {
                holder.bind(message as BotMessage)
            }
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return when (message) {
            is UserMessage -> TYPE_USER_MESSAGE
            is BotMessage -> TYPE_BOT_MESSAGE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    inner class UserMessageViewHolder(itemView: View) : ViewHolder(itemView) {

        private val userMessageTextView: TextView = itemView.findViewById(R.id.txt_user_message)

        fun bind(message: UserMessage) {
            userMessageTextView.text = message.text
        }
    }

    inner class BotMessageViewHolder(itemView: View) : ViewHolder(itemView) {

        private val botMessageTextView: TextView = itemView.findViewById(R.id.txt_bot_message)

        fun bind(message: BotMessage) {
            botMessageTextView.text = message.text
        }
    }

    companion object {

        private const val TYPE_USER_MESSAGE = 0
        private const val TYPE_BOT_MESSAGE = 1

        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}