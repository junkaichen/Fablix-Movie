use moviedb;

DELIMITER $$

CREATE PROCEDURE add_movie(IN newId VARCHAR(10), newTitle VARCHAR(100), newYear INTEGER, newDirector VARCHAR(100), newGenre INTEGER, newStar VARCHAR(10))
BEGIN
    INSERT INTO movies(id,title,year,director)  VALUES(newID,newTitle,newYear,newDirector);
    INSERT INTO stars_in_movies(starId,movieId)  VALUES(newStar,newId);
    INSERT INTO genres_in_movies(genreId,movieId)  VALUES(newGenre,newId);
    INSERT INTO ratings(movieId,rating,numVotes) VALUES(newId,7.5,432);
END
$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE add_star(IN newId VARCHAR(10), newStarname VARCHAR(100), newBirthYear INTEGER)
BEGIN
    INSERT INTO  stars(id,starname,birthYear) VALUES (newId,newStarname,newBirthYear);
END
$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE add_genre(newName VARCHAR(32))
BEGIN
    INSERT INTO genres(name) VALUES (newName);
END
$$

DELIMITER ;