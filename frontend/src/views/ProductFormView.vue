<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useProductStore } from '@/stores/product';
import { useRawMaterialStore } from '@/stores/rawMaterial';
import { useI18n } from 'vue-i18n';
import type { ProductCompositionItem } from '@/types';

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const productStore = useProductStore();
const rawMaterialStore = useRawMaterialStore();

const isEditing = computed(() => route.params.id !== undefined);
const loading = ref(false);

const form = ref({
  code: '',
  name: '',
  price: 0,
  composition: [] as ProductCompositionItem[]
});

onMounted(async () => {
  loading.value = true;
  await rawMaterialStore.fetchAll();
  
  if (isEditing.value) {
    const id = route.params.id as string;
    await productStore.fetchById(id);
    if (productStore.currentProduct) {
      form.value = {
        code: productStore.currentProduct.code,
        name: productStore.currentProduct.name,
        price: productStore.currentProduct.price,
        composition: [...productStore.currentProduct.composition]
      };
    }
  }
  loading.value = false;
});

const addMaterial = () => {
  form.value.composition.push({
    rawMaterialId: '',
    quantityRequiredPerUnit: 1
  });
};

const removeMaterial = (index: number) => {
  form.value.composition.splice(index, 1);
};

const save = async () => {
  try {
    loading.value = true;
    const data = {
      code: form.value.code,
      name: form.value.name,
      price: Number(form.value.price),
      composition: form.value.composition.map(c => ({
        rawMaterialId: c.rawMaterialId,
        quantityRequiredPerUnit: Number(c.quantityRequiredPerUnit)
      }))
    };

    if (isEditing.value) {
      await productStore.update(route.params.id as string, data);
    } else {
      await productStore.create(data);
    }
    router.push('/products');
  } catch (e: any) {
    alert('Error saving: ' + (e.response?.data?.message || e.message));
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="max-w-2xl mx-auto bg-white p-8 rounded-lg shadow">
    <h1 class="text-2xl font-bold mb-6">{{ isEditing ? t('product.edit') : t('product.new') }}</h1>

    <form @submit.prevent="save">
      <div class="grid grid-cols-1 gap-6 mb-6">
        <div>
          <label class="block text-sm font-medium text-gray-700">{{ t('common.code') }}</label>
          <input v-model="form.code" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">{{ t('common.name') }}</label>
          <input v-model="form.name" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">{{ t('common.price') }}</label>
          <input type="number" step="0.01" v-model="form.price" required min="0.01" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
        </div>
      </div>

      <div class="mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-medium text-gray-900">{{ t('product.composition') }}</h2>
          <button type="button" @click="addMaterial" class="text-sm text-indigo-600 hover:text-indigo-900">
            + {{ t('common.add') }}
          </button>
        </div>

        <div v-if="form.composition.length === 0" class="text-sm text-gray-500 italic mb-4">
          No raw materials added.
        </div>

        <div v-for="(item, index) in form.composition" :key="index" class="flex gap-4 mb-4 items-end bg-gray-50 p-3 rounded">
          <div class="flex-1">
            <label class="block text-xs font-medium text-gray-700 mb-1">Material</label>
            <select v-model="item.rawMaterialId" required class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2">
              <option value="" disabled>{{ t('product.selectMaterial') }}</option>
              <option v-for="rm in rawMaterialStore.rawMaterials" :key="rm.id" :value="rm.id">
                {{ rm.name }} ({{ rm.unit }})
              </option>
            </select>
          </div>
          <div class="w-32">
            <label class="block text-xs font-medium text-gray-700 mb-1">{{ t('common.quantity') }}</label>
            <input type="number" step="0.001" v-model="item.quantityRequiredPerUnit" required min="0.001" class="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
          </div>
          <button type="button" @click="removeMaterial(index)" class="text-red-600 hover:text-red-800 pb-2">
            {{ t('common.remove') }}
          </button>
        </div>
      </div>

      <div class="flex justify-end space-x-3">
        <router-link to="/products" class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
          {{ t('common.cancel') }}
        </router-link>
        <button type="submit" :disabled="loading" class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:opacity-50">
          {{ loading ? t('common.loading') : t('common.save') }}
        </button>
      </div>
    </form>
  </div>
</template>
