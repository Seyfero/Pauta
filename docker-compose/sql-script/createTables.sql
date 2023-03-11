CREATE SCHEMA IF NOT EXISTS controle_pautas AUTHORIZATION postgres;
-- controle_pautas.vt_pauta definition

-- Drop table

-- DROP TABLE controle_pautas.vt_pauta;

CREATE TABLE IF NOT EXISTS controle_pautas.vt_pauta (
	vt_pauta_id SERIAL PRIMARY KEY,
	vt_pauta_nome varchar(100) NOT NULL,
	vt_pauta_criacao TIMESTAMPTZ NOT NULL DEFAULT timezone('America/Sao_Paulo', NOW()),
	vt_pauta_duracao int4 NOT NULL,
	vt_pauta_processada boolean NOT NULL
);
CREATE INDEX vt_pauta_vt_pauta_nome_idx ON controle_pautas.vt_pauta USING btree (vt_pauta_nome);

-- Permissions

ALTER TABLE controle_pautas.vt_pauta OWNER TO postgres;
GRANT ALL ON TABLE controle_pautas.vt_pauta TO postgres;


-- controle_pautas.vt_voto definition

-- Drop table

-- DROP TABLE controle_pautas.vt_voto;

CREATE TABLE IF NOT EXISTS controle_pautas.vt_voto (
	vt_voto_id SERIAL PRIMARY KEY,
	vt_voto_escolha varchar(3) NOT NULL,
	vt_usuario_cpf varchar(11) NOT NULL,
	vt_pauta_id int8 NOT NULL,
	CONSTRAINT vt_voto_vt_pauta_id_fkey FOREIGN KEY (vt_pauta_id) REFERENCES controle_pautas.vt_pauta(vt_pauta_id) ON DELETE CASCADE
);
CREATE INDEX vt_voto_vt_pauta_id_idx ON controle_pautas.vt_voto USING btree (vt_pauta_id);
CREATE INDEX vt_voto_vt_usuario_cpf_idx ON controle_pautas.vt_voto USING btree (vt_usuario_cpf);

-- Permissions

ALTER TABLE controle_pautas.vt_voto OWNER TO postgres;
GRANT ALL ON TABLE controle_pautas.vt_voto TO postgres;




-- Permissions

GRANT ALL ON SCHEMA controle_pautas TO postgres;
GRANT ALL ON SCHEMA controle_pautas TO public;

