CREATE TABLE IF NOT EXISTS Users
(
    id        SERIAL PRIMARY KEY,
    ROLE varchar(50),
    Login  VARCHAR(50) UNIQUE,
    KODE varchar(50) UNIQUE,
    Age INT,
    Name VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS Pseudonyms
(
    id  SERIAL PRIMARY KEY,
    KODE varchar(50) UNIQUE,
    Pseudonym varchar(50),
    user_id INT DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS Books
(
    id        SERIAL PRIMARY KEY,
    BookName  VARCHAR(50),
    Annotation varchar(250),
    TextRef varchar(250) DEFAULT NULL,
    PageCount INT,
    Year INT,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES AuthorsPseudonyms (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Comments
(
    id SERIAL PRIMARY KEY,
    parent_id INT DEFAULT NULL,
    user_id INT,
    chat_id INT,
    INFO varchar(250),
    FOREIGN KEY (chat_id) REFERENCES Books(id) ON DELETE CASCADE,
    FOREIGN KEY (USER_id) REFERENCES Users(id) ON DELETE CASCADE ,
    FOREIGN KEY (parent_id) REFERENCES Comments(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Audio
(
    id  SERIAL PRIMARY KEY,
    Duration INT,
    AudioRef varchar(250),
    book_id INT,
    voiceover_id INT,
    FOREIGN KEY (voiceover_id) REFERENCES AuthorsPseudonyms (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES Books (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Reading
(
    id SERIAL PRIMARY KEY,
    LastPage INT,
    book_id INT,
    reader_id INT,
    FOREIGN KEY (reader_id) REFERENCES Users (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES Books (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Listening
(
    id SERIAL PRIMARY KEY,
    LastChapter INT,
    MinuteIndex INT,
    audio_id INT,
    reader_id INT,
    FOREIGN KEY (reader_id) REFERENCES Users (id) ON DELETE CASCADE,
    FOREIGN KEY (audio_id) REFERENCES Audio (id) ON DELETE CASCADE
);




