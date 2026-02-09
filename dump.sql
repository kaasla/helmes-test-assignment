--
-- PostgreSQL database dump
--

-- Dumped from database version 14.17 (Homebrew)
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- *not* creating schema, since initdb creates it


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: sectors; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sectors (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    parent_id bigint
);


--
-- Name: user_selection_sectors; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_selection_sectors (
    user_selection_id bigint NOT NULL,
    sector_id bigint NOT NULL
);


--
-- Name: user_selections; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_selections (
    id bigint NOT NULL,
    session_id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    agree_to_terms boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: user_selections_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.user_selections ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_selections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: sectors; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.sectors (id, name, parent_id) FROM stdin;
1	Manufacturing	\N
3	Other	\N
2	Service	\N
19	Construction materials	1
18	Electronics and Optics	1
6	Food and Beverage	1
13	Furniture	1
12	Machinery	1
11	Metalworking	1
9	Plastic and Rubber	1
5	Printing	1
7	Textile and Clothing	1
8	Wood	1
342	Bakery & confectionery products	6
43	Beverages	6
42	Fish & fish products	6
40	Meat & meat products	6
39	Milk & dairy products	6
437	Other	6
378	Sweets & snack food	6
389	Bathroom/sauna	13
385	Bedroom	13
390	Children's room	13
98	Kitchen	13
101	Living room	13
392	Office	13
394	Other (Furniture)	13
341	Outdoor	13
99	Project furniture	13
94	Machinery components	12
91	Machinery equipment/tools	12
224	Manufacture of machinery	12
97	Maritime	12
93	Metal structures	12
508	Other	12
227	Repair and maintenance service	12
271	Aluminium and steel workboats	97
269	Boat/Yacht building	97
230	Ship repair and conversion	97
67	Construction of metal structures	11
263	Houses and buildings	11
267	Metal products	11
542	Metal works	11
75	CNC-machining	542
62	Forgings, Fasteners	542
69	Gas, Plasma, Laser cutting	542
66	MIG, TIG, Aluminum welding	542
54	Packaging	9
556	Plastic goods	9
559	Plastic processing technology	9
560	Plastic profiles	9
55	Blowing	559
57	Moulding	559
53	Plastics welding and processing	559
148	Advertising	5
150	Book/Periodicals printing	5
145	Labelling and packaging printing	5
44	Clothing	7
45	Textile	7
337	Other (Wood)	8
51	Wooden building materials	8
47	Wooden houses	8
37	Creative industries	3
29	Energy technology	3
33	Environment	3
25	Business services	2
35	Engineering	2
28	Information Technology and Telecommunications	2
22	Tourism	2
141	Translation services	2
21	Transport and Logistics	2
581	Data processing, Web portals, E-marketing	28
576	Programming, Consultancy	28
121	Software, Hardware	28
122	Telecommunications	28
111	Air	21
114	Rail	21
112	Road	21
113	Water	21
\.


--
-- Name: user_selections_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_selections_id_seq', 1, true);


--
-- Name: sectors sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sectors
    ADD CONSTRAINT sectors_pkey PRIMARY KEY (id);


--
-- Name: user_selection_sectors user_selection_sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_selection_sectors
    ADD CONSTRAINT user_selection_sectors_pkey PRIMARY KEY (user_selection_id, sector_id);


--
-- Name: user_selections user_selections_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_selections
    ADD CONSTRAINT user_selections_pkey PRIMARY KEY (id);


--
-- Name: idx_sectors_parent_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sectors_parent_id ON public.sectors USING btree (parent_id);


--
-- Name: idx_user_selection_sectors_sector_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_selection_sectors_sector_id ON public.user_selection_sectors USING btree (sector_id);


--
-- Name: idx_user_selections_session_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX idx_user_selections_session_id ON public.user_selections USING btree (session_id);


--
-- Name: sectors sectors_parent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sectors
    ADD CONSTRAINT sectors_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES public.sectors(id) ON DELETE CASCADE;


--
-- Name: user_selection_sectors user_selection_sectors_sector_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_selection_sectors
    ADD CONSTRAINT user_selection_sectors_sector_id_fkey FOREIGN KEY (sector_id) REFERENCES public.sectors(id) ON DELETE CASCADE;


--
-- Name: user_selection_sectors user_selection_sectors_user_selection_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_selection_sectors
    ADD CONSTRAINT user_selection_sectors_user_selection_id_fkey FOREIGN KEY (user_selection_id) REFERENCES public.user_selections(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

