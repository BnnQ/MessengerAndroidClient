package me.bnnq.messenger.services

import me.bnnq.messenger.models.Message
import me.bnnq.messenger.services.abstractions.IMessageRepository
import java.util.Date

//@Suppress("DEPRECATION")
//class StubMessageRepository : IMessageRepository
//{
//    private val messages: ArrayList<Message> = arrayListOf(
//        Message(2, "1", "Sed egestas dapibus nibh a", Date(2023, 11, 12, 18, 27, 15)),
//        Message(
//            2,
//            "2",
//            "Cras sapien elit, placerat ac odio in, pulvinar feugiat massa.",
//            Date(2023, 11, 12, 18, 27, 45)),
//        Message(1, "1", "Curabitur tincidunt eros?", Date(2023, 11, 12, 18, 28, 15)),
//        Message(
//            2,
//            "2",
//            "Vestibulum accumsan ipsum sem, eu consequat lacus efficitur ut. Etiam faucibus consectetur ornare. Duis imperdiet.",
//            Date(2023, 11, 12, 18, 28, 35)),
//        Message(2, "1", "vestibulum interdum tempus magna)", Date(2023, 11, 12, 18, 29, 15)),
//        Message(2, "1", "Integer molestie", Date(2023, 11, 12, 18, 29, 47)),
//        Message(2, "2", "Suspendisse potenti. Duis sed libero nec", Date(2023, 11, 12, 18, 30, 15)),
//        Message(2, "2", "Phasellus tincidunt?", Date(2023, 11, 12, 18, 30, 19)),
//        Message(2, "1", "Donec lectus lorem, imperdiet nec laoreet eu?", Date(2023, 11, 12, 18, 31, 15)),
//        Message(2, "2", "Curabitur venenatis, nisl!", Date(2023, 11, 12, 18, 31, 41))
//    )
//
//    override fun getChatMessages(chatId: Int): List<Message>
//    {
//        return messages.filter { it.chatId == chatId }
//    }
//
//    override fun sendMessage(chatId: Int, message: Message): Message?
//    {
//        messages.add(message)
//        return message
//    }
//
//}