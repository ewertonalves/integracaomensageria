package com.integracao.integracaomensageria.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.integracao.integracaomensageria.model.Distritos;
import com.integracao.integracaomensageria.model.Distritos.Municipio;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DistritosSpClientTest {

	@Autowired
	private DistritosSpClient distritosSpClient;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void testGetDistritos_deveRertornarListaDeDistritos() throws Exception {
		List<Distritos> mockDistritos = getMockDistritos();

		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Distritos[].class)))
				.thenReturn(mockDistritos.toArray(new Distritos[0]));

		List<Distritos> distritos = distritosSpClient.getDistritos();

		assertNotNull(distritos);
		assertTrue(distritos.size() > 0);
	}

	@Test
	public void testGetDistritos_deveLancarExcecaoQuandoApiRetornarErro() throws Exception {
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Distritos[].class)))
				.thenThrow(new RestClientException("Erro simulado: Falha ao chamar a API"));

		try {
			distritosSpClient.getDistritos();
			fail("Deveria ter lançado uma RestClientException");
		} catch (RestClientException ex) {
			assertEquals("Erro simulado: Falha ao chamar a API", ex.getMessage());
		}
		Mockito.verify(restTemplate).getForObject(Mockito.anyString(), Mockito.eq(Distritos[].class));
	}

	@Test
	public void testGetDistritos_deveRetornarDistritosComCamposCorretos() throws Exception {
		List<Distritos> mockDistritos = getMockDistritos();

		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Distritos[].class)))
				.thenReturn(mockDistritos.toArray(new Distritos[0]));

		List<Distritos> distritos = distritosSpClient.getDistritos();

		assertEquals(1, distritos.size());
		assertEquals("Vila Mariana", distritos.get(0).getNome());
		assertEquals("São Paulo", distritos.get(0).getMunicipio().getNome());
	}

	private List<Distritos> getMockDistritos() {
		List<Distritos> mockDistritos = new ArrayList<>();

		Distritos distrito1 = new Distritos();
		distrito1.setId(1L);
		distrito1.setNome("Vila Mariana");

		Municipio municipio1 = new Distritos.Municipio();
		municipio1.setId(2L);
		municipio1.setNome("São Paulo");
		distrito1.setMunicipio(municipio1);

		Distritos distrito2 = new Distritos();
		distrito2.setId(3L);
		distrito2.setNome("Pinheiros");

		Municipio municipio2 = new Distritos.Municipio();
		municipio2.setId(4L);
		municipio2.setNome("São Paulo");
		distrito2.setMunicipio(municipio2);

		mockDistritos.add(distrito1);
		mockDistritos.add(distrito2);

		return mockDistritos;
	}

}
