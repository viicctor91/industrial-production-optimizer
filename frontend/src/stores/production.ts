import { defineStore } from 'pinia';
import api from '@/api';
import type { ProductionSuggestion } from '@/types';

export const useProductionStore = defineStore('production', {
    state: () => ({
        suggestion: null as ProductionSuggestion | null,
        loading: false,
        error: null as string | null,
    }),
    actions: {
        async fetchSuggestion() {
            this.loading = true;
            try {
                const response = await api.post<ProductionSuggestion>('/production/suggest');
                this.suggestion = response.data;
            } catch (err: any) {
                this.error = err.message || 'Failed to fetch suggestion';
            } finally {
                this.loading = false;
            }
        }
    }
});
