import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  ArrowRight,
  Bot,
  CalendarDays,
  CloudSun,
  Droplets,
  History,
  ImagePlus,
  Leaf,
  MapPin,
  MessageSquareText,
  Sprout,
  Wheat,
} from 'lucide-react'
import { useAuth } from '../hooks/useAuth'
import api from '../services/api'
import { getChatHistory } from '../services/chatService'


function ActionCard({ to, icon: Icon, title, description, accent }) {
  return (
    <Link
      className="group rounded-lg border border-emerald-100 bg-white p-5 shadow-sm shadow-emerald-950/5 transition duration-200 hover:-translate-y-1 hover:border-emerald-200 hover:shadow-xl hover:shadow-emerald-950/10 focus:outline-none focus:ring-2 focus:ring-emerald-600 focus:ring-offset-2"
      to={to}
    >
      <div className="flex items-start justify-between gap-4">
        <span className={`grid size-12 shrink-0 place-items-center rounded-lg ${accent}`}>
          <Icon aria-hidden="true" size={23} />
        </span>
        <ArrowRight aria-hidden="true" className="mt-1 text-slate-300 transition group-hover:translate-x-1 group-hover:text-emerald-700" size={20} />
      </div>
      <h2 className="mt-5 text-lg font-bold text-slate-950">{title}</h2>
      <p className="mt-2 text-sm leading-6 text-slate-600">{description}</p>
    </Link>
  )
}

function InfoCard({ title, icon: Icon, children, className = '' }) {
  return (
    <section className={`rounded-lg border border-emerald-100 bg-white p-5 shadow-sm shadow-emerald-950/5 ${className}`}>
      <div className="mb-4 flex items-center justify-between gap-3">
        <h2 className="text-base font-bold text-slate-950">{title}</h2>
        <span className="grid size-9 place-items-center rounded-lg bg-emerald-50 text-emerald-700">
          <Icon aria-hidden="true" size={19} />
        </span>
      </div>
      {children}
    </section>
  )
}

function Dashboard() {
  const { user } = useAuth()
  const [recentChats, setRecentChats] = useState([])
  const [loadingChats, setLoadingChats] = useState(true)
  const [weather, setWeather] = useState(null)
  const [dailyTip, setDailyTip] = useState("Loading...");

  useEffect(() => {
    const loadRecentChats = async () => {
      try {
        const chats = await getChatHistory()
        setRecentChats(chats.slice(0, 3))
      } catch {
        setRecentChats([])
      } finally {
        setLoadingChats(false)
      }
    }

    loadRecentChats()
  }, [])
  useEffect(() => {
  navigator.geolocation.getCurrentPosition(
    async (position) => {
      try {
        const { latitude, longitude } = position.coords;

        const response = await api.get('/weather', {
          params: { lat: latitude, lon: longitude },
        });

        setWeather(response.data);
      } catch (error) {
        console.error(error);
      }
    },
    (error) => {
      console.error("Location Error:", error);
    }
  );
}, []);
useEffect(() => {
  const loadTip = async () => {
    try {
      const res = await api.get('/tip/daily-tip');

      setDailyTip(res.data.tip);

    } catch (err) {
      setDailyTip("Unable to load today's farming tip.");
    }
  };

  loadTip();
}, []);

  const firstName = user?.name?.split(' ')[0] || 'Farmer'

  return (
    <section className="grid gap-6">
      <div className="grid gap-5 rounded-lg border border-emerald-100 bg-white p-5 shadow-lg shadow-emerald-950/5 md:grid-cols-[1.35fr_0.65fr] md:p-7">
        <div className="grid content-center gap-5">
          <div>
            <p className="inline-flex items-center gap-2 rounded-full bg-lime-50 px-3 py-1 text-sm font-bold text-emerald-800">
              <Sprout aria-hidden="true" size={16} />
              AI-powered farmer advisory
            </p>
            <h1 className="mt-4 text-3xl font-bold leading-tight text-slate-950 sm:text-4xl">
              Welcome back, {firstName}
            </h1>
            <p className="mt-3 max-w-2xl text-base leading-7 text-slate-600">
              Track advice, upload crop images, review market signals, and continue your farming conversations from one clean workspace.
            </p>
          </div>

          <div className="flex flex-wrap gap-3">
            <Link
              className="inline-flex items-center gap-2 rounded-lg bg-emerald-700 px-4 py-3 font-semibold text-white shadow-md shadow-emerald-900/20 transition hover:bg-emerald-800 focus:outline-none focus:ring-2 focus:ring-emerald-600 focus:ring-offset-2"
              to="/chat"
            >
              <Bot aria-hidden="true" size={18} />
              Ask AI advisor
            </Link>
            <Link
              className="inline-flex items-center gap-2 rounded-lg border border-emerald-200 px-4 py-3 font-semibold text-emerald-800 transition hover:bg-emerald-50 focus:outline-none focus:ring-2 focus:ring-emerald-600 focus:ring-offset-2"
              to="/upload"
            >
              <ImagePlus aria-hidden="true" size={18} />
              Upload crop image
            </Link>
          </div>
        </div>

        <div className="relative min-h-56 overflow-hidden rounded-lg bg-gradient-to-br from-emerald-700 via-emerald-600 to-lime-500 p-5 text-white">
          <div className="absolute left-6 top-6 size-16 rounded-full bg-amber-200 shadow-lg shadow-amber-900/20" />
          <div className="absolute bottom-0 left-0 right-0 h-24 bg-lime-600" />
          <div className="absolute bottom-10 left-0 right-0 h-16 bg-emerald-900/20" />
          <div className="absolute bottom-0 left-0 right-0 grid grid-cols-6 gap-2 px-5 pb-5">
            {Array.from({ length: 6 }).map((_, index) => (
              <span className="h-20 rounded-t-full bg-white/25" key={index} />
            ))}
          </div>
          <div className="relative ml-auto w-fit rounded-lg bg-white/15 p-4 backdrop-blur">
            <Wheat aria-hidden="true" size={42} />
            <p className="mt-2 text-sm font-semibold">Smart crop planning</p>
          </div>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <ActionCard
          accent="bg-emerald-50 text-emerald-700"
          description="Ask crop, pest, fertilizer, irrigation, and soil questions with your saved account."
          icon={Bot}
          title="AI Chat"
          to="/chat"
        />
        <ActionCard
          accent="bg-lime-50 text-lime-700"
          description="Search and delete previous advisory conversations using the existing history page."
          icon={History}
          title="Chat History"
          to="/chat-history"
        />
        <ActionCard
          accent="bg-amber-50 text-amber-700"
          description="Upload crop photos and save them for future AI disease analysis workflows."
          icon={ImagePlus}
          title="Upload Crop Image"
          to="/upload"
        />
      </div>

      <div className="grid gap-6 xl:grid-cols-[1.35fr_0.65fr]">
        <InfoCard title="Recent Conversations" icon={MessageSquareText}>
          {loadingChats ? (
            <p className="text-sm text-slate-500">Loading conversations...</p>
          ) : recentChats.length === 0 ? (
            <div className="rounded-lg border border-dashed border-emerald-200 bg-emerald-50/50 p-4 text-sm text-slate-600">
              No conversations yet. Start with AI Chat to build your advisory history.
            </div>
          ) : (
            <div className="grid gap-3">
              {recentChats.map((chat) => (
                <Link
                  className="rounded-lg border border-slate-100 p-4 transition hover:border-emerald-200 hover:bg-emerald-50/50"
                  key={chat._id}
                  to="/chat-history"
                >
                  <div className="flex items-center gap-2 text-xs font-semibold text-slate-500">
                    <CalendarDays aria-hidden="true" size={14} />
                    <time>{new Date(chat.createdAt).toLocaleString()}</time>
                  </div>
                  <h3 className="mt-2 line-clamp-1 font-semibold text-slate-950">{chat.question}</h3>
                  <p className="mt-2 line-clamp-2 text-sm leading-6 text-slate-600">{chat.response}</p>
                </Link>
              ))}
            </div>
          )}
        </InfoCard>

        <div className="grid gap-6">
          <InfoCard title="Weather" icon={CloudSun}>
            <div className="flex items-end justify-between gap-4">
              <div>
                <p className="text-4xl font-bold text-slate-950">
                    {weather ? `${weather.temperature}°C` : "Loading..."}</p>
                <p className="mt-1 flex items-center gap-2 text-sm text-slate-500">
                  <MapPin aria-hidden="true" size={15} />
                  {weather ? weather.city : "Getting location..."}
                </p>
              </div>
              <div className="rounded-lg bg-sky-50 p-3 text-sky-700">
                <Droplets aria-hidden="true" size={30} />
              </div>
            </div>
            <p className="mt-4 rounded-lg bg-emerald-50 p-3 text-sm leading-6 text-emerald-900">
             {weather
                 ? `${weather.condition} • Humidity: ${weather.humidity}%`
                 : "Fetching live weather..."} </p>
          </InfoCard>

          <InfoCard title="Daily Farming Tip" icon={Leaf}>
            <p className="text-sm leading-6 text-slate-600">
            {dailyTip}
            </p>
          </InfoCard>
        </div>
      </div>
    </section>
  )
}

export default Dashboard
