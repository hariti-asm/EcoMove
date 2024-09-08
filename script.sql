
CREATE TYPE reservation_status AS ENUM ( 'confirmed', 'cancelled');

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
CREATE TABLE clients (
                         id UUID  PRIMARY KEY DEFAULT gen_random_uuid(),
                         last_name VARCHAR(255) NOT NULL,
                         first_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         phone_number VARCHAR(20)
);
ALTER TABLE tickets DROP CONSTRAINT fk_contract_id;

ALTER TABLE tickets
    ADD CONSTRAINT fk_contract_id
        FOREIGN KEY (contract_id) REFERENCES contracts(id)
            ON DELETE CASCADE;

CREATE TABLE journey (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         start_point VARCHAR(255) NOT NULL,
                         end_point VARCHAR(255) NOT NULL,
                         start_time TIMESTAMP NOT NULL,
                         arrival_time TIMESTAMP NOT NULL
);
CREATE TABLE reservations (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              reservation_date TIMESTAMP NOT NULL,
                              client_id UUID NOT NULL,
                              ticket_id UUID NOT NULL,
                              status reservation_status NOT NULL,
                              CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
                              CONSTRAINT fk_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);
INSERT INTO journey (id, start_point, end_point, start_time, arrival_time)
VALUES
    (gen_random_uuid(), 'New York', 'Los Angeles', '2024-09-08 08:00:00', '2024-09-08 17:00:00'),
    (gen_random_uuid(), 'Casablanca', 'Marrakech', '2024-09-09 09:00:00', '2024-09-09 11:30:00'),
    (gen_random_uuid(), 'Rabat', 'Fes', '2024-09-10 14:15:00', '2024-09-10 16:45:00'),
    (gen_random_uuid(), 'Tangier', 'Tetouan', '2024-09-11 07:00:00', '2024-09-11 08:30:00'),
    (gen_random_uuid(), 'Agadir', 'Essaouira', '2024-09-12 12:00:00', '2024-09-12 14:30:00'),
    (gen_random_uuid(), 'Chicago', 'Houston', '2024-09-09 09:30:00', '2024-09-09 14:30:00'),
    (gen_random_uuid(), 'San Francisco', 'Seattle', '2024-09-10 12:15:00', '2024-09-10 16:45:00'),
    (gen_random_uuid(), 'Meknes', 'Oujda', '2024-09-13 06:00:00', '2024-09-13 10:00:00');
