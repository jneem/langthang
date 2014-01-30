# --- !Ups

insert into PLACES (NAME, ADDRESS, LATITUDE, LONGITUDE) values ('Camel Cafe', '2701 Penny Lane', 30.307761,-97.753401);
insert into PLACES (NAME, ADDRESS, LATITUDE, LONGITUDE) values ('Potato Cafe', '2701 Penny Lane', 30.307761,-97.753401);

insert into PHOTOS (PLACE_ID) values(select (ID) from PLACES where NAME = 'Camel Cafe');
insert into PHOTOS (PLACE_ID) values(select (ID) from PLACES where NAME = 'Camel Cafe');

# --- !Downs


delete from PHOTOS;
delete from PLACES;
