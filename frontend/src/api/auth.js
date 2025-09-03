import { http } from "./http";

export async function login(email, password) {
  const { data } = await http.post("/auth/login", { email, password });
  // expects { accessToken, user }
  return data;
}

export async function register(email, name, password) {
  const { data } = await http.post("/auth/register", { email, name, password });
  // returns user object (or 201)
  return data;
}
