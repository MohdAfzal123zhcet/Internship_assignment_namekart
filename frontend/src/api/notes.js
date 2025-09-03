import { http } from "./http";

export async function listNotes() {
  const { data } = await http.get("/notes");
  return data;
}

export async function getNote(id) {
  const { data } = await http.get(`/notes/${id}`);
  return data;
}

export async function createNote(payload) {
  // { title, content, tags? }
  const { data } = await http.post("/notes", payload);
  return data;
}

export async function updateNote(id, payload) {
  // { title?, content?, tags?, version }
  const { data } = await http.put(`/notes/${id}`, payload);
  return data;
}

export async function deleteNote(id) {
  await http.delete(`/notes/${id}`);
}

export async function createShare(id) {
  const { data } = await http.post(`/notes/${id}/share`, {});
  // { token, url, expiresAt }
  return data;
}

export async function viewShare(token) {
  const { data } = await http.get(`/share/${token}`);
  return data; // read-only note
}
