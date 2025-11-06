-- Criação do banco de dados
CREATE DATABASE energyflow;
USE energyflow;

-- Tabelas para definir endereços
CREATE TABLE estado (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cidade (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    id_estado INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (nome, id_estado),
    FOREIGN KEY (id_estado) REFERENCES estado(id)
);

CREATE TABLE bairro (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    id_cidade INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (nome, id_cidade),
    FOREIGN KEY (id_cidade) REFERENCES cidade(id)
);

CREATE TABLE cep (
    id INT NOT NULL AUTO_INCREMENT,
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    id_bairro INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_bairro) REFERENCES bairro(id)
);

-- Tabela de usuários do sistema
CREATE TABLE usuario (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tipo ENUM('admin', 'comum') DEFAULT 'comum',
    dt_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    ultimo_login DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

-- Tabela de unidades (empresa, casa, prédio, etc.)
CREATE TABLE unidade (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    id_cep INT NOT NULL,
    cod_acesso VARCHAR(100) UNIQUE NOT NULL,
    criado_por INT NULL,
    dt_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    FOREIGN KEY (criado_por) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (id_cep) REFERENCES cep(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Tabela de vínculo entre usuários e unidades
CREATE TABLE usuario_unidade (
    id INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_unidade INT NOT NULL,
    situacao ENUM('pendente', 'aprovado', 'rejeitado') DEFAULT 'pendente',
    dt_vinculo DATETIME DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_unidade) REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Tabela de ambientes dentro de uma unidade
CREATE TABLE ambiente (
    id INT NOT NULL AUTO_INCREMENT,
    id_unidade INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id_unidade) REFERENCES unidade(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Tabela de dispositivos pertencentes a um ambiente
CREATE TABLE dispositivo (
    id INT NOT NULL AUTO_INCREMENT,
    id_ambiente INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(100),
    potencia DECIMAL(10,2) NOT NULL,
    situacao ENUM('ativo', 'inativo') DEFAULT 'inativo',
    PRIMARY KEY (id),
    FOREIGN KEY (id_ambiente) REFERENCES ambiente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Tabela de registros de consumo dos dispositivos
CREATE TABLE consumo (
    id INT NOT NULL AUTO_INCREMENT,
    id_dispositivo INT NOT NULL,
    inicio_intervalo DATETIME NOT NULL,
    fim_intervalo DATETIME NOT NULL,
    consumo_min DECIMAL(10,2) NOT NULL,
    consumo_max DECIMAL(10,2) NOT NULL,
    consumo_medio DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivo(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Inserindo estados
INSERT INTO estado (nome, uf) VALUES
('São Paulo', 'SP'),
('Rio de Janeiro', 'RJ');

-- Inserindo cidades
INSERT INTO cidade (nome, id_estado) VALUES
('Rio Claro', 1),
('Campinas', 1),
('Niterói', 2);

-- Inserindo bairros
INSERT INTO bairro (nome, id_cidade) VALUES
('Centro', 1),
('Santa Cruz', 1),
('Vila Nova', 2),
('Icaraí', 3);

-- Inserindo CEPs
INSERT INTO cep (cep, logradouro, id_bairro) VALUES
('13500-000', 'Rua 1', 1),
('13501-010', 'Rua 2', 2),
('13010-200', 'Rua das Flores', 3),
('24230-000', 'Rua das Palmeiras', 4);

-- Inserindo usuários
INSERT INTO usuario (nome, sobrenome, email, senha, tipo) VALUES
('Ana', 'Silva', 'ana@email.com', 'senha123', 'admin'),
('Carlos', 'Pereira', 'carlos@email.com', 'senha123', 'comum'),
('Juliana', 'Souza', 'juliana@email.com', 'senha123', 'comum');

-- Inserindo unidades (ex: empresas ou casas)
INSERT INTO unidade (nome, id_cep, cod_acesso, criado_por) VALUES
('Empresa SolarTech', 1, 'EMP123', 1),
('Residência Carlos', 2, 'RES456', 2),
('Condomínio Bela Vista', 3, 'CON789', 1);

-- Vínculo dos usuários com as unidades
INSERT INTO usuario_unidade (id_usuario, id_unidade, situacao) VALUES
(1, 1, 'aprovado'),
(2, 2, 'aprovado'),
(3, 3, 'pendente'),
(1, 3, 'aprovado');

-- Inserindo ambientes
INSERT INTO ambiente (id_unidade, nome, descricao) VALUES
(1, 'Sala de Servidores', 'Ambiente crítico com servidores de alto consumo'),
(1, 'Recepção', 'Área administrativa com iluminação e ar condicionado'),
(2, 'Sala de Estar', 'Ambiente residencial principal'),
(3, 'Área Comum', 'Espaço compartilhado do condomínio');

-- Inserindo dispositivos
INSERT INTO dispositivo (id_ambiente, nome, tipo, potencia, situacao) VALUES
(1, 'Servidor Principal', 'Servidor', 800.00, 'ativo'),
(1, 'Ar Condicionado', 'Climatização', 1200.00, 'ativo'),
(2, 'Lâmpada LED', 'Iluminação', 15.00, 'ativo'),
(3, 'TV', 'Eletrônico', 200.00, 'ativo'),
(4, 'Elevador', 'Transporte', 1500.00, 'ativo');

-- Inserindo registros de consumo
INSERT INTO consumo (id_dispositivo, inicio_intervalo, fim_intervalo, consumo_min, consumo_max, consumo_medio) VALUES
(1, '2025-10-14 08:00:00', '2025-10-14 12:00:00', 3.50, 5.20, 4.30),
(2, '2025-10-14 08:00:00', '2025-10-14 12:00:00', 4.00, 6.10, 5.20),
(3, '2025-10-14 18:00:00', '2025-10-14 23:00:00', 0.10, 0.20, 0.15),
(4, '2025-10-14 18:00:00', '2025-10-14 23:00:00', 0.80, 1.50, 1.10),
(5, '2025-10-14 08:00:00', '2025-10-14 12:00:00', 5.50, 7.20, 6.35);

-- Mostra quais ambientes têm o maior consumo médio de energia
SELECT 
    a.nome AS ambiente,
    u.nome AS unidade,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio_total
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade u ON a.id_unidade = u.id
GROUP BY a.id, u.id
ORDER BY consumo_medio_total DESC;

-- Mostra quais unidades consomem mais energia
SELECT 
    u.nome AS unidade,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade u ON a.id_unidade = u.id
GROUP BY u.id
ORDER BY consumo_total DESC;

-- Top 5 dispositivos que mais consomem energia
SELECT 
    d.nome AS dispositivo,
    a.nome AS ambiente,
    u.nome AS unidade,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade u ON a.id_unidade = u.id
GROUP BY d.id
ORDER BY consumo_total DESC
LIMIT 5;

-- Mostra quantos dispositivos ativos existem em cada unidade
SELECT 
    u.nome AS unidade,
    COUNT(d.id) AS dispositivos_ativos
FROM dispositivo d
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade u ON a.id_unidade = u.id
WHERE d.situacao = 'ativo'
GROUP BY u.id;

-- Mostra usuários que ainda aguardam aprovação para acessar uma unidade
SELECT 
    uu.id AS id_vinculo,
    u.nome AS usuario,
    un.nome AS unidade,
    uu.situacao,
    uu.dt_vinculo
FROM usuario_unidade uu
JOIN usuario u ON uu.id_usuario = u.id
JOIN unidade un ON uu.id_unidade = un.id
WHERE uu.situacao = 'pendente';

-- Mostra o impacto de cada tipo de dispositivo no consumo energético
SELECT 
    d.tipo,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
GROUP BY d.tipo
ORDER BY consumo_total DESC;

-- Mostra se o consumo aumentou ou diminuiu de um dia para o outro.
SELECT
  t1.unidade,
  t1.data,
  t1.consumo_total,
  ROUND(t1.consumo_total - IFNULL(t2.consumo_total, 0), 2) AS diferenca_consumo
FROM (
  SELECT
    u.id AS id_unidade,
    u.nome AS unidade,
    DATE(c.inicio_intervalo) AS data,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total
  FROM consumo c
  JOIN dispositivo d ON c.id_dispositivo = d.id
  JOIN ambiente a ON d.id_ambiente = a.id
  JOIN unidade u ON a.id_unidade = u.id
  GROUP BY u.id, DATE(c.inicio_intervalo)
) AS t1
LEFT JOIN (
  SELECT
    u.id AS id_unidade,
    DATE(c.inicio_intervalo) AS data,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total
  FROM consumo c
  JOIN dispositivo d ON c.id_dispositivo = d.id
  JOIN ambiente a ON d.id_ambiente = a.id
  JOIN unidade u ON a.id_unidade = u.id
  GROUP BY u.id, DATE(c.inicio_intervalo)
) AS t2
  ON t1.id_unidade = t2.id_unidade
  AND t2.data = DATE_SUB(t1.data, INTERVAL 1 DAY)
ORDER BY t1.unidade, t1.data;

-- Mostra dispositivos cujo consumo médio está muito acima do normal, comparando com a média geral do mesmo tipo
SELECT 
    d.nome AS dispositivo,
    d.tipo,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_atual,
    (
        SELECT ROUND(AVG(c2.consumo_medio), 2)
        FROM consumo c2
        JOIN dispositivo d2 ON c2.id_dispositivo = d2.id
        WHERE d2.tipo = d.tipo
    ) AS media_tipo,
    CASE 
        WHEN AVG(c.consumo_medio) > (
            SELECT AVG(c2.consumo_medio) * 1.3
            FROM consumo c2
            JOIN dispositivo d2 ON c2.id_dispositivo = d2.id
            WHERE d2.tipo = d.tipo
        )
        THEN '⚠️ Acima do esperado'
        ELSE 'Normal'
    END AS status_consumo
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
GROUP BY d.id, d.tipo;

-- Mostra horários de pico de energia
SELECT 
    HOUR(c.inicio_intervalo) AS hora_inicial,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio_hora
FROM consumo c
GROUP BY HOUR(c.inicio_intervalo)
ORDER BY hora_inicial;

-- Mostra dispositivos ativos sem consumo nas últimas 24h
SELECT 
    d.nome AS dispositivo,
    a.nome AS ambiente,
    u.nome AS unidade,
    MAX(c.fim_intervalo) AS ultimo_registro
FROM dispositivo d
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade u ON a.id_unidade = u.id
LEFT JOIN consumo c ON d.id = c.id_dispositivo
WHERE d.situacao = 'ativo'
GROUP BY d.id
HAVING MAX(c.fim_intervalo) < NOW() - INTERVAL 1 DAY OR MAX(c.fim_intervalo) IS NULL;

