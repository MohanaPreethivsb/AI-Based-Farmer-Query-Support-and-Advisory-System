import api from './api'

export const askChatQuestion = async (question) => {
  const { data } = await api.post('/chat', { question })
  return data.chat
}

export const getChatHistory = async (search = '') => {
  const { data } = await api.get('/chat/history', {
    params: search ? { search } : {},
  })

  return data.chats
}

export const deleteChat = async (chatId) => {
  await api.delete(`/chat/${chatId}`)
}
