import { useState } from 'react'
import { useAuth } from '../hooks/useAuth'

function Profile() {
  const { user, updateProfile } = useAuth()
  const [values, setValues] = useState({
    name: user?.name || '',
    phone: user?.phone || '',
    district: user?.district || '',
    password: '',
  })
  const [status, setStatus] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (event) => {
    setValues((current) => ({ ...current, [event.target.name]: event.target.value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setStatus('')
    setError('')
    setLoading(true)

    const payload = { ...values }

    if (!payload.password) {
      delete payload.password
    }

    try {
      await updateProfile(payload)
      setValues((current) => ({ ...current, password: '' }))
      setStatus('Profile updated successfully')
    } catch (apiError) {
      setError(apiError.response?.data?.message || 'Unable to update profile right now')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="mx-auto max-w-xl rounded border border-[#d8e1d2] bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-[#17211b]">Profile</h1>
      <p className="mt-2 text-sm text-[#526056]">{user?.email}</p>

      <form className="mt-6 grid gap-4" onSubmit={handleSubmit}>
        {['name', 'phone', 'district'].map((field) => (
          <label className="grid gap-1 text-sm font-medium capitalize text-[#334137]" key={field}>
            {field}
            <input
              className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
              name={field}
              onChange={handleChange}
              required={field === 'name'}
              value={values[field]}
            />
          </label>
        ))}

        <label className="grid gap-1 text-sm font-medium text-[#334137]">
          New password
          <input
            className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
            name="password"
            onChange={handleChange}
            placeholder="Leave blank to keep current password"
            type="password"
            value={values.password}
          />
        </label>

        {status && <p className="rounded border border-green-200 bg-green-50 px-3 py-2 text-sm text-green-700">{status}</p>}
        {error && <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}

        <button className="rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading} type="submit">
          {loading ? 'Saving...' : 'Save profile'}
        </button>
      </form>
    </section>
  )
}

export default Profile
