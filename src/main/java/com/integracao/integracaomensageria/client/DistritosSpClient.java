package com.integracao.integracaomensageria.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.integracao.integracaomensageria.model.Distritos;

@FeignClient(name = "ibgeClient", url = "https://servicodados.ibge.gov.br/api/v1/localidades")
public interface DistritosSpClient {

	@GetMapping("/distritos?orderBy=nome")
	List<Distritos> getDistritos();

}
