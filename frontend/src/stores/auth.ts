import { defineStore } from 'pinia';

type User = { email: string };

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: (JSON.parse(localStorage.getItem('auth_user') || 'null') as User | null),
    message: '' as string,
  }),
  getters: {
    isAuthenticated: (state) => !!state.user,
  },
  actions: {
    login(email: string, _password: string) {
      // Demo only: accept any credentials
      this.user = { email };
      localStorage.setItem('auth_user', JSON.stringify(this.user));
      this.message = '';
    },
    signup(email: string, _password: string) {
      // Demo: auto-authenticate after signup
      this.user = { email };
      localStorage.setItem('auth_user', JSON.stringify(this.user));
      this.message = 'Cadastro realizado com sucesso';
    },
    logout() {
      this.user = null;
      localStorage.removeItem('auth_user');
      this.message = '';
    },
    clearMessage() {
      this.message = '';
    }
  }
});
