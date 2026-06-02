import { useState } from "react";

export default function SearchBar({ initialValue = "", onSearch, onClear }) {
  const [value, setValue] = useState(initialValue);

  function handleSubmit(event) {
    event.preventDefault();
    onSearch(value.trim());
  }

  function handleClear() {
    setValue("");
    onClear();
  }

  return (
    <form className="search-panel" onSubmit={handleSubmit}>
      <label htmlFor="article-search">Search articles</label>
      <div className="search-row">
        <input
          id="article-search"
          type="search"
          value={value}
          onChange={(event) => setValue(event.target.value)}
          placeholder="Search by title or summary..."
        />
        <button className="button" type="submit">
          Search
        </button>
        <button className="button button-ghost" type="button" onClick={handleClear}>
          Clear
        </button>
      </div>
    </form>
  );
}
