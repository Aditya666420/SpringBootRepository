import { useEffect, useState } from 'react'
import { Link, Route, Routes, useNavigate, useParams } from 'react-router-dom'
import { api } from './API/api.js'
import UploadPage from './pages/UploadPage.jsx'
import QuizPage from './pages/QuizPage.jsx'
import ResultPage from './pages/ResultPage.jsx'
import ScoreboardPage from './pages/ScoreboardPage.jsx'

// Small hook: load something by id and track loading/error
function useLoad(loader, deps) {
  const [data, setData] = useState(null)
  const [error, setError] = useState('')
  useEffect(() => {
    setData(null)
    setError('')
    loader().then(setData).catch((e) => setError(e.message))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps)
  return { data, error }
}

function Status({ error }) {
  return error ? (
    <div className="alert alert-danger">{error}</div>
  ) : (
    <div className="text-center p-5">
      <span className="spinner-border" />
    </div>
  )
}

function UploadRoute() {
  const nav = useNavigate()
  return <UploadPage onStart={(q) => nav(`/quiz/${q.id}`)} />
}

function QuizRoute() {
  const { id } = useParams()
  const nav = useNavigate()
  const { data: quiz, error } = useLoad(() => api.getQuiz(id), [id])
  if (!quiz) return <Status error={error} />
  return <QuizPage quiz={quiz} onDone={(r) => nav(`/result/${r.id}`)} />
}

function ResultRoute() {
  const { attemptId } = useParams()
  const nav = useNavigate()
  const { data: result, error } = useLoad(() => api.getAttempt(attemptId), [attemptId])
  if (!result) return <Status error={error} />
  return (
    <ResultPage
      result={result}
      onScoreboard={() => nav(`/quiz/${result.quizId}/scoreboard`)}
      onRetake={() => nav(`/quiz/${result.quizId}`)}
      onHome={() => nav('/')}
    />
  )
}

function ScoreboardRoute() {
  const { id } = useParams()
  const nav = useNavigate()
  const { data: quiz, error } = useLoad(() => api.getQuiz(id), [id])
  if (!quiz) return <Status error={error} />
  return <ScoreboardPage quiz={quiz} onBack={() => nav(-1)} />
}

export default function App() {
  return (
    <>
      <nav className="navbar navbar-dark bg-primary mb-4">
        <div className="container">
          <Link to="/" className="navbar-brand mb-0 h1">
            PDF Quiz
          </Link>
        </div>
      </nav>

      <div className="container pb-5" style={{ maxWidth: 800 }}>
        <Routes>
          <Route path="/" element={<UploadRoute />} />
          <Route path="/quiz/:id" element={<QuizRoute />} />
          <Route path="/quiz/:id/scoreboard" element={<ScoreboardRoute />} />
          <Route path="/result/:attemptId" element={<ResultRoute />} />
          <Route path="*" element={<div className="alert alert-warning">Page not found</div>} />
        </Routes>
      </div>
    </>
  )
}