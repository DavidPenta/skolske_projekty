CREATE TYPE "typ_registracie" AS ENUM (
  'cez_hru',
  'facebook',
  'google'
);

CREATE TABLE "Pouzivatel" (
  "id" SERIAL PRIMARY KEY,
  "nick" varchar(20) UNIQUE NOT NULL,
  "meno" varchar NOT NULL,
  "typ_registracie" enum NOT NULL,
  "heslo_hash" varchar NOT NULL,
  "email" varchar,
  "email_potvrdeny" boolean,
  "cas_vytvorenia" timestamp,
  "kod_krajiny" int,
  "tim_id" int
);

CREATE TABLE "Priatelia" (
  "id" SERIAL PRIMARY KEY,
  "pouzivatel_id_1" int NOT NULL,
  "pouzivatel_id_2" int NOT NULL,
  "postava_id_1" int NOT NULL,
  "postava_id_2" int NOT NULL,
  "cas_vytvorenia" timestamp
);

CREATE TABLE "Tim" (
  "id" SERIAL PRIMARY KEY,
  "meno" varchar UNIQUE NOT NULL,
  "pocet_clenov" int,
  "cas_vytvorenia" timestamp
);

CREATE TABLE "Ziadosti_o_priatelstvo" (
  "id" SERIAL PRIMARY KEY,
  "pouzivatel_id_1" int NOT NULL,
  "pouzivatel_id_2" int NOT NULL,
  "prijatie" boolean,
  "cas_vytvorenia" timestamp,
  "cas_prijatia" timestamp
);

CREATE TABLE "Pozvanka_do_timu" (
  "id" SERIAL PRIMARY KEY,
  "pouzivatel_id_1" int NOT NULL,
  "pouzivatel_id_2" int NOT NULL,
  "tim_id" int NOT NULL,
  "prijatie" boolean,
  "cas_vytvorenia" timestamp
);

CREATE TABLE "Timovy_chat" (
  "id" SERIAL PRIMARY KEY,
  "pouzivatel_id" int NOT NULL,
  "tim_id" int NOT NULL,
  "sprava" varchar NOT NULL,
  "cas_vytvorenia" timestamp
);

CREATE TABLE "Chat_medzi_priatelmi" (
  "id" SERIAL PRIMARY KEY,
  "pouzivatel_id_1" int NOT NULL,
  "pouzivatel_id_2" int NOT NULL,
  "sprava" varchar NOT NULL,
  "cas_vytvorenia" timestamp
);

CREATE TABLE "Postava" (
  "id" SERIAL PRIMARY KEY,
  "meno" varchar NOT NULL,
  "pouzivatel_id" int NOT NULL,
  "rola_id" int NOT NULL,
  "poloha_id" int,
  "level_id" int,
  "predmet_id" int,
  "EXP" int NOT NULL,
  "pocet_zivotov" int NOT NULL,
  "utocne_cislo" int NOT NULL,
  "obranne_cislo" int NOT NULL
);

CREATE TABLE "Level" (
  "id" SERIAL PRIMARY KEY,
  "nazov" varchar,
  "rola_id" int,
  "start_EXP" int,
  "end_EXP" int
);

CREATE TABLE "Poloha" (
  "id" SERIAL PRIMARY KEY,
  "meno" varchar,
  "poschodie" int NOT NULL,
  "potrebny_level_id" int,
  "x_suradnica" int,
  "y_suradnica" int
);

CREATE TABLE "Uloha" (
  "id" SERIAL PRIMARY KEY,
  "meno" varchar,
  "popis" varchar,
  "od_level_id" int,
  "do_level_id" int,
  "poloha_id" int,
  "predmet_id" int,
  "level_id" int,
  "EXP" int
);

CREATE TABLE "Prisera" (
  "id" SERIAL PRIMARY KEY,
  "meno" varchar UNIQUE NOT NULL,
  "popis" varchar,
  "rola_id" int,
  "poloha_id" int,
  "level_id" int,
  "od_level_id" int,
  "do_level_id" int,
  "predmet_id" int,
  "ulohat_pred_id" int,
  "prisera_pred_id" int,
  "EXP" int,
  "pocet_zivotov" int NOT NULL,
  "utocne_cislo" int NOT NULL,
  "obranne_cislo" int NOT NULL
);

CREATE TABLE "Suboj" (
  "id" SERIAL PRIMARY KEY,
  "prisera_id" int,
  "postava_id" int,
  "zabitie_prisery" boolean,
  "zabitie_postavy" boolean,
  "poloha_id" int,
  "EXP" int,
  "cas_zacatia" timestamp,
  "cas_ukoncenia" timestamp
);

CREATE TABLE "Udalost" (
  "id" SERIAL PRIMARY KEY,
  "nazov" varchar NOT NULL,
  "popis" varchar,
  "suboj_id" int NOT NULL,
  "poloha_id" int,
  "EXP" int,
  "cas" timestamp
);

CREATE TABLE "Postava_Uloha" (
  "id" SERIAL PRIMARY KEY,
  "dokoncena" boolean,
  "postava_id" int NOT NULL,
  "ulohat_id" int NOT NULL
);

CREATE TABLE "Postava_Schopnost" (
  "id" SERIAL PRIMARY KEY,
  "postava_id" int NOT NULL,
  "schopnost_id" int NOT NULL
);

CREATE TABLE "Rola" (
  "id" SERIAL PRIMARY KEY,
  "nazov" varchar UNIQUE NOT NULL,
  "popis" varchar,
  "zvysenie_pocetu_zivotov" int,
  "zvysenie_utocneho_cisla" int,
  "zvysenie_obranneho_cisla" int
);

CREATE TABLE "Schopnost" (
  "id" SERIAL PRIMARY KEY,
  "rola_id" int,
  "od_level_id" int,
  "nazov" varchar NOT NULL,
  "popis" varchar
);

CREATE TABLE "Predmet" (
  "id" SERIAL PRIMARY KEY,
  "nazov" varchar NOT NULL,
  "popis" varchar,
  "poloha_id" int,
  "zvysenie_pocetu_zivotov" int,
  "zvysenie_utocneho_cisla" int,
  "zvysenie_obranneho_cisla" int
);

ALTER TABLE "Pouzivatel" ADD FOREIGN KEY ("tim_id") REFERENCES "Tim" ("id");

ALTER TABLE "Priatelia" ADD FOREIGN KEY ("pouzivatel_id_1") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Priatelia" ADD FOREIGN KEY ("pouzivatel_id_2") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Priatelia" ADD FOREIGN KEY ("postava_id_1") REFERENCES "Postava" ("id");

ALTER TABLE "Priatelia" ADD FOREIGN KEY ("postava_id_2") REFERENCES "Postava" ("id");

ALTER TABLE "Ziadosti_o_priatelstvo" ADD FOREIGN KEY ("pouzivatel_id_1") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Ziadosti_o_priatelstvo" ADD FOREIGN KEY ("pouzivatel_id_2") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Pozvanka_do_timu" ADD FOREIGN KEY ("pouzivatel_id_1") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Pozvanka_do_timu" ADD FOREIGN KEY ("pouzivatel_id_2") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Pozvanka_do_timu" ADD FOREIGN KEY ("tim_id") REFERENCES "Tim" ("id");

ALTER TABLE "Timovy_chat" ADD FOREIGN KEY ("pouzivatel_id") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Timovy_chat" ADD FOREIGN KEY ("tim_id") REFERENCES "Tim" ("id");

ALTER TABLE "Chat_medzi_priatelmi" ADD FOREIGN KEY ("pouzivatel_id_1") REFERENCES "Priatelia" ("pouzivatel_id_1");

ALTER TABLE "Chat_medzi_priatelmi" ADD FOREIGN KEY ("pouzivatel_id_2") REFERENCES "Priatelia" ("pouzivatel_id_2");

ALTER TABLE "Postava" ADD FOREIGN KEY ("pouzivatel_id") REFERENCES "Pouzivatel" ("id");

ALTER TABLE "Postava" ADD FOREIGN KEY ("rola_id") REFERENCES "Rola" ("id");

ALTER TABLE "Postava" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");

ALTER TABLE "Postava" ADD FOREIGN KEY ("level_id") REFERENCES "Level" ("id");

ALTER TABLE "Postava" ADD FOREIGN KEY ("predmet_id") REFERENCES "Predmet" ("id");

ALTER TABLE "Level" ADD FOREIGN KEY ("rola_id") REFERENCES "Rola" ("id");

ALTER TABLE "Poloha" ADD FOREIGN KEY ("potrebny_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Uloha" ADD FOREIGN KEY ("od_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Uloha" ADD FOREIGN KEY ("do_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Uloha" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");

ALTER TABLE "Uloha" ADD FOREIGN KEY ("predmet_id") REFERENCES "Predmet" ("id");

ALTER TABLE "Uloha" ADD FOREIGN KEY ("level_id") REFERENCES "Level" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("rola_id") REFERENCES "Rola" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("level_id") REFERENCES "Level" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("od_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("do_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("predmet_id") REFERENCES "Predmet" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("ulohat_pred_id") REFERENCES "Uloha" ("id");

ALTER TABLE "Prisera" ADD FOREIGN KEY ("prisera_pred_id") REFERENCES "Prisera" ("id");

ALTER TABLE "Suboj" ADD FOREIGN KEY ("prisera_id") REFERENCES "Prisera" ("id");

ALTER TABLE "Suboj" ADD FOREIGN KEY ("postava_id") REFERENCES "Postava" ("id");

ALTER TABLE "Suboj" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");

ALTER TABLE "Udalost" ADD FOREIGN KEY ("suboj_id") REFERENCES "Suboj" ("id");

ALTER TABLE "Udalost" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");

ALTER TABLE "Postava_Uloha" ADD FOREIGN KEY ("postava_id") REFERENCES "Postava" ("id");

ALTER TABLE "Postava_Uloha" ADD FOREIGN KEY ("ulohat_id") REFERENCES "Uloha" ("id");

ALTER TABLE "Postava_Schopnost" ADD FOREIGN KEY ("postava_id") REFERENCES "Postava" ("id");

ALTER TABLE "Postava_Schopnost" ADD FOREIGN KEY ("schopnost_id") REFERENCES "Schopnost" ("id");

ALTER TABLE "Schopnost" ADD FOREIGN KEY ("rola_id") REFERENCES "Rola" ("id");

ALTER TABLE "Schopnost" ADD FOREIGN KEY ("od_level_id") REFERENCES "Level" ("id");

ALTER TABLE "Predmet" ADD FOREIGN KEY ("poloha_id") REFERENCES "Poloha" ("id");
