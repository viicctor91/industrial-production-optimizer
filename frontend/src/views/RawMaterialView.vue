<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRawMaterialStore } from '@/stores/rawMaterial';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const store = useRawMaterialStore();

const showModal = ref(false);
const isEditing = ref(false);
const form = ref({
  id: '',
  code: '',
  name: '',
  unit: '',
  quantityInStock: 0
});

onMounted(() => {
  store.fetchAll();
});

const openCreate = () => {
  form.value = { id: '', code: '', name: '', unit: '', quantityInStock: 0 };
  isEditing.value = false;
  showModal.value = true;
};

const openEdit = (item: any) => {
  form.value = { ...item };
  isEditing.value = true;
  showModal.value = true;
};

const save = async () => {
  try {
    if (isEditing.value) {
      await store.update(form.value.id, {
        code: form.value.code,
        name: form.value.name,
        unit: form.value.unit,
        quantityInStock: Number(form.value.quantityInStock)
      });
    } else {
      await store.create({
        code: form.value.code,
        name: form.value.name,
        unit: form.value.unit,
        quantityInStock: Number(form.value.quantityInStock)
      });
    }
    showModal.value = false;
  } catch (e) {
    alert('Error saving');
  }
};

const remove = async (id: string) => {
  if (confirm(t('common.confirmDelete'))) {
    await store.remove(id);
  }
};
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-800">{{ t('rawMaterial.title') }}</h1>
      <button @click="openCreate" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
        {{ t('common.create') }}
      </button>
    </div>

    <div v-if="store.loading" class="text-center py-4">{{ t('common.loading') }}</div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="item in store.rawMaterials" :key="item.id" class="bg-white p-6 rounded-lg shadow border border-gray-200">
        <div class="flex justify-between items-start">
          <div>
            <h3 class="text-lg font-semibold text-gray-800">{{ item.name }}</h3>
            <p class="text-sm text-gray-500">{{ item.code }}</p>
          </div>
          <span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">
            {{ item.quantityInStock }} {{ item.unit }}
          </span>
        </div>
        <div class="mt-4 flex justify-end space-x-2">
          <button @click="openEdit(item)" class="text-indigo-600 hover:text-indigo-900 text-sm font-medium">
            {{ t('common.edit') }}
          </button>
          <button @click="remove(item.id!)" class="text-red-600 hover:text-red-900 text-sm font-medium">
            {{ t('common.delete') }}
          </button>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center p-4">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-md p-6">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? t('rawMaterial.edit') : t('rawMaterial.new') }}</h2>
        <form @submit.prevent="save">
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">{{ t('common.code') }}</label>
            <input v-model="form.code" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">{{ t('common.name') }}</label>
            <input v-model="form.name" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">{{ t('common.unit') }}</label>
            <input v-model="form.unit" required class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700">{{ t('common.stock') }}</label>
            <input type="number" step="0.01" v-model="form.quantityInStock" required min="0" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 border p-2" />
          </div>
          <div class="flex justify-end space-x-3">
            <button type="button" @click="showModal = false" class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
              {{ t('common.cancel') }}
            </button>
            <button type="submit" class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
              {{ t('common.save') }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
