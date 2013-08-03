

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'RetailX';
DROP DATABASE IF EXISTS RetailX;
create database RetailX;
use RetailX;

CREATE TABLE products  (
	id SERIAL PRIMARY KEY,
	name VARCHAR(200) NOT NULL,
	barcode VARCHAR(1000),
	buy_price NUMERIC(10,2),
	sell_price NUMERIC(10,2) NOT NULL,
	box_quantity INTEGER,
	reorder_percentage NUMERIC(3,2),
	default_supplier_id INTEGER
);

CREATE TABLE product_stocks (
	id SERIAL PRIMARY KEY,
	product_id INTEGER REFERENCES products(id) NOT NULL,
	bought_quantity INTEGER,
	remaining_quantity INTEGER,
	comments VARCHAR(300),
	buy_price NUMERIC(10,2),
	sell_price NUMERIC(10, 2),
	purchase_date DATE
);

