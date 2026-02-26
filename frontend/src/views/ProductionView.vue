<script setup lang="ts">
import { useProductionStore } from '@/stores/production';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const store = useProductionStore();

const suggest = async () => {
  await store.fetchSuggestion();
};
</script>

<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h1 class="text-2xl font-bold text-gray-800">{{ t('production.title') }}</h1>
      <button @click="suggest" :disabled="store.loading" class="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 disabled:opacity-50 shadow-lg font-semibold">
        {{ store.loading ? t('common.loading') : t('production.suggestButton') }}
      </button>
    </div>

    <div v-if="store.suggestion" class="space-y-8">
      <!-- Summary Card -->
      <div class="bg-gradient-to-r from-indigo-500 to-purple-600 rounded-lg shadow-lg p-6 text-white">
        <h2 class="text-lg font-medium opacity-90">{{ t('production.totalValue') }}</h2>
        <p class="text-4xl font-bold mt-2">${{ store.suggestion.totalValue.toFixed(2) }}</p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <!-- Suggested Production List -->
        <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-200 bg-gray-50">
            <h3 class="text-lg font-medium text-gray-900">{{ t('production.plan') }}</h3>
          </div>
          <div v-if="store.suggestion.suggestedItems.length === 0" class="p-6 text-gray-500 text-center">
            {{ t('production.noPlan') }}
          </div>
          <ul v-else class="divide-y divide-gray-200">
            <li v-for="item in store.suggestion.suggestedItems" :key="item.productId" class="px-6 py-4 hover:bg-gray-50 transition">
              <div class="flex justify-between items-center">
                <div>
                  <p class="text-sm font-medium text-indigo-600 truncate">{{ item.productName }}</p>
                  <p class="text-xs text-gray-500">{{ item.productCode }}</p>
                </div>
                <div class="text-right">
                  <p class="text-lg font-bold text-gray-900">{{ item.unitsToProduce }} units</p>
                  <p class="text-xs text-gray-500">@ ${{ item.unitPrice }} = ${{ item.totalValue.toFixed(2) }}</p>
                </div>
              </div>
            </li>
          </ul>
        </div>

        <!-- Remaining Stock List -->
        <div class="bg-white rounded-lg shadow border border-gray-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-gray-200 bg-gray-50">
            <h3 class="text-lg font-medium text-gray-900">{{ t('production.remainingStock') }}</h3>
          </div>
          <ul class="divide-y divide-gray-200 max-h-[500px] overflow-y-auto">
            <li v-for="stock in store.suggestion.remainingStock" :key="stock.id" class="px-6 py-3 hover:bg-gray-50">
              <div class="flex justify-between items-center">
                <span class="text-sm font-medium text-gray-700">{{ stock.name }}</span>
                <span :class="{'text-red-600 font-bold': stock.quantityInStock === 0, 'text-green-600': stock.quantityInStock > 0}" class="text-sm">
                  {{ stock.quantityInStock }} {{ stock.unit }}
                </span>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
    
    <div v-else-if="!store.loading && !store.suggestion" class="text-center py-12 text-gray-500 bg-white rounded-lg border border-dashed border-gray-300">
      <p>Click "Suggest Production Plan" to calculate optimal production.</p>
    </div>
  </div>
</template>
