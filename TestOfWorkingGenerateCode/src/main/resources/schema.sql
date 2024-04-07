
CREATE TABLE IF NOT EXISTS Users
(
    id        SERIAL PRIMARY KEY,
    UserName  VARCHAR(50)

);
CREATE TABLE IF NOT EXISTS Books
(
    id        SERIAL PRIMARY KEY,
    BookName  VARCHAR(50),
    Year INT,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES Users (id)
);
CREATE TABLE IF NOT EXISTS Types
(
    id        SERIAL PRIMARY KEY,
    Info  VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS book_has_type
(
    book_id INT,
    FOREIGN KEY (book_id) REFERENCES Books (id),
    type_id INT,
    FOREIGN KEY (type_id) REFERENCES Types (id)
);





