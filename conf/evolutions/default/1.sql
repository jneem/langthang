# --- !Ups

create table PLACES (
  ID bigint not null auto_increment,
  NAME varchar not null,
  ADDRESS varchar,
  LATITUDE decimal(10, 6),
  LONGITUDE decimal(10, 6),
  
  primary key(ID)
  )
  ;
  
create table PHOTOS (
  ID bigint not null auto_increment,
  PLACE_ID bigint,
  
  primary key(ID),
  constraint FK_PLACE foreign key(PLACE_ID) references PLACES(ID)
  )
  ;

# --- !Downs

drop table PLACES;
drop table PHOTOS;
