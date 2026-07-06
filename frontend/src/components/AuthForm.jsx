function AuthForm({ mode, values, error, loading, onChange, onSubmit }) {
  const isRegister = mode === 'register'

  return (
    <form className="grid gap-4" onSubmit={onSubmit}>
      {isRegister && (
        <label className="grid gap-1 text-sm font-medium text-[#334137]">
          Name
          <input
            className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
            name="name"
            onChange={onChange}
            required
            value={values.name}
          />
        </label>
      )}

      <label className="grid gap-1 text-sm font-medium text-[#334137]">
        Email
        <input
          className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
          name="email"
          onChange={onChange}
          required
          type="email"
          value={values.email}
        />
      </label>

      {isRegister && (
        <>
          <label className="grid gap-1 text-sm font-medium text-[#334137]">
            Phone
            <input
              className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
              name="phone"
              onChange={onChange}
              value={values.phone}
            />
          </label>
          <label className="grid gap-1 text-sm font-medium text-[#334137]">
            District
            <input
              className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
              name="district"
              onChange={onChange}
              value={values.district}
            />
          </label>
        </>
      )}

      <label className="grid gap-1 text-sm font-medium text-[#334137]">
        Password
        <input
          className="rounded border border-[#c9d6c4] px-3 py-2 outline-none focus:border-[#1d5f35]"
          name="password"
          onChange={onChange}
          minLength={isRegister ? 6 : undefined}
          required
          type="password"
          value={values.password}
        />
      </label>

      {error && <p className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}

      <button className="rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading} type="submit">
        {loading ? 'Please wait...' : isRegister ? 'Create account' : 'Login'}
      </button>
    </form>
  )
}

export default AuthForm
