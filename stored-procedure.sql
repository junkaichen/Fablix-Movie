use moviedb;

DELIMITER $$
CREATE FUNCTION movie_exist(mTitle VARCHAR(100),mYear INTEGER, mDirector VARCHAR(100))
    RETURNS INTEGER
    DETERMINISTIC
BEGIN
    DECLARE val INTEGER;
    SET val = (SELECT COUNT(*) FROM movies where title like mTitle
                                             AND year = mYear AND director LIKE mDirector);

    IF ( val = 0 ) THEN
        RETURN 0;
    ELSE
        RETURN 1;
    END IF;

END
$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION genre_exist(gName VARCHAR(32))
    RETURNS INTEGER
    DETERMINISTIC
BEGIN
    DECLARE val INTEGER;
    SET val = (SELECT COUNT(*) FROM genres where name like gName);

    IF ( val = 0 ) THEN
        RETURN 0;
    ELSE
        RETURN 1;
    END IF;
END
$$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION star_exist(sName VARCHAR(100), sYear INTEGER)
    RETURNS INTEGER
    DETERMINISTIC
BEGIN
    DECLARE val INTEGER;
    SET val = (SELECT COUNT(*) FROM stars where starname like sName AND birthYear = sYear);

    IF ( val = 0 ) THEN
        RETURN 0;
    ELSE
        RETURN 1;
    END IF;
END
$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE add_movie(IN newId VARCHAR(10), newTitle VARCHAR(100), newYear INTEGER, newDirector VARCHAR(100), newGenre INTEGER, newStar VARCHAR(10), newStarYear INTEGER, dummyID VARCHAR(10))
BEGIN
    DECLARE mE INTEGER;
    DECLARE gE INTEGER;
    DECLARE sE INTEGER;
    DECLARE sID VARCHAR(10);
    DECLARE gID INTEGER;
    SET mE = (SELECT movie_exist(newTitle,newYear,newDirector));
    SET gE = (SELECT genre_exist(newGenre));
    SET sE = (SELECT star_exist(newStar,newStarYear));
    IF (mE = 0) THEN
        IF (gE = 1) THEN
            SET gID = (SELECT id from genres where name like newGenre);
            IF (sE = 1) THEN
                SET sID = (SELECT id from stars where starname like newStar and birthYear like newStarYear);
                INSERT INTO movies(id,title,year,director)  VALUES(newID,newTitle,newYear,newDirector);
                INSERT INTO stars_in_movies(starId,movieId)  VALUES(sID,newId);
                INSERT INTO genres_in_movies(genreId,movieId)  VALUES(gID,newId);
                INSERT INTO ratings(movieId,rating,numVotes) VALUES(newId,7.5,432);
                SELECT CONCAT(newTitle, " tmovie was added...") as answer;

            ELSE
                INSERT INTO stars (id,starname,birthYear) VALUES (dummyID,newStar,birthYear);
                SET sID = dummyID;
                INSERT INTO movies(id,title,year,director)  VALUES(newID,newTitle,newYear,newDirector);
                INSERT INTO stars_in_movies(starId,movieId)  VALUES(sID,newId);
                INSERT INTO genres_in_movies(genreId,movieId)  VALUES(gID,newId);
                INSERT INTO ratings(movieId,rating,numVotes) VALUES(newId,7.5,432);
                SELECT CONCAT(newTitle, " movie was added...new star") as answer;
            END IF;
        ELSE
            INSERT INTO genres (name) VALUES (newGenre);
            SET gID = (SELECT id FROM genres WHERE name LIKE newGenre);
            IF (sE = 1) THEN
                SET sID = (SELECT id from stars where starname like newStar and birthYear like newStarYear);
                INSERT INTO movies(id,title,year,director)  VALUES(newID,newTitle,newYear,newDirector);
                INSERT INTO stars_in_movies(starId,movieId)  VALUES(newStar,newId);
                INSERT INTO genres_in_movies(genreId,movieId)  VALUES(newGenre,newId);
                INSERT INTO ratings(movieId,rating,numVotes) VALUES(newId,7.5,432);
                SELECT CONCAT(newTitle, " tmovie was added...new genre") as answer;

            ELSE
                INSERT INTO stars (id,starname,birthYear) VALUES (dummyID,newStar,birthYear);
                SET sID = dummyID;
                INSERT INTO movies(id,title,year,director)  VALUES(newID,newTitle,newYear,newDirector);
                INSERT INTO stars_in_movies(starId,movieId)  VALUES(newStar,newId);
                INSERT INTO genres_in_movies(genreId,movieId)  VALUES(newGenre,newId);
                INSERT INTO ratings(movieId,rating,numVotes) VALUES(newId,7.5,432);
                SELECT CONCAT(newTitle, " movie was added...new star,new genre") as answer;
            END IF;
        END IF;
    ELSE
        SELECT CONCAT(newTitle, " this movie already exists") as answer;

    END IF;


END
$$
DELIMITER ;