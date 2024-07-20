package br.com.fiap.economed.controller;

import br.com.fiap.economed.dto.historicoSaudeCliente.DetalhesHistoricoSaudeClienteDTO;
import br.com.fiap.economed.repository.HistoricoSaudeClienteRepository;
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
@RequestMapping("/historico-saude")
public class HistoricoSaudeClienteController {

    @Autowired
    private HistoricoSaudeClienteRepository historicoSaudeClienteRepository;

    @GetMapping
    public ResponseEntity<Page<DetalhesHistoricoSaudeClienteDTO>> pesquisar(Pageable pageable) {
        log.info("Listando histórico de saúde");
        var page = historicoSaudeClienteRepository.findAll(pageable).map(DetalhesHistoricoSaudeClienteDTO::new);
        log.info("Listagem de histórico de saúde finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesHistoricoSaudeClienteDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando histórico de saúde com ID: {}", id);
        var endereco = new DetalhesHistoricoSaudeClienteDTO(historicoSaudeClienteRepository.getReferenceById(id));
        log.info("Histórico de saúde encontrado: {}", endereco);
        return ResponseEntity.ok(endereco);
    }
}
