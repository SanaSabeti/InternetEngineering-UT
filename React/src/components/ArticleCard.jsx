import { Link } from "react-router-dom";

export default function ArticleCard({ article }) {
  const referenceCount = Array.isArray(article.references) ? article.references.length : 0;

  return (
    <article className="article-card">
      <div className="article-card-header">
        <h2>{article.title}</h2>
        <span className="pill">{referenceCount} references</span>
      </div>

      <p>{article.summary}</p>

      <div className="article-card-footer">
        <Link className="button button-secondary" to={`/articles/${article.id}`}>
          Read article
        </Link>
      </div>
    </article>
  );
}
