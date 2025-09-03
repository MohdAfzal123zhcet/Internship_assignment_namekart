// src/pages/NotesList.jsx
import { useEffect, useState } from "react";
import { createNote, deleteNote, listNotes } from "../api/notes";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";

export default function NotesList() {
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");
  const nav = useNavigate();

  async function load() {
    setLoading(true);
    setErr("");
    try {
      setNotes(await listNotes());
    } catch (e) {
      setErr(e?.response?.data?.message || "Failed to load notes");
    } finally {
      setLoading(false);
    }
  }
  useEffect(() => { load(); }, []);

  async function newNote() {
    try {
      setErr("");
      const n = await createNote({ title: "Untitled", content: "", tags: [] });
      if (n?.id) nav(`/edit/${n.id}`); else load();
    } catch (e) {
      setErr(e?.response?.data?.message || "Failed to create note");
    }
  }

  async function onDelete(id) {
    try { await deleteNote(id); load(); } 
    catch (e) { setErr(e?.response?.data?.message || "Delete failed"); }
  }

  return (
    <div className="container">
      <Navbar />
      <div className="panel">
        <div className="toolbar">
          <h2>Your Notes</h2>
          <button className="btn" onClick={newNote}>+ New Note</button>
        </div>

        {err && <p className="error">{err}</p>}

        {loading ? (
          <p>Loading…</p>
        ) : notes.length === 0 ? (
          <div className="empty">
            <h3>No notes yet</h3>
            <p>Create your first note to get started.</p>
            <button className="btn" onClick={newNote}>+ New Note</button>
          </div>
        ) : (
          <ul className="list">
            {notes.map((n) => (
              <li key={n.id} className="row">
                <div>
                  <Link to={`/edit/${n.id}`} className="title">{n.title || "(untitled)"}</Link>
                  <div className="meta">{new Date(n.updatedAt).toLocaleString()}</div>
                </div>
                <div className="actions">
                  <button className="btn btn-danger" onClick={() => onDelete(n.id)}>Delete</button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
