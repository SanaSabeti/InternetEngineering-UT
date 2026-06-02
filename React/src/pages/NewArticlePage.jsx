import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { createArticle, getArticles } from "../api/articlesApi.js";
import LoadingState from "../components/LoadingState.jsx";

const initialForm = {
  title: "",
  summary: "",
  body: "",
  references: []
};

export default function NewArticlePage() {
  const navigate = useNavigate();
  const [form, setForm] = useState(initialForm);
  const [articles, setArticles] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadArticles = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await getArticles();
      setArticles(data);
    } catch (error) {
      toast.error(error.message || "Could not load reference articles.");
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    loadArticles();
  }, [loadArticles]);

  const selectedReferenceIds = useMemo(
    () => new Set(form.references),
    [form.references]
  );

  function updateField(field, value) {
    setForm((current) => ({
      ...current,
      [field]: value
    }));
  }

  function toggleReference(articleId) {
    setForm((current) => {
      const exists = current.references.includes(articleId);
      const references = exists
        ? current.references.filter((id) => id !== articleId)
        : [...current.references, articleId];

      return {
        ...current,
        references
      };
    });
  }

  function validateForm() {
    if (!form.title.trim()) {
      return "Title is required.";
    }

    if (!form.summary.trim()) {
      return "Summary is required.";
    }

    if (!form.body.trim()) {
      return "Body is required.";
    }

    return null;
  }

  async function handleSubmit(event) {
    event.preventDefault();

    const validationError = validateForm();
    if (validationError) {
      toast.error(validationError);
      return;
    }

    try {
      setIsSubmitting(true);
      const created = await createArticle({
        title: form.title.trim(),
        summary: form.summary.trim(),
        body: form.body.trim(),
        references: form.references
      });

      toast.success("Article created successfully.");
      navigate(`/articles/${created.id}`);
    } catch (error) {
      toast.error(error.message || "Could not create article.");
    } finally {
      setIsSubmitting(false);
    }
  }

  if (isLoading) {
    return <LoadingState message="Preparing article form..." />;
  }

  return (
    <section className="page-stack">
      <div className="page-heading">
        <p className="eyebrow">Create</p>
        <h1>Add a new article</h1>
        <p>
          The form sends a <code>POST</code> request to your existing
          <code> /article</code> endpoint.
        </p>
      </div>

      <form className="article-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            type="text"
            value={form.title}
            onChange={(event) => updateField("title", event.target.value)}
            placeholder="Enter a unique article title"
          />
        </div>

        <div className="form-group">
          <label htmlFor="summary">Summary</label>
          <textarea
            id="summary"
            rows="3"
            value={form.summary}
            onChange={(event) => updateField("summary", event.target.value)}
            placeholder="Write a short summary"
          />
        </div>

        <div className="form-group">
          <label htmlFor="body">Body</label>
          <textarea
            id="body"
            rows="8"
            value={form.body}
            onChange={(event) => updateField("body", event.target.value)}
            placeholder="Write the full article body"
          />
        </div>

        <fieldset className="reference-picker">
          <legend>References</legend>
          <p>Select existing articles referenced by this article.</p>

          {articles.length === 0 ? (
            <div className="muted-box">No existing articles are available.</div>
          ) : (
            <div className="reference-options">
              {articles.map((article) => (
                <label key={article.id} className="reference-option">
                  <input
                    type="checkbox"
                    checked={selectedReferenceIds.has(article.id)}
                    onChange={() => toggleReference(article.id)}
                  />
                  <span>
                    <strong>{article.title}</strong>
                    <small>Article #{article.id}</small>
                  </span>
                </label>
              ))}
            </div>
          )}
        </fieldset>

        <div className="form-actions">
          <button className="button" type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating..." : "Create article"}
          </button>
          <button
            className="button button-ghost"
            type="button"
            onClick={() => setForm(initialForm)}
            disabled={isSubmitting}
          >
            Reset
          </button>
        </div>
      </form>
    </section>
  );
}
