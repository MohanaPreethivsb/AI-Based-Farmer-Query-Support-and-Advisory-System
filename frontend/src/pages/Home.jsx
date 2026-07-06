import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

function Home() {
  const { isAuthenticated } = useAuth()

  return (
    <section className="grid gap-8 lg:grid-cols-[1.1fr_0.9fr] lg:items-center">
      <div className="grid gap-5">
        <p className="text-sm font-semibold uppercase tracking-wider text-[#6a7a2c]">AI farming support</p>
        <h1 className="max-w-3xl text-4xl font-bold leading-tight text-[#17211b] sm:text-5xl">
          Practical crop advice for farmers, starting with secure accounts.
        </h1>
        <p className="max-w-2xl text-lg text-[#526056]">
          Register, login, and manage your profile now. Chatbot, image upload, weather, and history modules will be added feature by feature.
        </p>
        <div className="flex flex-wrap gap-3">
          <Link className="rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white" to={isAuthenticated ? '/dashboard' : '/register'}>
            {isAuthenticated ? 'Open dashboard' : 'Get started'}
          </Link>
          <Link className="rounded border border-[#9db096] px-4 py-2 font-semibold text-[#1d5f35]" to="/login">
            Login
          </Link>
        </div>
      </div>

      <div className="rounded border border-[#d8e1d2] bg-white p-5 shadow-sm">
        <h2 className="text-xl font-semibold text-[#1d5f35]">Current module</h2>
        <ul className="mt-4 grid gap-3 text-[#526056]">
          <li>JWT-based registration and login</li>
          <li>Protected dashboard and profile pages</li>
          <li>MongoDB user storage with hashed passwords</li>
        </ul>
      </div>
    </section>
  )
}

export default Home
