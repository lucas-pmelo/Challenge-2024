package br.com.fiap.economed.controller;

import br.com.fiap.economed.dto.enderecoUnidade.DetalhesEnderecoUnidadeDTO;
import br.com.fiap.economed.repository.EnderecoUnidadeRepository;
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
@RequestMapping("/endereco-unidade")
public class EnderecoUnidadeController {

    @Autowired
    private EnderecoUnidadeRepository enderecoUnidadeRepository;

    @GetMapping
    public ResponseEntity<Page<DetalhesEnderecoUnidadeDTO>> pesquisar(Pageable pageable) {
        log.info("Listando endereços de unidades");
        var page = enderecoUnidadeRepository.findAll(pageable).map(DetalhesEnderecoUnidadeDTO::new);
        log.info("Listagem de endereços de unidades finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesEnderecoUnidadeDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando endereço de unidade com ID: {}", id);
        var endereco = new DetalhesEnderecoUnidadeDTO(enderecoUnidadeRepository.getReferenceById(id));
        log.info("Endereço de unidade encontrado: {}", endereco);
        return ResponseEntity.ok(endereco);
    }
}
