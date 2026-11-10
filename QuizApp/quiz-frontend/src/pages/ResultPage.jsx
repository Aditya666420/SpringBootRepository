
import { api } from '../API/api'
export default function ResultPage({ result, onScoreboard, onRetake, onHome }) {
  const barColor = (pct, weak) => (weak ? 'bg-danger' : pct >= 90 ? 'bg-success' : 'bg-primary')

  return (
    <>
      <div className="card shadow-sm mb-4 text-center">
        <div className="card-body">
          <div className="text-muted">{result.participantName}</div>
          <h1 className="display-4">
            {result.score} / {result.total}
          </h1>
          <div className="fs-5 text-muted">{result.percentage}%</div>
        </div>
      </div>

      <div className="card shadow-sm mb-4">
        <div className="card-header">Score by section</div>
        <div className="card-body">
          {result.sections.map((s) => (
            <div key={s.sectionIndex} className="mb-3">
              <div className="d-flex justify-content-between">
                <span>
                  {s.title}
                  {s.weak && <span className="badge bg-danger ms-2">Needs work</span>}
                </span>
                <span>
                  {s.correct}/{s.total}
                </span>
              </div>
              <div className="progress" style={{ height: 10 }}>
                <div
                  className={`progress-bar ${barColor(s.percentage, s.weak)}`}
                  style={{ width: `${s.percentage}%` }}
                />
              </div>
            </div>
          ))}
        </div>
      </div>

      {result.weakSections.length > 0 ? (
        <div className="alert alert-warning">
          <h6 className="alert-heading">Sections to improve</h6>
          <ul className="mb-2">
            {result.weakSections.map((w) => (
              <li key={w}>{w}</li>
            ))}
          </ul>
          <hr />
          <div style={{ whiteSpace: 'pre-line' }}>{result.studyTips}</div>
        </div>
      ) : (
        <div className="alert alert-success">{result.studyTips}</div>
      )}

      <h5 className="mt-4">Answer review</h5>
      {result.review.map((r) => (
        <div
          key={r.questionIndex}
          className={`card mb-2 border-${r.correct ? 'success' : 'danger'}`}
        >
          <div className="card-body">
            <div className="fw-semibold mb-2">
              {r.questionIndex + 1}. {r.question}
            </div>
            <div className={r.correct ? 'text-success' : 'text-danger'}>
              Your answer: {r.yourAnswer}
            </div>
            {!r.correct && (
              <div className="text-success">Correct answer: {r.correctAnswer}</div>
            )}
            {r.explanation && (
              <div className="small text-muted mt-1">{r.explanation}</div>
            )}
          </div>
        </div>
      ))}

      <div className="d-flex gap-2 mt-4">
        <button className="btn btn-primary" onClick={onScoreboard}>
          Scoreboard
        </button>
        <button className="btn btn-outline-primary" onClick={onRetake}>
          Retake quiz
        </button>
         <a className="btn btn-success" href={api.pdfUrl(result.quizId, true)} download>
          Download Q&amp;A (PDF)
        </a>
        <a className="btn btn-outline-success" href={api.pdfUrl(result.quizId, false)} download>
          Questions only (PDF)
        </a>
        <button className="btn btn-outline-secondary" onClick={onHome}>
          New PDF
        </button>
      </div>
    </>
  )
}