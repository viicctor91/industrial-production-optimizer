<script setup lang="ts">
import { onMounted } from 'vue';
import { useProductStore } from '@/stores/product';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const { t } = useI18n();
const store = useProductStore();
const router = useRouter();

onMounted(() => {
  store.fetchAll();
});

const remove = async (id: string) => {
  if (confirm(t('common.confirmDelete'))) {
    await store.remove(id);
  }
};
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">{{ t('product.title') }}</h1>
      <router-link to="/products/new" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
        {{ t('common.create') }}
      </router-link>
    </div>

    <div v-if="store.loading" class="text-center py-4">{{ t('common.loading') }}</div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="product in store.products" :key="product.id" class="bg-white p-6 rounded-lg shadow border border-gray-200">
        <div class="flex justify-between items-start mb-2">
          <h3 class="text-lg font-semibold text-gray-800">{{ product.name }}</h3>
          <span class="text-green-600 font-bold">${{ product.price }}</span>
        </div>
        <p class="text-sm text-gray-500 mb-4">{{ product.code }}</p>
        
        <div class="mb-4">
          <h4 class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">{{ t('product.composition') }}</h4>
          <ul class="text-sm text-gray-600 space-y-1">
            <li v-for="comp in product.composition" :key="comp.id" class="flex justify-between">
              <span>{{ comp.rawMaterialName }}</span>
              <span>{{ comp.quantityRequiredPerUnit }} {{ comp.rawMaterialUnit }}</span>
            </li>
            <li v-if="product.composition.length === 0" class="italic text-gray-400">None</li>
          </ul>
        </div>

        <div class="flex justify-end space-x-2 mt-auto">
          <router-link :to="`/products/${product.id}/edit`" class="text-indigo-600 hover:text-indigo-900 text-sm font-medium">
            {{ t('common.edit') }}
          </router-link>
          <button @click="remove(product.id!)" class="text-red-600 hover:text-red-900 text-sm font-medium">
            {{ t('common.delete') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
