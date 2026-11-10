import { useEffect, useState } from 'react'
import { api } from '../API/api.js'

export default function UploadPage({ onStart }) {
  const [file, setFile] = useState(null)
  const [title, setTitle] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [quizzes, setQuizzes] = useState([])

  useEffect(() => {
    api.listQuizzes().then(setQuizzes).catch(() => {})
  }, [])

  const generate = async () => {
    if (!file) return
    setLoading(true)
    setError('')
    try {
      onStart(await api.createQuiz(file, title))
    } catch (e) {
      setError(e.message)
      setLoading(false)
    }
  }

  const openExisting = async (id) => {
    try {
      onStart(await api.getQuiz(id))
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <>
      <div className="card shadow-sm mb-4">
        <div className="card-body">
          <h4 className="card-title">Generate a quiz from a PDF</h4>
          <p className="text-muted">
            The AI reads your PDF and writes 10 multiple-choice questions.
          </p>

          <div className="mb-3">
            <label className="form-label">PDF file</label>
            <input
              type="file"
              accept="application/pdf"
              className="form-control"
              disabled={loading}
              onChange={(e) => setFile(e.target.files[0] || null)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Title (optional)</label>
            <input
              className="form-control"
              value={title}
              disabled={loading}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          {error && <div className="alert alert-danger">{error}</div>}

          <button
            className="btn btn-primary"
            onClick={generate}
            disabled={!file || loading}
          >
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" />
                Generating... this can take a few minutes
              </>
            ) : (
              'Generate quiz'
            )}
          </button>
        </div>
      </div>

      {quizzes.length > 0 && (
        <div className="card shadow-sm">
          <div className="card-header">Previous quizzes</div>
          <ul className="list-group list-group-flush">
            {quizzes.map((q) => (
              <li
                key={q.id}
                className="list-group-item d-flex justify-content-between align-items-center"
              >
                <div>
                  <strong>{q.title}</strong>
                  <div className="small text-muted">
                    {q.questionCount} questions
                  </div>
                </div>
                <button
                  className="btn btn-outline-primary btn-sm"
                  onClick={() => openExisting(q.id)}
                >
                  Take quiz
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </>
  )
}