import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <section className="empty-state">
      <h1>Page not found</h1>
      <p>The route you opened does not exist in the React app.</p>
      <Link className="button" to="/">
        Back to articles
      </Link>
    </section>
  );
}
