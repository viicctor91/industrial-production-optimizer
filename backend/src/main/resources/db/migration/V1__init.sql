CREATE TABLE raw_materials (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    quantity_in_stock NUMERIC(18,3) NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(18,2) NOT NULL
);

CREATE TABLE product_materials (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    raw_material_id UUID NOT NULL REFERENCES raw_materials(id),
    quantity_required_per_unit NUMERIC(18,3) NOT NULL
);

CREATE INDEX idx_product_materials_product ON product_materials(product_id);
CREATE INDEX idx_product_materials_raw ON product_materials(raw_material_id);
