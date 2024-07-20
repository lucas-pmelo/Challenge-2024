package br.com.fiap.economed.controller;

import br.com.fiap.economed.dto.historicoHospitalCliente.DetalhesHistoricoHospitalClienteDTO;
import br.com.fiap.economed.repository.HistoricoHospitalClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/historico-hospital")
public class HistoricoHospitalClienteController {

    @Autowired
    private HistoricoHospitalClienteRepository historicoHospitalClienteRepository;

    @GetMapping
    public ResponseEntity<Page<DetalhesHistoricoHospitalClienteDTO>> pesquisar(Pageable pageable) {
        log.info("Listando histórico de hospitais");
        var page = historicoHospitalClienteRepository.findAll(pageable).map(DetalhesHistoricoHospitalClienteDTO::new);
        log.info("Listagem de histórico de hospitais finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesHistoricoHospitalClienteDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando histórico de hospital com ID: {}", id);
        var endereco = new DetalhesHistoricoHospitalClienteDTO(historicoHospitalClienteRepository.getReferenceById(id));
        log.info("Histórico de hospital encontrado: {}", endereco);
        return ResponseEntity.ok(endereco);
    }
}
