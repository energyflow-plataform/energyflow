package com.pi.energyflow.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pi.energyflow.model.Endereco;

@Service
public class EnderecoService {

    public Endereco buscarPorCep(String cep) {
        String url = "https://brasilapi.com.br/api/cep/v1/" + cep;
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            Endereco endereco = new Endereco();
            endereco.setCep((String) response.get("cep"));
            endereco.setLogradouro((String) response.get("street"));
            endereco.setBairro((String) response.get("neighborhood"));
            endereco.setCidade((String) response.get("city"));
            endereco.setEstado((String) response.get("state"));

            return endereco;
        } catch (Exception e) {
            return null;
        }
    }
}
