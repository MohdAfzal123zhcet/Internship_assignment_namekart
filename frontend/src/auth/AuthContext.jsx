import React, { createContext, useContext, useState } from "react";
import { login as apiLogin, register as apiRegister } from "../api/auth";

const Ctx = createContext({});

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const u = localStorage.getItem("user");
    return u ? JSON.parse(u) : undefined;
  });

  async function login(email, password) {
    const data = await apiLogin(email, password);
    localStorage.setItem("token", data.accessToken);
    localStorage.setItem("user", JSON.stringify(data.user));
    setUser(data.user);
  }

  async function register(email, name, password) {
    await apiRegister(email, name, password);
    await login(email, password);
  }

  function logout() {
    localStorage.clear();
    setUser(undefined);
  }

  return <Ctx.Provider value={{ user, login, register, logout }}>{children}</Ctx.Provider>;
}

export function useAuth() {
  return useContext(Ctx);
}
