CREATE SCHEMA IF NOT EXISTS hiking
  AUTHORIZATION postgres;


-- Table: hiking.trail

CREATE TABLE IF NOT EXISTS hiking.trail
(
  trail_id   uuid NOT NULL,
  end_time   time without time zone,
  max_age    integer,
  min_age    integer,
  name       character varying(255) COLLATE pg_catalog."default",
  price      double precision,
  start_time time without time zone,
  CONSTRAINT trail_pkey PRIMARY KEY (trail_id)
) TABLESPACE pg_default;

ALTER TABLE hiking.trail
  OWNER to postgres;

-- Table: hiking.hiker

CREATE TABLE IF NOT EXISTS hiking.hiker
(
  hiker_id uuid NOT NULL,
  age      integer,
  mail     character varying(255) COLLATE pg_catalog."default",
  name     character varying(255) COLLATE pg_catalog."default",
  surname  character varying(255) COLLATE pg_catalog."default",
  CONSTRAINT hiker_pkey PRIMARY KEY (hiker_id)
) TABLESPACE pg_default;

ALTER TABLE hiking.hiker
  OWNER to postgres;


-- Table: hiking.booking

CREATE TABLE IF NOT EXISTS hiking.booking
(
  booking_id           uuid NOT NULL,
  booking_date         date,
  reserved_by_hiker_id uuid,
  trail_id             uuid,
  CONSTRAINT booking_pkey PRIMARY KEY (booking_id),
  CONSTRAINT fkqc275ha8wkxqi4bc5ueekx8pu FOREIGN KEY (trail_id)
    REFERENCES hiking.trail (trail_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
) TABLESPACE pg_default;

ALTER TABLE hiking.booking
  OWNER to postgres;

CREATE TABLE IF NOT EXISTS hiking.booking_hiker
(
  booking_id UUID NOT NULL,
  hiker_id   UUID NOT NULL,
  CONSTRAINT fkqc275ha8wkxqi4bc5uhhkx8pu FOREIGN KEY (hiker_id)
    REFERENCES hiking.hiker (hiker_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
  CONSTRAINT fkqc275ha8mmxqi4bc5uhhkx8pu FOREIGN KEY (booking_id)
    REFERENCES hiking.booking (booking_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION

) TABLESPACE pg_default;

ALTER TABLE hiking.booking_hiker
  OWNER to postgres;

INSERT INTO hiking.trail
  (trail_id, name, price, min_age, max_age, start_time, end_time)
VALUES ('ac43d61a-9a62-4151-9448-f4b09dba6b54',
        'Shire',
        29.90,
        5,
        100,
        time '07:00',
        time '09:00')
ON CONFLICT (trail_id) DO NOTHING;


INSERT INTO hiking.trail
  (trail_id, name, price, min_age, max_age, start_time, end_time)
VALUES ('c820b2b3-f10a-4ab9-86c0-e32362e2cc1d',
        'Gondor',
        59.90,
        11,
        50,
        time '10:00',
        time '13:00')
ON CONFLICT (trail_id) DO NOTHING;

INSERT INTO hiking.trail
  (trail_id, name, price, min_age, max_age, start_time, end_time)
VALUES ('e231de08-d21b-41e3-8ba9-6195beb8676f',
        'Mordor',
        99.90,
        18,
        40,
        time '14:00',
        time '19:00')
ON CONFLICT (trail_id) DO NOTHING;