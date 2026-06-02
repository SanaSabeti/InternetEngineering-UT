import { useCallback, useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { getArticle, getArticles } from "../api/articlesApi.js";
import LoadingState from "../components/LoadingState.jsx";
import { createArticleMap, normalizeReferences } from "../utils/articles.js";

export default function ArticleDetailsPage() {
  const { id } = useParams();
  const [article, setArticle] = useState(null);
  const [allArticles, setAllArticles] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);

  const loadArticle = useCallback(async () => {
    try {
      setIsLoading(true);
      const [articleData, articlesData] = await Promise.all([
        getArticle(id),
        getArticles()
      ]);

      if (!articleData) {
        setNotFound(true);
        return;
      }

      setArticle(articleData);
      setAllArticles(articlesData);
    } catch (error) {
      toast.error(error.message || "Could not load article.");
      setNotFound(true);
    } finally {
      setIsLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadArticle();
  }, [loadArticle]);

  const articleMap = useMemo(() => createArticleMap(allArticles), [allArticles]);
  const referenceIds = normalizeReferences(article?.references);

  if (isLoading) {
    return <LoadingState message="Loading article..." />;
  }

  if (notFound) {
    return (
      <section className="empty-state">
        <h1>Article not found</h1>
        <p>The backend did not return an article for this id.</p>
        <Link className="button" to="/">
          Back to articles
        </Link>
      </section>
    );
  }

  return (
    <section className="article-details page-stack">
      <Link className="back-link" to="/">
        ← Back to articles
      </Link>

      <article className="details-panel">
        <p className="eyebrow">Article #{article.id}</p>
        <h1>{article.title}</h1>
        <p className="summary-text">{article.summary}</p>
        <div className="article-body">{article.body}</div>
      </article>

      <aside className="references-panel">
        <h2>Referenced articles</h2>

        {referenceIds.length === 0 ? (
          <p>This article does not reference any other article.</p>
        ) : (
          <ul className="reference-list">
            {referenceIds.map((referenceId) => {
              const referencedArticle = articleMap.get(referenceId);

              return (
                <li key={referenceId}>
                  {referencedArticle ? (
                    <Link to={`/articles/${referenceId}`}>
                      {referencedArticle.title}
                    </Link>
                  ) : (
                    <span>Article #{referenceId} was not found in the API result.</span>
                  )}
                </li>
              );
            })}
          </ul>
        )}
      </aside>
    </section>
  );
}
