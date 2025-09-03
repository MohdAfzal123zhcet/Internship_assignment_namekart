import { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const nav = useNavigate();
  const { register } = useAuth();
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [err, setErr] = useState("");

  async function submit(e) {
    e.preventDefault();
    setErr("");
    try {
      await register(email, name, password);
      nav("/");
    } catch (ex) {
      setErr(ex?.response?.data?.message || "Register failed");
    }
  }

  return (
    <form onSubmit={submit} className="card">
      <h2>Register</h2>
      {err && <p className="error">{err}</p>}
      <input placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
      <input placeholder="Name" value={name} onChange={(e) => setName(e.target.value)} />
      <input
        placeholder="Password"
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <button>Create account</button>
    </form>
  );
}
