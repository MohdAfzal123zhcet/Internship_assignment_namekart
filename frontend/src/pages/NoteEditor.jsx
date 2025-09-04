// src/pages/NoteEditor.jsx
import { useEffect, useState } from "react";
import { getNote, updateNote, createShare } from "../api/notes";
import { useParams } from "react-router-dom";
import Navbar from "../components/Navbar";

export default function NoteEditor() {
  const { id } = useParams();
  const [note, setNote] = useState(null);
  const [err, setErr] = useState("");
  const [success, setSuccess] = useState("");   // ✅ added
  const [shareUrl, setShareUrl] = useState("");

  useEffect(() => { (async () => setNote(await getNote(id)))(); }, [id]);

  async function save() {
    if (!note) return;
    try {
      const updated = await updateNote(note.id, {
        title: note.title, content: note.content, tags: note.tags, version: note.version
      });
      setNote(updated);
      setErr("");
      setSuccess("✅ Note saved successfully");   // ✅ show success
      setTimeout(() => setSuccess(""), 2000);     // auto-clear after 2s
    } catch (ex) {
      if (ex?.response?.status === 409) {
        const sv = ex.response.data.serverVersion;
        setErr(`Version conflict. Server version is ${sv}. Please reload to continue.`);
      } else setErr(ex?.response?.data?.message || "Save failed");
      setSuccess("");   // clear success if error
    }
  }

  async function share() {
    const { url } = await createShare(id);
    setShareUrl(url);
  }

  if (!note) return <div className="container"><Navbar /><div className="panel"><p>Loading…</p></div></div>;

  return (
    <div className="container">
      <Navbar />
      <div className="panel">
        <div className="toolbar">
          <h2>Edit Note</h2>
          <div style={{display:"flex", gap:10}}>
            <button className="btn" onClick={save}>Save</button>
            <button className="btn" onClick={share}>Create Share Link</button>
          </div>
        </div>

        {err && <p className="error">{err}</p>}
        {success && <p className="success">{success}</p>}   {/* ✅ success message */}

        <input
          className="titleInput"
          value={note.title}
          placeholder="Title"
          onChange={(e) => setNote({ ...note, title: e.target.value })}
        />
        <textarea
          className="editor"
          value={note.content}
          placeholder="Write here…"
          onChange={(e) => setNote({ ...note, content: e.target.value })}
        />

        {shareUrl && (
          <p className="meta">
            Public URL: <a href={shareUrl} target="_blank" rel="noreferrer">{shareUrl}</a>
          </p>
        )}
        <p className="meta">Version: {note.version}</p>
      </div>
    </div>
  );
}
