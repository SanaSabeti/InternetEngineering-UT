import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout.jsx";
import HomePage from "./pages/HomePage.jsx";
import ArticleDetailsPage from "./pages/ArticleDetailsPage.jsx";
import NewArticlePage from "./pages/NewArticlePage.jsx";
import NotFoundPage from "./pages/NotFoundPage.jsx";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/articles/new" element={<NewArticlePage />} />
        <Route path="/articles/:id" element={<ArticleDetailsPage />} />
        <Route path="/article/:id" element={<Navigate to="/articles/:id" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
