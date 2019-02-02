-- Script de création de la base de données ENCHERES
--   type :      SQL Server 2012
--
USE eni_enchere;

CREATE TABLE CATEGORIES (
    no_categorie   INTEGER IDENTITY(1,1) NOT NULL,
    libelle        VARCHAR(255) NOT NULL
);

ALTER TABLE CATEGORIES ADD constraint categorie_pk PRIMARY KEY (no_categorie);

CREATE TABLE ENCHERES (
    no_utilisateur   INTEGER NOT NULL,
    no_article       INTEGER NOT NULL,
    date_enchere     datetime NOT NULL,
	montant_enchere  INTEGER NOT NULL

);

ALTER TABLE ENCHERES ADD constraint enchere_pk PRIMARY KEY (no_utilisateur, no_article);

CREATE TABLE RETRAITS (
	no_article         INTEGER NOT NULL,
    rue              VARCHAR(255) NOT NULL,
    code_postal      VARCHAR(255) NOT NULL,
    ville            VARCHAR(255) NOT NULL
);

ALTER TABLE RETRAITS ADD constraint retrait_pk PRIMARY KEY  (no_article);

CREATE TABLE UTILISATEURS (
    no_utilisateur   INTEGER IDENTITY(1,1) NOT NULL,
    pseudo           VARCHAR(30) NOT NULL,
    nom              VARCHAR(255) NOT NULL,
    prenom           VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    telephone        VARCHAR(255),
    rue              VARCHAR(255) NOT NULL,
    code_postal      VARCHAR(255) NOT NULL,
    ville            VARCHAR(255) NOT NULL,
    mot_de_passe     VARCHAR(255) NOT NULL,
    credit           INTEGER NOT NULL,
    administrateur   bit NOT NULL
);

ALTER TABLE UTILISATEURS ADD constraint utilisateur_pk PRIMARY KEY (no_utilisateur);


CREATE TABLE ARTICLES_VENDUS (
    no_article                    INTEGER IDENTITY(1,1) NOT NULL,
    nom_article                   VARCHAR(50) NOT NULL,
    description                   VARCHAR(500) NOT NULL,
	date_debut_encheres           DATE NOT NULL,
    date_fin_encheres             DATE NOT NULL,
    prix_initial                  INTEGER,
    prix_vente                    INTEGER,
    no_utilisateur                INTEGER NOT NULL,
    no_categorie                  INTEGER NOT NULL
);

ALTER TABLE ARTICLES_VENDUS ADD constraint articles_vendus_pk PRIMARY KEY (no_article);

ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_articles_vendus_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLES_VENDUS ( no_article )
    ON DELETE NO ACTION

ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_no_utilisateur FOREIGN KEY ( no_utilisateur )
        REFERENCES UTILISATEURS ( no_utilisateur )
    ON DELETE CASCADE
	
ALTER TABLE RETRAITS
    ADD CONSTRAINT retraits_articles_vendus_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLES_VENDUS ( no_article )
    ON DELETE CASCADE

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT articles_vendus_categories_fk FOREIGN KEY ( no_categorie )
        REFERENCES CATEGORIES ( no_categorie )
    ON DELETE NO ACTION

ALTER TABLE ARTICLES_VENDUS
    ADD CONSTRAINT ventes_utilisateur_fk FOREIGN KEY ( no_utilisateur )
        REFERENCES UTILISATEURS ( no_utilisateur )
    ON DELETE NO ACTION
    
ALTER TABLE ARTICLES_VENDUS
	ADD CONSTRAINT CK_articles_dates CHECK (date_fin_encheres > date_debut_encheres)

