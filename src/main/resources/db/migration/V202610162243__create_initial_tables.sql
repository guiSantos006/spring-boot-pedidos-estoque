CREATE TABLE products
(
    id          UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(255),
    price       NUMERIC(19, 2) NOT NULL,
    stock       BIGINT         NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,

    CONSTRAINT ck_products_name_not_blank CHECK (TRIM(name) <> ''),
    CONSTRAINT ck_products_price_positive_or_zero CHECK (price >= 0),
    CONSTRAINT ck_products_stock_positive_or_zero CHECK (stock >= 0)
);

CREATE TABLE orders
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status       VARCHAR(255)   NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT ck_orders_total_amount CHECK (total_amount >= 0),
    CONSTRAINT ck_orders_status CHECK (status IN ('CANCELED', 'PENDING', 'COMPLETED'))
);

CREATE TABLE order_items
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id       UUID           NOT NULL,
    product_id     UUID           NOT NULL,
    quantity       BIGINT        NOT NULL,
    purchase_price NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT ck_order_items_quantity CHECK (quantity > 0),
    CONSTRAINT ck_order_items_purchase_price CHECK (purchase_price >= 0)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_product_id ON order_items (product_id);

CREATE TABLE stock_movements
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id    UUID         NOT NULL,
    order_id      UUID,
    order_item_id UUID,
    quantity      BIGINT       NOT NULL,
    type          VARCHAR(255) NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    justification VARCHAR(255),
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_stock_movements_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_stock_movements_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_stock_movements_order_item FOREIGN KEY (order_item_id) REFERENCES order_items (id),
    CONSTRAINT ck_stock_movements_quantity CHECK (quantity > 0),
    CONSTRAINT ck_stock_movements_type CHECK (type IN ('OUTCOME', 'INCOME', 'ADJUSTMENT')),
    CONSTRAINT ck_stock_movements_reason CHECK (reason IN ('PURCHASE', 'CANCEL', 'MANUAL_ADJUSTMENT', 'CREATION'))
);

CREATE INDEX idx_stock_movement_product_id ON stock_movements (product_id);
CREATE INDEX idx_stock_movement_order_id ON stock_movements (order_id);
CREATE INDEX idx_stock_movement_order_item_id ON stock_movements (order_item_id);