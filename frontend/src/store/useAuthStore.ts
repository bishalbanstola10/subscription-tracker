import { create } from "zustand";

interface AuthState {
  token: string | null;
  userId: string | null;
  email: string | null;
  isAuthenticated: boolean;
  setAuthData: (payload: {
    token: string;
    userId: string;
    email: string;
  }) => void;
  resetAuthData: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  token: null,
  userId: null,
  email: null,
  isAuthenticated: false,
  setAuthData: ({ token, userId, email }) =>
    set({ token, userId, email, isAuthenticated: true }),
  resetAuthData: () =>
    set({ token: null, userId: null, email: null, isAuthenticated: false }),
}));
