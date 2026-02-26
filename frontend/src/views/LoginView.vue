<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const email = ref('');
const password = ref('');
const mode = ref<'login' | 'signup'>('login');
const auth = useAuthStore();
const router = useRouter();

const submit = () => {
  if (mode.value === 'login') {
    auth.login(email.value, password.value);
  } else {
    auth.signup(email.value, password.value);
  }
  if (auth.isAuthenticated) {
    setTimeout(() => auth.clearMessage(), 3000);
    router.push('/raw-materials');
  }
};
</script>

<template>
  <div class="max-w-md mx-auto bg-white rounded-lg shadow p-6">
    <h1 class="text-2xl font-semibold mb-4">Entrar</h1>
    <div class="space-y-4">
      <div>
        <label class="block text-sm font-medium text-gray-700">E-mail</label>
        <input v-model="email" type="email" class="mt-1 w-full border rounded px-3 py-2" placeholder="voce@empresa.com" />
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700">Senha</label>
        <input v-model="password" type="password" class="mt-1 w-full border rounded px-3 py-2" placeholder="••••••••" />
      </div>
      <div class="flex items-center justify-between">
        <div class="space-x-2">
          <button @click="mode='login'; submit()" class="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-500">Login</button>
          <button @click="mode='signup'; submit()" class="bg-gray-200 text-gray-800 px-4 py-2 rounded hover:bg-gray-300">Cadastro</button>
        </div>
      </div>
      <p v-if="auth.message" class="text-green-700 bg-green-100 border border-green-300 rounded px-3 py-2">
        {{ auth.message }}
      </p>
      <p class="text-xs text-gray-500">Autenticação de demonstração: qualquer e-mail/senha funciona.</p>
    </div>
  </div>
  </template>
