import { defineStore } from 'pinia';
import api from '@/api';
import type { Product } from '@/types';

export const useProductStore = defineStore('product', {
    state: () => ({
        products: [] as Product[],
        currentProduct: null as Product | null,
        loading: false,
        error: null as string | null,
    }),
    actions: {
        async fetchAll() {
            this.loading = true;
            try {
                const response = await api.get<Product[]>('/products');
                this.products = response.data;
            } catch (err: any) {
                this.error = err.message || 'Failed to fetch products';
            } finally {
                this.loading = false;
            }
        },
        async fetchById(id: string) {
            this.loading = true;
            try {
                const response = await api.get<Product>(`/products/${id}`);
                this.currentProduct = response.data;
            } catch (err: any) {
                this.error = err.message;
            } finally {
                this.loading = false;
            }
        },
        async create(data: Omit<Product, 'id'>) {
            this.loading = true;
            try {
                await api.post('/products', data);
                await this.fetchAll();
            } catch (err: any) {
                this.error = err.response?.data?.message || err.message;
                throw err;
            } finally {
                this.loading = false;
            }
        },
        async update(id: string, data: Omit<Product, 'id'>) {
            this.loading = true;
            try {
                await api.put(`/products/${id}`, data);
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
                await api.delete(`/products/${id}`);
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
