--
-- PostgreSQL database dump
--

-- Dumped from database version 10.11
-- Dumped by pg_dump version 10.11

-- Started on 2020-01-16 12:22:09

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 24969)
-- Name: bacheca; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA bacheca;


ALTER SCHEMA bacheca OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 215 (class 1259 OID 24980)
-- Name: allegato; Type: TABLE; Schema: bacheca; Owner: postgres
--

CREATE TABLE bacheca.allegato (
    id integer NOT NULL,
    id_pubblicazione integer NOT NULL,
    nomefile character varying(255) NOT NULL,
    dimensione integer NOT NULL,
    contenuto bytea
);


ALTER TABLE bacheca.allegato OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 24978)
-- Name: allegato_id_seq; Type: SEQUENCE; Schema: bacheca; Owner: postgres
--

CREATE SEQUENCE bacheca.allegato_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bacheca.allegato_id_seq OWNER TO postgres;

--
-- TOC entry 2852 (class 0 OID 0)
-- Dependencies: 214
-- Name: allegato_id_seq; Type: SEQUENCE OWNED BY; Schema: bacheca; Owner: postgres
--

ALTER SEQUENCE bacheca.allegato_id_seq OWNED BY bacheca.allegato.id;


--
-- TOC entry 213 (class 1259 OID 24972)
-- Name: pubblicazione; Type: TABLE; Schema: bacheca; Owner: postgres
--

CREATE TABLE bacheca.pubblicazione (
    id integer NOT NULL,
    tipo character varying(32) NOT NULL,
    numero integer NOT NULL,
    data_pubblicazione date NOT NULL,
    titolo character varying(255) NOT NULL,
    ufficio character varying(32) NOT NULL,
    proprietario character varying(64) NOT NULL,
    nome_documento character varying(255) NOT NULL,
    dimensione integer NOT NULL,
    contenuto_documento bytea NOT NULL,
    mail_status character varying(16) NOT NULL,
    indexing_status character varying(16) NOT NULL
);


ALTER TABLE bacheca.pubblicazione OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 24970)
-- Name: elenco_id_seq; Type: SEQUENCE; Schema: bacheca; Owner: postgres
--

CREATE SEQUENCE bacheca.elenco_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bacheca.elenco_id_seq OWNER TO postgres;

--
-- TOC entry 2853 (class 0 OID 0)
-- Dependencies: 212
-- Name: elenco_id_seq; Type: SEQUENCE OWNED BY; Schema: bacheca; Owner: postgres
--

ALTER SEQUENCE bacheca.elenco_id_seq OWNED BY bacheca.pubblicazione.id;


--
-- TOC entry 2720 (class 2604 OID 24983)
-- Name: allegato id; Type: DEFAULT; Schema: bacheca; Owner: postgres
--

ALTER TABLE ONLY bacheca.allegato ALTER COLUMN id SET DEFAULT nextval('bacheca.allegato_id_seq'::regclass);


--
-- TOC entry 2719 (class 2604 OID 24975)
-- Name: pubblicazione id; Type: DEFAULT; Schema: bacheca; Owner: postgres
--

ALTER TABLE ONLY bacheca.pubblicazione ALTER COLUMN id SET DEFAULT nextval('bacheca.elenco_id_seq'::regclass);


--
-- TOC entry 2724 (class 2606 OID 24989)
-- Name: allegato allegato_pkey; Type: CONSTRAINT; Schema: bacheca; Owner: postgres
--

ALTER TABLE ONLY bacheca.allegato
    ADD CONSTRAINT allegato_pkey PRIMARY KEY (id);


--
-- TOC entry 2722 (class 2606 OID 24977)
-- Name: pubblicazione elenco_pkey; Type: CONSTRAINT; Schema: bacheca; Owner: postgres
--

ALTER TABLE ONLY bacheca.pubblicazione
    ADD CONSTRAINT elenco_pkey PRIMARY KEY (id);


--
-- TOC entry 2725 (class 2606 OID 24997)
-- Name: allegato ALLEGATO_FK_PUBBLICAZIONE; Type: FK CONSTRAINT; Schema: bacheca; Owner: postgres
--

ALTER TABLE ONLY bacheca.allegato
    ADD CONSTRAINT "ALLEGATO_FK_PUBBLICAZIONE" FOREIGN KEY (id_pubblicazione) REFERENCES bacheca.pubblicazione(id) NOT VALID;


-- Completed on 2020-01-16 12:22:09

--
-- PostgreSQL database dump complete
--

