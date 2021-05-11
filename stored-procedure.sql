DELIMITER $$

CREATE PROCEDURE add_movie
    (IN @id VARCHAR(10), @title VARCHAR(100),
        @year INTEGER, @director VARCHAR(100),
        @genres VARCHAR(32), @starsname VARCHAR(100))
BEGIN
    INSERT INTO movies (id, title, year, director)
    VALUES (@id, @title, @year, @director)


CREATE PROCEDURE add_star
(IN @id VARCHAR(10), @name VARCHAR(100),
 @birthYear INTEGER)
BEGIN
INSERT INTO stars (id, name, birthYear)
VALUES (@id, @name, @birthYear)

