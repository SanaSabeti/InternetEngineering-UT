export function normalizeReferences(references) {
  if (!Array.isArray(references)) {
    return [];
  }

  return [...new Set(references.map(Number).filter(Number.isInteger))];
}

export function sortSearchResults(articles, searchTerm) {
  const keyword = searchTerm.trim().toLowerCase();

  if (!keyword) {
    return articles;
  }

  const titleMatches = [];
  const summaryMatches = [];

  for (const article of articles) {
    const title = article.title?.toLowerCase() || "";
    const summary = article.summary?.toLowerCase() || "";

    if (title.includes(keyword)) {
      titleMatches.push(article);
    } else if (summary.includes(keyword)) {
      summaryMatches.push(article);
    }
  }

  return [...titleMatches, ...summaryMatches];
}

export function getReferenceArticles(article, allArticles) {
  const references = normalizeReferences(article?.references);
  return references
    .map((id) => allArticles.find((candidate) => candidate.id === id))
    .filter(Boolean);
}

export function createArticleMap(articles) {
  return new Map(articles.map((article) => [article.id, article]));
}
