import { NavLink, Outlet } from "react-router-dom";

export default function Layout() {
  return (
    <div className="app-shell">
      <header className="topbar">
        <NavLink to="/" className="brand">
          <span className="brand-mark">A</span>
          <span>Article Hub</span>
        </NavLink>

        <nav className="nav-links" aria-label="Main navigation">
          <NavLink to="/" end>
            Articles
          </NavLink>
          <NavLink to="/articles/new">New Article</NavLink>
        </nav>
      </header>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
}
