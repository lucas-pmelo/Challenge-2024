package br.com.fiap.economed.controller;

import br.com.fiap.economed.dto.enderecoCliente.DetalhesEnderecoClienteDTO;
import br.com.fiap.economed.repository.EnderecoClienteRepository;
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
@RequestMapping("/endereco-cliente")
public class EnderecoClienteController {

    @Autowired
    private EnderecoClienteRepository enderecoClienteRepository;

    @GetMapping
    public ResponseEntity<Page<DetalhesEnderecoClienteDTO>> pesquisar(Pageable pageable) {
        log.info("Listando endereços de clientes");
        var page = enderecoClienteRepository.findAll(pageable).map(DetalhesEnderecoClienteDTO::new);
        log.info("Listagem de endereços de clientes finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesEnderecoClienteDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando endereço de cliente com ID: {}", id);
        var endereco = new DetalhesEnderecoClienteDTO(enderecoClienteRepository.getReferenceById(id));
        log.info("Endereço de cliente encontrado: {}", endereco);
        return ResponseEntity.ok(endereco);
    }
}
