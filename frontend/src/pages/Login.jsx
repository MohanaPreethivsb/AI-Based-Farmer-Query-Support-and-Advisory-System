import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import AuthForm from '../components/AuthForm'
import { useAuth } from '../hooks/useAuth'
import { getApiErrorMessage } from '../utils/apiError'

function Login() {
  const [values, setValues] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleChange = (event) => {
    setValues((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await login(values)
      navigate(location.state?.from?.pathname || '/dashboard')
    } catch (apiError) {
      setError(getApiErrorMessage(apiError, 'Unable to login right now'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="mx-auto max-w-md rounded border border-[#d8e1d2] bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-[#17211b]">Login</h1>
      <p className="mt-2 text-sm text-[#526056]">Access your farmer advisory dashboard.</p>
      <div className="mt-6">
        <AuthForm mode="login" values={values} error={error} loading={loading} onChange={handleChange} onSubmit={handleSubmit} />
      </div>
      <p className="mt-4 text-sm">
        <Link className="font-semibold text-[#1d5f35]" to="/forgot-password">Forgot password?</Link>
      </p>
      <p className="mt-4 text-sm text-[#526056]">
        New here? <Link className="font-semibold text-[#1d5f35]" to="/register">Create an account</Link>
      </p>
    </section>
  )
}

export default Login
