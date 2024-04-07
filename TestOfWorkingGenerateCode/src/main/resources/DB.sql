--One table
CREATE TABLE IF NOT EXISTS Table0
(
    id         SERIAL PRIMARY KEY,
    fieldStr  VARCHAR(50),
    fieldName  VARCHAR(50),
    fieldInt  INT,
    fieldBool BOOLEAN,
    fieldFloat FLOAT
);
--Many to Many
CREATE TABLE IF NOT EXISTS Table1
(
    id         SERIAL PRIMARY KEY,
    fieldStr1  VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Table2
(
    id         SERIAL PRIMARY KEY,
    fieldStr2  VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Table12_ref
(
    Table1_id INT,
    Table2_id INT,
    FOREIGN KEY (Table1_id) REFERENCES Table1 (id) ON DELETE CASCADE,
    FOREIGN KEY (Table2_id) REFERENCES Table2 (id)  ON DELETE CASCADE
);
--One to Many or Many to One
CREATE TABLE IF NOT EXISTS Table4
(
    id  SERIAL PRIMARY KEY,
    fieldStr4  VARCHAR(50)

);
CREATE TABLE IF NOT EXISTS Table3
(
    id         SERIAL PRIMARY KEY,
    Table4_id  INT,
    FOREIGN KEY (Table4_id) REFERENCES Table4 (id)  ON DELETE SET NULL,
    fieldStr3  VARCHAR(50)
);

--Many to Many 2 tables
CREATE TABLE IF NOT EXISTS Table5
(
    id         SERIAL PRIMARY KEY,
    fieldStr5  VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Table5_ref
(
    Table5R_id INT,
    Table5L_id INT,
    FOREIGN KEY (Table5R_id) REFERENCES Table5 (id) ON DELETE CASCADE,
    FOREIGN KEY (Table5L_id) REFERENCES Table5 (id) ON DELETE CASCADE
);
--One to Many Or Many to One 1-table
CREATE TABLE IF NOT EXISTS Table6
(
    id SERIAL PRIMARY KEY,
    table6_id INT,
    FOREIGN KEY (Table6_id) REFERENCES Table6 (id) ON DELETE SET NULL ,
    fieldStr6  VARCHAR(50)
);


