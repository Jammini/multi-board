import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    email: null as string | null,
  }),
  actions: {
    login(email: string) {
      this.email = email;
    },
    logout() {
      this.email = null;
    },
  },
});
