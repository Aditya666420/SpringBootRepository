import { useState } from 'react'
import { api } from '../API/api.js'

export default function QuizPage({ quiz, onDone }) {
  const total = quiz.questions.length
  const [name, setName] = useState('')
  const [started, setStarted] = useState(false)
  const [current, setCurrent] = useState(0)
  const [answers, setAnswers] = useState(Array(total).fill(-1))
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')

  if (!started) {
    return (
      <div className="card shadow-sm">
        <div className="card-body">
          <h4>{quiz.title}</h4>
          <p className="text-muted">{total} questions</p>
          <label className="form-label">Your name</label>
          <input
            className="form-control mb-3"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter your name"
          />
          <button
            className="btn btn-primary"
            disabled={!name.trim()}
            onClick={() => setStarted(true)}
          >
            Start quiz
          </button>
        </div>
      </div>
    )
  }

  const q = quiz.questions[current]
  const answered = answers.filter((a) => a !== -1).length

  const pick = (i) =>
    setAnswers((prev) => prev.map((a, idx) => (idx === current ? i : a)))

  const submit = async () => {
    setSubmitting(true)
    setError('')
    try {
      onDone(await api.submit(quiz.id, name.trim(), answers))
    } catch (e) {
      setError(e.message)
      setSubmitting(false)
    }
  }

  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <div className="d-flex justify-content-between small text-muted mb-1">
          <span>
            Question {current + 1} of {total}
          </span>
          <span>{answered} answered</span>
        </div>
        <div className="progress mb-4" style={{ height: 6 }}>
          <div
            className="progress-bar"
            style={{ width: `${((current + 1) / total) * 100}%` }}
          />
        </div>

        <div className="small text-muted mb-1">
          {quiz.sections[q.sectionIndex]}
        </div>
        <h5 className="mb-3">{q.question}</h5>

        <div className="list-group mb-4">
          {q.options.map((opt, i) => (
            <button
              key={i}
              type="button"
              className={`list-group-item list-group-item-action ${
                answers[current] === i ? 'active' : ''
              }`}
              onClick={() => pick(i)}
            >
              <strong className="me-2">{'ABCD'[i]}.</strong>
              {opt}
            </button>
          ))}
        </div>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="d-flex justify-content-between">
          <button
            className="btn btn-outline-secondary"
            disabled={current === 0}
            onClick={() => setCurrent(current - 1)}
          >
            Previous
          </button>

          {current < total - 1 ? (
            <button
              className="btn btn-primary"
              onClick={() => setCurrent(current + 1)}
            >
              Next
            </button>
          ) : (
            <button
              className="btn btn-success"
              disabled={submitting}
              onClick={submit}
            >
              {submitting ? 'Submitting...' : 'Submit quiz'}
            </button>
          )}
        </div>

        {current === total - 1 && answered < total && (
          <div className="small text-warning mt-3">
            {total - answered} question(s) unanswered. They will count as wrong.
          </div>
        )}
      </div>
    </div>
  )
}