import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import i18n from '@/i18n'
import ProductionView from '@/views/ProductionView.vue'
import { useProductionStore } from '@/stores/production'

// Mock i18n
const i18nMock = {
    global: {
        t: (key: string) => key
    }
}

describe('ProductionView', () => {
    let wrapper: any;
    let store: any;

    beforeEach(() => {
        wrapper = mount(ProductionView, {
            global: {
                plugins: [createTestingPinia({ createSpy: vi.fn }), i18n],
            }
        });
        store = useProductionStore();
    });

    it('renders "Suggest Production" button initially', () => {
        expect(wrapper.text()).toContain(i18n.global.t('production.suggestButton') as string);
    });

    it('renders suggested items and total value when store has data', async () => {
        // Mock store state
        store.suggestion = {
            totalValue: 1000.0,
            suggestedItems: [
                {
                    productId: '1',
                    productCode: 'P1',
                    productName: 'Product 1',
                    unitsToProduce: 10,
                    unitPrice: 100.0,
                    totalValue: 1000.0
                }
            ],
            remainingStock: []
        };

        await wrapper.vm.$nextTick();

        expect(wrapper.text()).toContain('$1000.00');
        expect(wrapper.text()).toContain('Product 1');
        expect(wrapper.text()).toContain('10 units');
    });

    it('calls fetchSuggestion when button is clicked', async () => {
        const button = wrapper.find('button');
        await button.trigger('click');
        expect(store.fetchSuggestion).toHaveBeenCalled();
    });
});
