import { useEffect, useState } from 'react'
import { deleteChat, getChatHistory } from '../services/chatService'

function ChatHistory() {
  const [chats, setChats] = useState([])
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const timeoutId = setTimeout(async () => {
      setLoading(true)
      setError('')

      try {
        const results = await getChatHistory(search)
        setChats(results)
      } catch (apiError) {
        setError(apiError.response?.data?.message || 'Unable to load chat history.')
      } finally {
        setLoading(false)
      }
    }, 250)

    return () => clearTimeout(timeoutId)
  }, [search])

  const handleDelete = async (chatId) => {
    setError('')

    try {
      await deleteChat(chatId)
      setChats((current) => current.filter((chat) => chat._id !== chatId))
    } catch (apiError) {
      setError(apiError.response?.data?.message || 'Unable to delete this chat.')
    }
  }

  return (
    <section className="grid gap-6">
      <div>
        <h1 className="text-3xl font-bold text-[#17211b]">Chat History</h1>
        <p className="mt-2 text-[#526056]">Review and search previous farming conversations.</p>
      </div>

      <input
        className="w-full rounded border border-[#c9d6c4] bg-white px-3 py-2 outline-none focus:border-[#1d5f35]"
        onChange={(event) => setSearch(event.target.value)}
        placeholder="Search conversations"
        value={search}
      />

      {error && <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}
      {loading && <p className="text-sm text-[#526056]">Loading conversations...</p>}

      {!loading && chats.length === 0 ? (
        <p className="rounded border border-[#d8e1d2] bg-white p-5 text-[#526056]">No conversations found.</p>
      ) : (
        <div className="grid gap-4">
          {chats.map((chat) => (
            <article className="rounded border border-[#d8e1d2] bg-white p-5 shadow-sm" key={chat._id}>
              <div className="flex flex-wrap items-start justify-between gap-3">
                <time className="text-xs font-medium text-[#69756c]">{new Date(chat.createdAt).toLocaleString()}</time>
                <button className="rounded border border-red-200 px-3 py-1 text-sm font-semibold text-red-700" onClick={() => handleDelete(chat._id)} type="button">
                  Delete
                </button>
              </div>
              <h2 className="mt-3 font-semibold text-[#1d5f35]">{chat.question}</h2>
              <p className="mt-3 whitespace-pre-wrap text-sm leading-6 text-[#526056]">{chat.response}</p>
            </article>
          ))}
        </div>
      )}
    </section>
  )
}

export default ChatHistory
