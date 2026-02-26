import { defineStore } from 'pinia';
import api from '@/api';
import type { RawMaterial } from '@/types';

export const useRawMaterialStore = defineStore('rawMaterial', {
    state: () => ({
        rawMaterials: [] as RawMaterial[],
        loading: false,
        error: null as string | null,
    }),
    actions: {
        async fetchAll() {
            this.loading = true;
            try {
                const response = await api.get<RawMaterial[]>('/raw-materials');
                this.rawMaterials = response.data;
            } catch (err: any) {
                this.error = err.message || 'Failed to fetch raw materials';
            } finally {
                this.loading = false;
            }
        },
        async create(data: Omit<RawMaterial, 'id'>) {
            this.loading = true;
            try {
                await api.post('/raw-materials', data);
                await this.fetchAll();
            } catch (err: any) {
                this.error = err.response?.data?.message || err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },
        async update(id: string, data: Omit<RawMaterial, 'id'>) {
            this.loading = true;
            try {
                await api.put(`/raw-materials/${id}`, data);
                await this.fetchAll();
            } catch (err: any) {
                this.error = err.response?.data?.message || err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },
        async remove(id: string) {
            this.loading = true;
            try {
                await api.delete(`/raw-materials/${id}`);
                await this.fetchAll();
            } catch (err: any) {
                this.error = err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        }
    }
});
