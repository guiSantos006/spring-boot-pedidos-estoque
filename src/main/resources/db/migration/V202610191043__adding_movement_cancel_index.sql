CREATE UNIQUE INDEX uq_stock_movement_cancel_income_per_item
    ON stock_movement (order_item_id) WHERE type = 'INCOME'
  AND reason = 'CANCEL'
  AND order_item_id IS NOT NULL;