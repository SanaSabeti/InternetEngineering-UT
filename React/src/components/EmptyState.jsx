import { Link } from "react-router-dom";

export default function EmptyState({ title, message, showCreateLink = true }) {
  return (
    <div className="empty-state">
      <h2>{title}</h2>
      <p>{message}</p>
      {showCreateLink && (
        <Link className="button" to="/articles/new">
          Create first article
        </Link>
      )}
    </div>
  );
}
