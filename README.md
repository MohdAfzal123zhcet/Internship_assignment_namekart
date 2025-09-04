# 📝 Notes App

A full-stack **Notes Management Application** with authentication, tagging, and shareable links.  
Built using **Spring Boot (backend)**, **React + Vite (frontend)**, and **PostgreSQL (database)**.  
The app supports versioning, conflict handling, and public share links that work even in **Incognito mode**.

---<img width="1920" height="1080" alt="Screenshot (166)" src="https://github.com/user-attachments/assets/73d022fa-6941-46a1-8c55-ebd19601768b" />

<img width="1920" height="1080" alt="Screenshot (165)" src="https://github.com/user-attachments/assets/1ff038aa-1dbd-4860-9775-ff41f62d9ca2" />

<img width="1920" height="1080" alt="Screenshot (164)" src="https://github.com/user-attachments/assets/bae72c8d-da50-4517-b447-3ceeb9fe22d2" />


## 🚀 Live Demo

- **Frontend (Vercel):** [Frontend Link](https://your-frontend-url.vercel.app)  


---

## ✨ Features

- 🔐 **User Authentication** (Register/Login/Logout with JWT)  
- 📒 **CRUD Notes** (Create, Read, Update, Delete)  
- 🏷 **Tag Support** (Add/Remove tags per note)  
- 🔄 **Versioning** (Optimistic locking to avoid conflicts)  
- 🔗 **Share Notes** via public link (works in Incognito)  
- 📱 **Responsive UI** (Vercel-hosted React frontend)  
- 🗄 **Persistent Storage** with PostgreSQL (Render-hosted) & mysql for local

---

## 🛠 Tech Stack

**Frontend**
- React + Vite
- React Router
- Context API for Auth
- Deployed on Vercel

**Backend**
- Spring Boot (v3.5.x)
- Spring Security with JWT
- JPA + Hibernate
- Deployed on Render

**Database**
- PostgreSQL (Render Cloud DB)
  Mysql for local run
---

---

## ⚡ API Endpoints (Backend)

| Method | Endpoint               | Description                  | Auth Required |
|--------|------------------------|------------------------------|---------------|
| POST   | `/api/v1/auth/register`| Register new user            | ❌ |
| POST   | `/api/v1/auth/login`   | Login user (JWT issued)      | ❌ |
| GET    | `/api/v1/notes`        | List notes for logged-in user| ✅ |
| POST   | `/api/v1/notes`        | Create a new note            | ✅ |
| GET    | `/api/v1/notes/{id}`   | Get single note              | ✅ |
| PUT    | `/api/v1/notes/{id}`   | Update note (with versioning)| ✅ |
| DELETE | `/api/v1/notes/{id}`   | Delete note                  | ✅ |
| POST   | `/api/v1/share/{id}`   | Create share link            | ✅ |
| GET    | `/api/v1/share/{id}`   | Access shared note           | ❌ |

---

## 🖥 Local Setup

### 1. Clone Repository
```bash
git clone https://github.com/your-username/notes-app.git
cd notes-app

