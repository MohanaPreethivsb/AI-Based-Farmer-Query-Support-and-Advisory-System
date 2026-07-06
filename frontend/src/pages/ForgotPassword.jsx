import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import api from '../services/api'
import { getApiErrorMessage } from '../utils/apiError'

const initialValues = {
  email: '',
  otp: '',
  newPassword: '',
}

function ForgotPassword() {
  const [values, setValues] = useState(initialValues)
  const [step, setStep] = useState('email')
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleChange = (event) => {
    setValues((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  const requestOtp = async () => {
    const { data } = await api.post('/auth/forgot-password', { email: values.email })
    setMessage(data.message || 'OTP sent to your registered email.')
    setStep('otp')
  }

  const verifyOtp = async () => {
    const { data } = await api.post('/auth/verify-otp', {
      email: values.email,
      otp: values.otp,
    })
    setMessage(data.message || 'OTP verified. Create a new password.')
    setStep('password')
  }

  const resetPassword = async () => {
    const { data } = await api.post('/auth/reset-password', {
      email: values.email,
      newPassword: values.newPassword,
    })
    setMessage(data.message || 'Password reset successfully.')
    setValues(initialValues)
    setTimeout(() => navigate('/login'), 900)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setMessage('')
    setLoading(true)

    try {
      if (step === 'email') {
        await requestOtp()
      } else if (step === 'otp') {
        await verifyOtp()
      } else {
        await resetPassword()
      }
    } catch (apiError) {
      setError(getApiErrorMessage(apiError, 'Unable to reset password right now'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="mx-auto max-w-md rounded border border-[#d8e1d2] bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-[#17211b]">Forgot password</h1>
      <p className="mt-2 text-sm text-[#526056]">Reset access with the OTP sent to your registered email.</p>

      <form className="mt-6 grid gap-4" onSubmit={handleSubmit}>
        <label className="grid gap-1 text-sm font-medium text-[#334137]">
          Email
          <input
            className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35] disabled:bg-slate-100"
            disabled={step !== 'email'}
            name="email"
            onChange={handleChange}
            required
            type="email"
            value={values.email}
          />
        </label>

        {step !== 'email' && (
          <label className="grid gap-1 text-sm font-medium text-[#334137]">
            OTP
            <input
              className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35] disabled:bg-slate-100"
              disabled={step === 'password'}
              inputMode="numeric"
              maxLength={6}
              minLength={6}
              name="otp"
              onChange={handleChange}
              pattern="\d{6}"
              required
              value={values.otp}
            />
          </label>
        )}

        {step === 'password' && (
          <label className="grid gap-1 text-sm font-medium text-[#334137]">
            New password
            <input
              className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
              minLength={6}
              name="newPassword"
              onChange={handleChange}
              required
              type="password"
              value={values.newPassword}
            />
          </label>
        )}

        {message && <p className="rounded border border-green-200 bg-green-50 px-3 py-2 text-sm text-green-700">{message}</p>}
        {error && <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}

        <button className="rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading} type="submit">
          {loading ? 'Please wait...' : step === 'email' ? 'Send OTP' : step === 'otp' ? 'Verify OTP' : 'Reset password'}
        </button>
      </form>

      <p className="mt-4 text-sm text-[#526056]">
        Remembered it? <Link className="font-semibold text-[#1d5f35]" to="/login">Login</Link>
      </p>
    </section>
  )
}

export default ForgotPassword
