import { Bot, Clock3, Home, ImagePlus, LayoutDashboard, LogOut, Sprout, UserRound } from 'lucide-react'
import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/chat', label: 'AI Chat', icon: Bot },
  { to: '/chat-history', label: 'History', icon: Clock3 },
  { to: '/upload', label: 'Upload', icon: ImagePlus },
  { to: '/profile', label: 'Profile', icon: UserRound },
]

function AppLayout() {
  const { isAuthenticated, logout, user } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  return (
    <div className="min-h-screen bg-[#f7faf5] text-[#17211b]">
      <header className="sticky top-0 z-30 border-b border-emerald-100 bg-white/95 shadow-sm shadow-emerald-950/5 backdrop-blur">
        <nav className="mx-auto flex max-w-7xl items-center justify-between gap-4 px-4 py-3 sm:px-6 lg:px-8">
          <Link to="/" className="flex items-center gap-3 text-lg font-bold text-emerald-800">
            <span className="grid size-10 place-items-center rounded-lg bg-emerald-700 text-white shadow-md shadow-emerald-900/20">
              <Sprout aria-hidden="true" size={22} />
            </span>
            <span>Farmer Advisory</span>
          </Link>

          <div className="flex items-center gap-2 text-sm font-semibold">
            {isAuthenticated ? (
              <>
                <div className="hidden items-center gap-1 lg:flex">
                  {navItems.map(({ to, label, icon: Icon }) => (
                    <NavLink
                      className={({ isActive }) =>
                        `flex items-center gap-2 rounded-lg px-3 py-2 transition ${
                          isActive ? 'bg-emerald-50 text-emerald-800' : 'text-slate-600 hover:bg-emerald-50 hover:text-emerald-800'
                        }`
                      }
                      key={to}
                      to={to}
                    >
                      <Icon aria-hidden="true" size={17} />
                      {label}
                    </NavLink>
                  ))}
                </div>
                <span className="hidden rounded-full bg-lime-50 px-3 py-2 text-emerald-900 sm:inline">{user?.name}</span>
                <button
                  className="inline-flex items-center gap-2 rounded-lg bg-emerald-700 px-3 py-2 text-white shadow-sm shadow-emerald-900/20 transition hover:bg-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-600 focus:ring-offset-2"
                  type="button"
                  onClick={handleLogout}
                >
                  <LogOut aria-hidden="true" size={16} />
                  <span className="hidden sm:inline">Logout</span>
                </button>
              </>
            ) : (
              <>
                <NavLink className="rounded-lg px-3 py-2 text-slate-600 transition hover:bg-emerald-50 hover:text-emerald-800" to="/login">
                  Login
                </NavLink>
                <NavLink className="rounded-lg bg-emerald-700 px-3 py-2 text-white shadow-sm shadow-emerald-900/20 transition hover:bg-emerald-800" to="/register">
                  Register
                </NavLink>
              </>
            )}
          </div>
        </nav>
      </header>

      {isAuthenticated && (
        <nav className="border-b border-emerald-100 bg-white px-4 py-2 lg:hidden" aria-label="Primary">
          <div className="flex gap-2 overflow-x-auto pb-1">
            {navItems.map(({ to, label, icon: Icon }) => (
              <NavLink
                className={({ isActive }) =>
                  `inline-flex shrink-0 items-center gap-2 rounded-lg px-3 py-2 text-sm font-semibold transition ${
                    isActive ? 'bg-emerald-700 text-white' : 'text-slate-600 hover:bg-emerald-50 hover:text-emerald-800'
                  }`
                }
                key={to}
                to={to}
              >
                <Icon aria-hidden="true" size={16} />
                {label}
              </NavLink>
            ))}
          </div>
        </nav>
      )}

      <div className="mx-auto flex w-full max-w-7xl gap-6 px-4 py-6 sm:px-6 lg:px-8">
        {isAuthenticated && (
          <aside className="sticky top-24 hidden h-[calc(100vh-7rem)] w-64 shrink-0 rounded-lg border border-emerald-100 bg-white p-4 shadow-lg shadow-emerald-950/5 lg:block">
            <div className="rounded-lg bg-emerald-50 p-4">
              <p className="text-xs font-bold uppercase tracking-wider text-emerald-700">Signed in</p>
              <p className="mt-1 truncate font-semibold text-slate-900">{user?.name}</p>
              <p className="truncate text-sm text-slate-500">{user?.district || 'Farm advisory workspace'}</p>
            </div>

            <nav className="mt-5 grid gap-1" aria-label="Sidebar">
              {navItems.map(({ to, label, icon: Icon }) => (
                <NavLink
                  className={({ isActive }) =>
                    `flex items-center gap-3 rounded-lg px-3 py-3 text-sm font-semibold transition ${
                      isActive
                        ? 'bg-emerald-700 text-white shadow-md shadow-emerald-900/20'
                        : 'text-slate-600 hover:bg-emerald-50 hover:text-emerald-800'
                    }`
                  }
                  key={to}
                  to={to}
                >
                  <Icon aria-hidden="true" size={18} />
                  {label}
                </NavLink>
              ))}
            </nav>

            <Link
              className="mt-5 flex items-center gap-3 rounded-lg border border-emerald-100 px-3 py-3 text-sm font-semibold text-slate-600 transition hover:border-emerald-200 hover:bg-emerald-50 hover:text-emerald-800"
              to="/"
            >
              <Home aria-hidden="true" size={18} />
              Home
            </Link>
          </aside>
        )}

        <main className="min-w-0 flex-1">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

export default AppLayout
