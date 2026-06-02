const API_BASE = import.meta.env.VITE_API_BASE_URL || "";
const ARTICLE_PATH = `${API_BASE}/article`;

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = typeof data === "string" ? data : "Request failed";
    throw new Error(message);
  }

  return data;
}

export async function getArticles(searchTerm = "") {
  const query = searchTerm.trim();
  const path = query
    ? `${ARTICLE_PATH}?s=${encodeURIComponent(query)}`
    : ARTICLE_PATH;

  const articles = await request(path);
  return Array.isArray(articles) ? articles : [];
}

export async function getArticle(id) {
  return request(`${ARTICLE_PATH}/${id}`);
}

export async function createArticle(article) {
  const created = await request(ARTICLE_PATH, {
    method: "POST",
    body: JSON.stringify(article),
  });

  if (!created) {
    throw new Error("An article with this title already exists.");
  }

  return created;
}
