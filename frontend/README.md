# Vercel frontend

This directory contains the static browser UI. In Vercel, use `frontend` as the project root directory and the **Other** framework preset; no build command is required.

After the Spring Boot API is running, enter its public base URL in the **Backend address** field in the page. The UI remembers it in that browser. Configure `FRONTEND_ORIGINS` on the backend host with the exact Vercel origin, for example `https://your-project.vercel.app`. Local development defaults to `http://localhost:8080` and the backend allows `http://localhost:3000`.
