import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import i18n from '@/i18n'
import { createRouter, createMemoryHistory } from 'vue-router'
import ProductFormView from '@/views/ProductFormView.vue'
import { useRawMaterialStore } from '@/stores/rawMaterial'

// Mock router
const routerMock = {
    push: vi.fn()
}
const routeMock = {
    params: {}
}

describe('ProductFormView', () => {
    let wrapper: any;
    let rawMaterialStore: any;

    beforeEach(async () => {
        const testRouter = createRouter({
            history: createMemoryHistory(),
            routes: [
                { path: '/products/new', component: ProductFormView },
                { path: '/products/:id/edit', component: ProductFormView },
            ],
        })
        await testRouter.push('/products/new')
        await testRouter.isReady()

        wrapper = mount(ProductFormView, {
            global: {
                plugins: [createTestingPinia({ createSpy: vi.fn }), i18n, testRouter],
                mocks: {
                    $router: routerMock
                },
                stubs: ['router-link']
            }
        });
        rawMaterialStore = useRawMaterialStore();
        rawMaterialStore.rawMaterials = [
            { id: '1', name: 'Iron', unit: 'kg' },
            { id: '2', name: 'Wood', unit: 'kg' }
        ];
    });

    it('adds a composition item when "Add" button is clicked', async () => {
        // Initially 0 composition items
        expect(wrapper.findAll('.bg-gray-50').length).toBe(0);

        // Find "Add" button (text contains "+ common.add")
        const buttons = wrapper.findAll('button');
        const addButton = buttons.find((b: any) => b.text().includes(`+ ${i18n.global.t('common.add')}`));
        
        await addButton.trigger('click');

        // Should have 1 item
        expect(wrapper.findAll('.bg-gray-50').length).toBe(1);
    });

    it('removes a composition item when "Remove" button is clicked', async () => {
        // Add one item first
        const buttons = wrapper.findAll('button');
        const addButton = buttons.find((b: any) => b.text().includes(`+ ${i18n.global.t('common.add')}`));
        await addButton.trigger('click');
        
        expect(wrapper.findAll('.bg-gray-50').length).toBe(1);

        // Find remove button
        const removeButton = wrapper.find('button.text-red-600'); // Based on class in template
        await removeButton.trigger('click');

        expect(wrapper.findAll('.bg-gray-50').length).toBe(0);
    });
});
