const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

async function handle(res) {
  if (!res.ok) {
    let msg = res.statusText
    try {
      const j = await res.json()
      msg = j.message || msg
    } catch { /* body was not JSON */ }
    throw new Error(msg)
  }
  return res.json()
}

export const api = {
  listQuizzes: () => fetch(`${BASE}/quizzes`).then(handle),
  getAttempt: (id) => fetch(`${BASE}/attempts/${id}`).then(handle),
  getQuiz: (id) => fetch(`${BASE}/quizzes/${id}`).then(handle),
  pdfUrl: (quizId, answers = true) => `${BASE}/quizzes/${quizId}/pdf?answers=${answers}`,
  createQuiz: (file, title) => {
    const form = new FormData()
    form.append('file', file)
    if (title) form.append('title', title)
    return fetch(`${BASE}/quizzes`, { method: 'POST', body: form }).then(handle)
  },

  submit: (quizId, participantName, answers) =>
    fetch(`${BASE}/quizzes/${quizId}/attempts`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ participantName, answers }),
    }).then(handle),

  scoreboard: (quizId) => fetch(`${BASE}/quizzes/${quizId}/attempts`).then(handle),
}