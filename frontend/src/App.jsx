import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import RequireAuth from "./auth/RequireAuth";
import Login from "./pages/Login";
import Register from "./pages/Register";
import NotesList from "./pages/NotesList";
import NoteEditor from "./pages/NoteEditor";
import ShareView from "./pages/ShareView";
import "./index.css";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/"
            element={
              <RequireAuth>
                <NotesList />
              </RequireAuth>
            }
          />
          <Route
            path="/edit/:id"
            element={
              <RequireAuth>
                <NoteEditor />
              </RequireAuth>
            }
          />
          <Route path="/s/:token" element={<ShareView />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
