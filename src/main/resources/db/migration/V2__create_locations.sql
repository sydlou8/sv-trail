CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    tech_company VARCHAR(255) NOT NULL,
    order_index INT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);