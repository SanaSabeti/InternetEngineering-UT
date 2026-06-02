import { useCallback, useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import { getArticles } from "../api/articlesApi.js";
import ArticleCard from "../components/ArticleCard.jsx";
import EmptyState from "../components/EmptyState.jsx";
import LoadingState from "../components/LoadingState.jsx";
import SearchBar from "../components/SearchBar.jsx";
import { sortSearchResults } from "../utils/articles.js";

export default function HomePage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const searchTerm = searchParams.get("s") || "";

  const [articles, setArticles] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const loadArticles = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await getArticles(searchTerm);
      setArticles(data);
    } catch (error) {
      toast.error(error.message || "Could not load articles.");
    } finally {
      setIsLoading(false);
    }
  }, [searchTerm]);

  useEffect(() => {
    loadArticles();
  }, [loadArticles]);

  const visibleArticles = useMemo(
    () => sortSearchResults(articles, searchTerm),
    [articles, searchTerm]
  );

  function handleSearch(nextSearchTerm) {
    if (nextSearchTerm) {
      setSearchParams({ s: nextSearchTerm });
    } else {
      setSearchParams({});
    }
  }

  function handleClear() {
    setSearchParams({});
  }

  return (
    <section className="page-stack">
      <div className="hero-panel">
        <div>
          <h1>Explore shared articles</h1>
        </div>
      </div>

      <SearchBar
        initialValue={searchTerm}
        onSearch={handleSearch}
        onClear={handleClear}
      />

      {isLoading ? (
        <LoadingState message="Loading articles..." />
      ) : visibleArticles.length === 0 ? (
        <EmptyState
          title={searchTerm ? "No matching articles" : "No articles yet"}
          message={
            searchTerm
              ? "Try a different search term or clear the search box."
              : "Create an article to see it listed here."
          }
          showCreateLink={!searchTerm}
        />
      ) : (
        <div className="article-grid">
          {visibleArticles.map((article) => (
            <ArticleCard key={article.id} article={article} />
          ))}
        </div>
      )}
    </section>
  );
}
