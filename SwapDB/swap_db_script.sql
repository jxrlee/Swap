CREATE TABLE users ( 
	phone                VARCHAR( 10 )  NOT NULL  ,
	premium              BIT  NOT NULL  ,
	startdate            DATETIME    ,
	CONSTRAINT pk_users PRIMARY KEY ( phone )
 ) engine=InnoDB;
 
CREATE TABLE items ( 
	id                   INT  NOT NULL  AUTO_INCREMENT,
	title                VARCHAR( 50 )  NOT NULL  ,
	price                FLOAT  NOT NULL  ,
	description          VARCHAR( 255 )  NOT NULL  ,
	date                 DATETIME   DEFAULT CURRENT_TIMESTAMP ,
	featured             BIT    ,
	rating               TINYINT    ,
	location             VARCHAR( 50 )    ,
	available            BIT    ,
	sellerid             VARCHAR( 10 )  NOT NULL  ,
	CONSTRAINT pk_items PRIMARY KEY ( id )
 ) engine=InnoDB;

CREATE INDEX idx_items ON items ( sellerid );

ALTER TABLE items ADD CONSTRAINT fk_items_users FOREIGN KEY ( sellerid ) REFERENCES users( phone ) ON DELETE CASCADE ON UPDATE NO ACTION;

