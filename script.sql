CREATE TABLE partners (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          company_name VARCHAR(255) NOT NULL,
                          transport_type VARCHAR(50),
                          geographical_zone VARCHAR(100),
                          special_conditions TEXT,
                          status VARCHAR(50),
                          creation_date DATE NOT NULL
);

CREATE TABLE contracts (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           start_date DATE NOT NULL,
                           end_date DATE,
                           special_rate NUMERIC(10, 2),
                           agreement_conditions TEXT,
                           renewable BOOLEAN,
                           status VARCHAR(50),
                           partner_id UUID REFERENCES partners(id) ON DELETE CASCADE
);

CREATE TABLE promotions (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            offer_name VARCHAR(255) NOT NULL,
                            description TEXT,
                            start_date DATE,
                            end_date DATE,
                            discount_type VARCHAR(50),
                            discount_value NUMERIC(10, 2),
                            conditions TEXT,
                            status VARCHAR(50),
                            contract_id UUID REFERENCES contracts(id) ON DELETE CASCADE
);

CREATE TABLE tickets (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         transport_type VARCHAR(50) NOT NULL,
                         purchase_price NUMERIC(10, 2) NOT NULL,
                         selling_price NUMERIC(10, 2) NOT NULL,
                         sale_date TIMESTAMP NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         partner_id UUID REFERENCES partners(id) ON DELETE CASCADE
);


SELECT enumlabel AS enum_value
FROM pg_enum
         JOIN pg_type ON pg_type.oid = pg_enum.enumtypid
WHERE pg_type.typname = 'contract_status';