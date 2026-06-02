# Article Hub React Client - Phase 3

This is the React frontend for the existing phase-2 Spring Boot REST API.

The backend code is not changed. The frontend uses your current endpoints:

- `GET /article`
- `GET /article?s=keyword`
- `GET /article/{id}`
- `POST /article`

## How phase 2 and phase 3 are connected

During development, the React app runs on Vite at:

```text
http://localhost:5173
```

Your Spring Boot API runs at:

```text
http://localhost:8080
```

The React code calls `/article`. The Vite proxy in `vite.config.js` forwards those requests to:

```text
http://localhost:8080/article
```

Because of this, you do not need to change your Spring Boot backend or add CORS configuration.

## Run instructions

First, run the phase-2 backend:

```bash
cd code
mvn spring-boot:run
```

Then run this React frontend in another terminal:

```bash
cd article-react-client
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

## Pages

- `/` - article list and search
- `/articles/new` - add article form
- `/articles/:id` - article details page

## Libraries used

- React
- React Router DOM
- React Toastify
- Vite

## Notes about the unchanged backend

The backend returns `null` when a duplicate title is submitted. This frontend handles that by showing an error toast.

The backend also returns `null` when an article id does not exist. This frontend handles that by showing an Article Not Found page.
