--Many to many Ref
CREATE TABLE IF NOT EXISTS Table1(
    id SERIAL PRIMARY KEY,
    fieldStr1 VARCHAR(50) NOT NULL,
    fieldInt1 INT,
    fieldBool1 BOOLEAN
);
CREATE TABLE IF NOT EXISTS Table2(
    id SERIAL PRIMARY KEY,
    fieldStr2 VARCHAR(50) NOT NULL,
    fieldInt2 INT,
    fieldBool2 BOOLEAN
);
CREATE TABLE IF NOT EXISTS Table12_ref(
    Table1_id INT,
    Table2_id INT,
    FOREIGN KEY (Table1_id) REFERENCES Table1(id),
    FOREIGN KEY (Table2_id) REFERENCES Table2(id)
);
CREATE TABLE IF NOT EXISTS Table3(
    id SERIAL PRIMARY KEY,
    Table2_id INT,
    FOREIGN KEY (Table2_id) REFERENCES Table1(id),
    fieldStr3 VARCHAR(50) NOT NULL,
    fieldInt3 INT,
    fieldBool3 BOOLEAN
);
CREATE TABLE IF NOT EXISTS Table4(
    id SERIAL PRIMARY KEY,
    Table31_id INT,
    FOREIGN KEY (Table31_id) REFERENCES Table3(id),
    Table32_id INT,
    FOREIGN KEY (Table32_id) REFERENCES Table3(id)
);