import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="btn-link">Notes</Link>
      </div>

      <div className="navbar-right">
        {user ? (
          <>
            <span style={{ fontWeight: "600" }}>{user.name}</span>
            <button onClick={logout} className="btn-link">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="btn-link">Login</Link>
            <Link to="/register" className="btn-link">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}
