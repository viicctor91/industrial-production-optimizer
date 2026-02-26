<script setup lang="ts">
import { useI18n } from 'vue-i18n';

const { t, locale } = useI18n();

const toggleLanguage = () => {
  locale.value = locale.value === 'pt' ? 'en' : 'pt';
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-900">
    <nav class="bg-indigo-600 text-white shadow-md">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center">
            <div class="flex-shrink-0 font-bold text-xl flex items-center gap-2">
              <img src="/src/assets/logo.svg" alt="Optimizer" class="h-6" />
              <span>Optimizer</span>
            </div>
            <div class="hidden md:block">
              <div class="ml-10 flex items-baseline space-x-4">
                <router-link to="/raw-materials" active-class="bg-indigo-700" class="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">{{ t('nav.rawMaterials') }}</router-link>
                <router-link to="/products" active-class="bg-indigo-700" class="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">{{ t('nav.products') }}</router-link>
                <router-link to="/production" active-class="bg-indigo-700" class="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">{{ t('nav.production') }}</router-link>
                <router-link to="/login" active-class="bg-indigo-700" class="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">Login</router-link>
              </div>
            </div>
          </div>
          <div>
            <button @click="toggleLanguage" class="bg-indigo-500 hover:bg-indigo-400 px-3 py-1 rounded text-sm">
              {{ locale.toUpperCase() }}
            </button>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <router-view></router-view>
    </main>
  </div>
</template>
