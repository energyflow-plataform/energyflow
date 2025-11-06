-- ==============================================
-- 1. View: vw_unidades_com_localizacao
-- ==============================================
CREATE OR REPLACE VIEW vw_unidades_com_localizacao AS
SELECT 
    u.id AS id_unidade,
    u.nome AS nome_unidade,
    c.cep,
    c.logradouro,
    b.nome AS bairro,
    ci.nome AS cidade,
    e.nome AS estado,
    e.uf AS uf_estado
FROM unidade u
JOIN cep c ON u.id_cep = c.id
JOIN bairro b ON c.id_bairro = b.id
JOIN cidade ci ON b.id_cidade = ci.id
JOIN estado e ON ci.id_estado = e.id;

-- ==============================================
-- 2. View: vw_usuarios_unidades
-- ==============================================
CREATE OR REPLACE VIEW vw_usuarios_unidades AS
SELECT 
    uu.id AS id_vinculo,
    u.id AS id_usuario,
    CONCAT(u.nome, ' ', u.sobrenome) AS nome_completo,
    u.email,
    u.tipo,
    un.id AS id_unidade,
    un.nome AS nome_unidade,
    uu.situacao,
    uu.dt_vinculo
FROM usuario_unidade uu
JOIN usuario u ON uu.id_usuario = u.id
JOIN unidade un ON uu.id_unidade = un.id;

-- ==============================================
-- 3. View: vw_ambientes_dispositivos
-- ==============================================
CREATE OR REPLACE VIEW vw_ambientes_dispositivos AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    d.potencia,
    d.situacao,
    a.id AS id_ambiente,
    a.nome AS nome_ambiente,
    un.id AS id_unidade,
    un.nome AS nome_unidade
FROM dispositivo d
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id;

-- ==============================================
-- 4. View: vw_consumo_dispositivos
-- ==============================================
CREATE OR REPLACE VIEW vw_consumo_dispositivos AS
SELECT 
    c.id AS id_consumo,
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    a.nome AS nome_ambiente,
    un.nome AS nome_unidade,
    c.inicio_intervalo,
    c.fim_intervalo,
    c.consumo_min,
    c.consumo_max,
    c.consumo_medio,
    ROUND((c.consumo_max + c.consumo_min)/2, 2) AS consumo_estimado
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id;

-- ==============================================
-- 5. View: vw_relatorio_consumo_unidade
-- ==============================================
CREATE OR REPLACE VIEW vw_relatorio_consumo_unidade AS
SELECT 
    un.id AS id_unidade,
    un.nome AS nome_unidade,
    ROUND(AVG(c.consumo_medio), 2) AS media_consumo,
    ROUND(SUM(c.consumo_medio), 2) AS total_consumo,
    COUNT(DISTINCT d.id) AS qtd_dispositivos
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY un.id, un.nome
;

-- ==============================================
-- 6. View: vw_dispositivos_inativos
-- ==============================================
CREATE OR REPLACE VIEW vw_dispositivos_inativos AS
SELECT 
    d.id,
    d.nome,
    d.tipo,
    d.potencia,
    a.nome AS ambiente,
    un.nome AS unidade
FROM dispositivo d
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
WHERE d.situacao = 'inativo';

-- ==============================================
-- 7. View: vw_ranking_dispositivos_consumo
-- ==============================================
CREATE OR REPLACE VIEW vw_ranking_dispositivos_consumo AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    un.nome AS nome_unidade,
    a.nome AS nome_ambiente,
    ROUND(AVG(c.consumo_medio), 2) AS media_consumo,
    ROUND(SUM(c.consumo_medio), 2) AS total_consumo
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY d.id, d.nome, d.tipo, un.nome, a.nome
;

-- ==============================================
-- 8. View: vw_consumo_por_unidade
-- ==============================================
CREATE OR REPLACE VIEW vw_consumo_por_unidade AS
SELECT 
    un.id AS id_unidade,
    un.nome AS nome_unidade,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio_unidade,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total_unidade,
    COUNT(DISTINCT d.id) AS total_dispositivos
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY un.id, un.nome
;


-- ======================================================
-- 9. VIEW: vw_dispositivos_ativados
-- ======================================================
-- Exibe todos os dispositivos com status 'Ativo', incluindo informações sobre o ambiente e unidade.
-- -----------------------------------------------------
CREATE VIEW vw_dispositivos_ativados AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    d.potencia,
    d.status,
    a.nome AS nome_ambiente,
    un.nome AS nome_unidade,
    un.id AS id_unidade
FROM dispositivo d
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
WHERE d.status = 'Ativo';

-- Consulta de exemplo
SELECT * FROM vw_dispositivos_ativados ORDER BY 1;
-- ==============================================
-- 10. View: vw_tempo_medio_uso_dispositivo
-- ==============================================
CREATE OR REPLACE VIEW vw_tempo_medio_uso_dispositivo AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    un.nome AS unidade,
    AVG(TIMESTAMPDIFF(HOUR, c.inicio_intervalo, c.fim_intervalo)) AS tempo_medio_horas
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY d.id, d.nome, d.tipo, un.nome
;

-- ==============================================
-- 11. View: vw_dispositivos_criticos
-- ==============================================
CREATE OR REPLACE VIEW vw_dispositivos_criticos AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    d.potencia,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio,
    un.nome AS unidade,
    a.nome AS ambiente
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
WHERE d.situacao = 'ativo'
GROUP BY d.id, d.nome, d.tipo, d.potencia, un.nome, a.nome
HAVING AVG(c.consumo_medio) > 100
;

-- ==============================================
-- 12. View: vw_eficiencia_dispositivos
-- ==============================================
CREATE OR REPLACE VIEW vw_eficiencia_dispositivos AS
SELECT 
    d.id AS id_dispositivo,
    d.nome AS nome_dispositivo,
    d.tipo,
    d.potencia,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio,
    ROUND(AVG(c.consumo_medio) / d.potencia, 3) AS eficiencia_energetica,
    CASE
        WHEN (AVG(c.consumo_medio) / d.potencia) < 0.5 THEN 'Alta Eficiência'
        WHEN (AVG(c.consumo_medio) / d.potencia) BETWEEN 0.5 AND 1 THEN 'Moderada'
        ELSE 'Baixa Eficiência'
    END AS classificacao
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
GROUP BY d.id, d.nome, d.tipo, d.potencia
;

-- ==============================================
-- 13. View: vw_resumo_diario_consumo
-- ==============================================
CREATE OR REPLACE VIEW vw_resumo_diario_consumo AS
SELECT 
    DATE(c.inicio_intervalo) AS data,
    un.nome AS unidade,
    ROUND(SUM(c.consumo_medio), 2) AS consumo_total_dia,
    ROUND(AVG(c.consumo_medio), 2) AS consumo_medio_dia
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY DATE(c.inicio_intervalo), un.nome
;

-- ==============================================
-- 14. View: vw_top_unidades_eficientes
-- ==============================================
CREATE OR REPLACE VIEW vw_top_unidades_eficientes AS
SELECT 
    un.id AS id_unidade,
    un.nome AS nome_unidade,
    ROUND(AVG(c.consumo_medio / d.potencia), 3) AS eficiencia_media
FROM consumo c
JOIN dispositivo d ON c.id_dispositivo = d.id
JOIN ambiente a ON d.id_ambiente = a.id
JOIN unidade un ON a.id_unidade = un.id
GROUP BY un.id, un.nome
;