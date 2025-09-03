// src/pages/ShareView.jsx
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { viewShare } from "../api/notes";

export default function ShareView() {
  const { token } = useParams();
  const [n, setN] = useState(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    (async () => {
      try { setN(await viewShare(token)); }
      catch (e) { setErr(e?.response?.data?.message || "Unable to load shared note"); }
    })();
  }, [token]);

  return (
    <div className="container">
      <div className="panel">
        {err ? <p className="error">{err}</p> :
        !n ? <p>Loading…</p> :
        <>
          <h1 style={{marginTop: 0}}>{n.title}</h1>
          <pre className="viewer">{n.content}</pre>
        </>}
      </div>
    </div>
  );
}
