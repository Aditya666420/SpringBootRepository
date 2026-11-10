import { useEffect, useState } from 'react'
import { api } from '../API/api.js'

export default function ScoreboardPage({ quiz, onBack }) {
  const [rows, setRows] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    api.scoreboard(quiz.id).then(setRows).catch((e) => setError(e.message))
  }, [quiz.id])

  return (
    <div className="card shadow-sm">
      <div className="card-header d-flex justify-content-between align-items-center">
        <span>Scoreboard: {quiz.title}</span>
        <button className="btn btn-sm btn-outline-secondary" onClick={onBack}>
          Back
        </button>
      </div>

      {error && <div className="alert alert-danger m-3">{error}</div>}
      {!rows && !error && <div className="p-3">Loading...</div>}

      {rows && (
        <table className="table mb-0">
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Score</th>
              <th>%</th>
              <th>When</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((r, i) => (
              <tr key={r.id}>
                <td>{i + 1}</td>
                <td>{r.participantName}</td>
                <td>
                  {r.score}/{r.total}
                </td>
                <td>{r.percentage}%</td>
                <td>{new Date(r.createdAt).toLocaleString()}</td>
              </tr>
            ))}
            {rows.length === 0 && (
              <tr>
                <td colSpan="5" className="text-center text-muted">
                  No attempts yet
                </td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  )
}