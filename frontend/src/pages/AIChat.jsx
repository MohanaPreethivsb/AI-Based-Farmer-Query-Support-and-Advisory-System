import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { askChatQuestion } from '../services/chatService'

const starterQuestions = [
  'Why are my tomato leaves turning yellow?',
  'Which fertilizer is suitable for paddy?',
  'How often should I irrigate my cotton crop?',
]

function AIChat() {
  const [question, setQuestion] = useState('')
  const [messages, setMessages] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const bottomRef = useRef(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const submitQuestion = async (event) => {
    event.preventDefault()

    const trimmedQuestion = question.trim()

    if (trimmedQuestion.length < 5) {
      setError('Please enter a farming question with at least 5 characters.')
      return
    }

    setMessages((current) => [
      ...current,
      { role: 'user', text: trimmedQuestion, createdAt: new Date().toISOString() },
    ])
    setQuestion('')
    setError('')
    setLoading(true)

    try {
      const chat = await askChatQuestion(trimmedQuestion)
      setMessages((current) => [
        ...current,
        { role: 'assistant', text: chat.response, createdAt: chat.createdAt },
      ])
    } catch (apiError) {
      setError(apiError.response?.data?.message || 'Unable to get advice right now.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="grid gap-6">
      <div className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-[#17211b]">AI Chat</h1>
          <p className="mt-2 text-[#526056]">Ask crop, pest, fertilizer, irrigation, or soil questions.</p>
        </div>
        <Link className="rounded border border-[#9db096] px-4 py-2 font-semibold text-[#1d5f35]" to="/chat-history">
          View history
        </Link>
      </div>

      <div className="grid min-h-[420px] gap-4 rounded border border-[#d8e1d2] bg-white p-4 shadow-sm">
        <div className="max-h-[52vh] min-h-80 overflow-y-auto pr-1">
          {messages.length === 0 ? (
            <div className="grid h-full place-items-center text-center text-[#526056]">
              <div>
                <p className="font-semibold text-[#1d5f35]">Start with a farming question.</p>
                <div className="mt-4 flex flex-wrap justify-center gap-2">
                  {starterQuestions.map((starter) => (
                    <button
                      className="rounded border border-[#d8e1d2] px-3 py-2 text-sm hover:bg-[#edf4e8]"
                      key={starter}
                      onClick={() => setQuestion(starter)}
                      type="button"
                    >
                      {starter}
                    </button>
                  ))}
                </div>
              </div>
            </div>
          ) : (
            <div className="grid gap-3">
              {messages.map((message, index) => (
                <article
                  className={`max-w-3xl rounded p-3 ${
                    message.role === 'user'
                      ? 'ml-auto bg-[#1d5f35] text-white'
                      : 'mr-auto border border-[#d8e1d2] bg-[#f6f8f2] text-[#17211b]'
                  }`}
                  key={`${message.createdAt}-${index}`}
                >
                  <p className="whitespace-pre-wrap text-sm leading-6">{message.text}</p>
                  <time className={`mt-2 block text-xs ${message.role === 'user' ? 'text-green-100' : 'text-[#69756c]'}`}>
                    {new Date(message.createdAt).toLocaleString()}
                  </time>
                </article>
              ))}
              {loading && (
                <p className="mr-auto rounded border border-[#d8e1d2] bg-[#f6f8f2] px-3 py-2 text-sm text-[#526056]">
                  Preparing farming advice...
                </p>
              )}
              <div ref={bottomRef} />
            </div>
          )}
        </div>

        <form className="grid gap-3 border-t border-[#d8e1d2] pt-4" onSubmit={submitQuestion}>
          {error && <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}
          <textarea
            className="min-h-28 resize-y rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
            onChange={(event) => setQuestion(event.target.value)}
            placeholder="Type your farming question..."
            value={question}
          />
          <button className="w-fit rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading} type="submit">
            {loading ? 'Asking...' : 'Ask AI'}
          </button>
        </form>
      </div>
    </section>
  )
}

export default AIChat
