import { createI18n } from 'vue-i18n';

const messages = {
    en: {
        nav: {
            rawMaterials: 'Raw Materials',
            products: 'Products',
            production: 'Production Plan',
            language: 'Language'
        },
        common: {
            actions: 'Actions',
            edit: 'Edit',
            delete: 'Delete',
            create: 'Create',
            save: 'Save',
            cancel: 'Cancel',
            loading: 'Loading...',
            confirmDelete: 'Are you sure you want to delete this item?',
            code: 'Code',
            name: 'Name',
            unit: 'Unit',
            price: 'Price',
            quantity: 'Quantity',
            stock: 'Stock',
            required: 'Required',
            add: 'Add',
            remove: 'Remove'
        },
        rawMaterial: {
            title: 'Raw Materials',
            new: 'New Raw Material',
            edit: 'Edit Raw Material'
        },
        product: {
            title: 'Products',
            new: 'New Product',
            edit: 'Edit Product',
            composition: 'Composition',
            selectMaterial: 'Select Material'
        },
        production: {
            title: 'Production Suggestion',
            suggestButton: 'Suggest Production Plan',
            totalValue: 'Total Sales Value',
            plan: 'Suggested Plan',
            remainingStock: 'Remaining Stock',
            noPlan: 'No production suggested based on current stock.'
        }
    },
    pt: {
        nav: {
            rawMaterials: 'Matérias-Primas',
            products: 'Produtos',
            production: 'Plano de Produção',
            language: 'Idioma'
        },
        common: {
            actions: 'Ações',
            edit: 'Editar',
            delete: 'Excluir',
            create: 'Criar',
            save: 'Salvar',
            cancel: 'Cancelar',
            loading: 'Carregando...',
            confirmDelete: 'Tem certeza que deseja excluir este item?',
            code: 'Código',
            name: 'Nome',
            unit: 'Unidade',
            price: 'Preço',
            quantity: 'Quantidade',
            stock: 'Estoque',
            required: 'Obrigatório',
            add: 'Adicionar',
            remove: 'Remover'
        },
        rawMaterial: {
            title: 'Matérias-Primas',
            new: 'Nova Matéria-Prima',
            edit: 'Editar Matéria-Prima'
        },
        product: {
            title: 'Produtos',
            new: 'Novo Produto',
            edit: 'Editar Produto',
            composition: 'Composição',
            selectMaterial: 'Selecionar Material'
        },
        production: {
            title: 'Sugestão de Produção',
            suggestButton: 'Sugerir Plano',
            totalValue: 'Valor Total de Vendas',
            plan: 'Plano Sugerido',
            remainingStock: 'Estoque Restante',
            noPlan: 'Nenhuma produção sugerida com o estoque atual.'
        }
    }
};

const i18n = createI18n({
    legacy: false,
    locale: 'pt', // Default to pt-BR as requested
    fallbackLocale: 'en',
    messages,
});

export default i18n;
