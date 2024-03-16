CREATE TABLE IF NOT EXISTS pedido
(
    id                       UUID NOT NULL PRIMARY KEY,
    valor_total              DOUBLE PRECISION NOT NULL,
    valor_total_servicos     DOUBLE PRECISION NOT NULL,
    valor_total_produtos     DOUBLE PRECISION NOT NULL,
    valor_original_produtos  DOUBLE PRECISION NOT NULL,
    prc_desconto_produtos    DOUBLE PRECISION NOT NULL,
    aberto                   BOOLEAN DEFAULT TRUE NOT NULL,
    created_at               TIMESTAMP NOT NULL,
    updated_at               TIMESTAMP
);

CREATE TABLE IF NOT EXISTS item_pedido
(
    id                  UUID NOT NULL PRIMARY KEY,
    pedido_id           UUID NOT NULL,
    produto_servico_id  UUID NOT NULL,
    quantidade          DOUBLE PRECISION NOT NULL,
    valor_unitario      DOUBLE PRECISION NOT NULL,
    valor_total         DOUBLE PRECISION NOT NULL,
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP,
    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_produto_servico FOREIGN KEY (produto_servico_id) REFERENCES produto_servico(id),
    CONSTRAINT uc_pedido_produto UNIQUE (pedido_id, produto_servico_id)
);