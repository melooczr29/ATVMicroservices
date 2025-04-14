INSERT INTO CAD_CIDADAO (ID, NOME, ENDERECO, BAIRRO) VALUES 
(10001, 'Homer Simpson', 'Evergreen Terrace', 'Centro'),
(10002, 'Marge Simpson', 'Evergreen Terrace', 'Centro');

INSERT INTO USUARIO (CIDADAO_ID, USERNAME, SENHA) VALUES 
(10001, 'homer', 'donuts123'),
(10002, 'marge', 'bagels456');