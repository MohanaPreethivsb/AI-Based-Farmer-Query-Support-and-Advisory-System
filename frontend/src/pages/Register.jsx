import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import AuthForm from '../components/AuthForm'
import { useAuth } from '../hooks/useAuth'
import { getApiErrorMessage } from '../utils/apiError'

function Register() {
  const [values, setValues] = useState({
    name: '',
    email: '',
    phone: '',
    district: '',
    password: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleChange = (event) => {
    setValues((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await register(values)
      navigate('/dashboard')
    } catch (apiError) {
      setError(getApiErrorMessage(apiError, 'Unable to create account right now'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="mx-auto max-w-md rounded border border-[#d8e1d2] bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-[#17211b]">Create account</h1>
      <p className="mt-2 text-sm text-[#526056]">Start with a secure farmer profile.</p>
      <div className="mt-6">
        <AuthForm mode="register" values={values} error={error} loading={loading} onChange={handleChange} onSubmit={handleSubmit} />
      </div>
      <p className="mt-4 text-sm text-[#526056]">
        Already registered? <Link className="font-semibold text-[#1d5f35]" to="/login">Login</Link>
      </p>
    </section>
  )
}

export default Register
