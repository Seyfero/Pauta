CREATE TABLE IF NOT EXISTS vt_pauta (
	vt_pauta_id SERIAL PRIMARY KEY,
	vt_pauta_nome varchar(100) NOT NULL,
	vt_pauta_criacao timestamp NOT NULL,
	vt_pauta_duracao int4 NOT NULL,
	vt_pauta_votos_total int4 NOT NULL
);
CREATE INDEX vt_pauta_vt_pauta_nome_idx ON vt_pauta USING btree (vt_pauta_nome);

CREATE TABLE IF NOT EXISTS vt_voto (
	vt_voto_id SERIAL PRIMARY KEY,
	vt_voto_escolha varchar(3) NOT NULL,
	vt_usuario_cpf varchar(11) NOT NULL,
	vt_pauta_id int8 NOT NULL,
	CONSTRAINT vt_voto_vt_pauta_id_fkey FOREIGN KEY (vt_pauta_id) REFERENCES vt_pauta(vt_pauta_id) ON DELETE CASCADE
);
CREATE INDEX vt_voto_vt_pauta_id_idx ON vt_voto USING btree (vt_pauta_id);
CREATE INDEX vt_voto_vt_usuario_cpf_idx ON vt_voto USING btree (vt_usuario_cpf);

