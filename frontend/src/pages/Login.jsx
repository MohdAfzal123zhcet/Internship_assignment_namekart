import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { Link, useNavigate, useLocation } from "react-router-dom";

export default function Login() {
  const nav = useNavigate();
  const { state } = useLocation();
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [err, setErr] = useState("");

  async function submit(e) {
    e.preventDefault();
    setErr("");
    try {
      await login(email, password);
      nav(state?.from?.pathname || "/");
    } catch (ex) {
      setErr(ex?.response?.data?.message || "Login failed");
    }
  }

  return (
    <form onSubmit={submit} className="card">
      <h2>Login</h2>
      {err && <p className="error">{err}</p>}
      <input placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
      <input
        placeholder="Password"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button>Login</button>
      <p>
        New here? <Link to="/register">Create account</Link>
      </p>
    </form>
  );
}
