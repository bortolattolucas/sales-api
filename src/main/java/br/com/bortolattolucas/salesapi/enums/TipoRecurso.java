package br.com.bortolattolucas.salesapi.enums;

import lombok.Getter;

@Getter
public enum TipoRecurso {
    PRODUTO("PRODUTO"),
    SERVICO("SERVICO");

    private final String descricao;

    TipoRecurso(String descricao) {
        this.descricao = descricao;
    }

    public static TipoRecurso fromDescricao(String descricao) {
        for (TipoRecurso tipo : TipoRecurso.values()) {
            if (tipo.getDescricao().equals(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de recurso inválido");
    }
}
