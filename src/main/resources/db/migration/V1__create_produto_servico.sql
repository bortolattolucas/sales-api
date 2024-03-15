CREATE TABLE IF NOT EXISTS produto_servico
(
    id        UUID NOT NULL PRIMARY KEY,
    nome      TEXT NOT NULL,
    preco     DOUBLE PRECISION NOT NULL,
    descricao TEXT,
    tipo      TEXT NOT NULL,
    ativo     BOOLEAN DEFAULT TRUE NOT NULL
);