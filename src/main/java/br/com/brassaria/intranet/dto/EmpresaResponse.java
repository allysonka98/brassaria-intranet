package br.com.brassaria.intranet.dto;

import br.com.brassaria.intranet.model.Empresa;

import java.time.Year;
import java.util.List;

public record EmpresaResponse(
        Long id,
        String nome,
        Integer anoFundacao,
        int anosDeHistoria,
        String slogan,
        String historia,
        String missao,
        String visao,
        List<ValorDto> valores) {

    public static EmpresaResponse de(Empresa e) {
        int anos = Year.now().getValue() - e.getAnoFundacao();
        List<ValorDto> valores = e.getValores().stream().map(ValorDto::de).toList();
        return new EmpresaResponse(e.getId(), e.getNome(), e.getAnoFundacao(), anos,
                e.getSlogan(), e.getHistoria(), e.getMissao(), e.getVisao(), valores);
    }
}
