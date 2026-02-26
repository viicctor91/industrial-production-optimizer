export interface RawMaterial {
    id?: string;
    code: string;
    name: string;
    unit: string;
    quantityInStock: number;
}

export interface ProductCompositionItem {
    id?: string;
    rawMaterialId: string;
    rawMaterialName?: string;
    rawMaterialUnit?: string;
    quantityRequiredPerUnit: number;
}

export interface Product {
    id?: string;
    code: string;
    name: string;
    price: number;
    composition: ProductCompositionItem[];
}

export interface SuggestedItem {
    productId: string;
    productCode: string;
    productName: string;
    unitsToProduce: number;
    unitPrice: number;
    totalValue: number;
}

export interface ProductionSuggestion {
    suggestedItems: SuggestedItem[];
    totalValue: number;
    remainingStock: RawMaterial[];
}
