import { useEffect, useMemo, useState } from 'react'
import api from '../services/api'
import { AuthContext } from './AuthContextObject'

const TOKEN_KEY = 'farmer_advisory_token'
const USER_KEY = 'farmer_advisory_user'

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY))
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem(USER_KEY)
    return storedUser ? JSON.parse(storedUser) : null
  })
  const [loading, setLoading] = useState(Boolean(token))

  const persistSession = (authPayload) => {
    localStorage.setItem(TOKEN_KEY, authPayload.token)
    localStorage.setItem(USER_KEY, JSON.stringify(authPayload.user))
    setToken(authPayload.token)
    setUser(authPayload.user)
  }

  const clearSession = () => {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
    setToken(null)
    setUser(null)
  }

  useEffect(() => {
    const loadProfile = async () => {
      if (!token) {
        setLoading(false)
        return
      }

      try {
        const { data } = await api.get('/auth/profile')
        localStorage.setItem(USER_KEY, JSON.stringify(data.user))
        setUser(data.user)
      } catch {
        clearSession()
      } finally {
        setLoading(false)
      }
    }

    loadProfile()
  }, [token])

  const register = async (formData) => {
    const { data } = await api.post('/auth/register', formData)
    persistSession(data)
    return data.user
  }

  const login = async (credentials) => {
    const { data } = await api.post('/auth/login', credentials)
    persistSession(data)
    return data.user
  }

  const updateProfile = async (formData) => {
    const { data } = await api.put('/auth/profile', formData)
    persistSession(data)
    return data.user
  }

  const value = useMemo(
    () => ({
      user,
      token,
      loading,
      isAuthenticated: Boolean(token && user),
      register,
      login,
      logout: clearSession,
      updateProfile,
    }),
    [user, token, loading],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
