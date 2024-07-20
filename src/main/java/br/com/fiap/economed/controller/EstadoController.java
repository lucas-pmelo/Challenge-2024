package br.com.fiap.economed.controller;

import br.com.fiap.economed.model.Estado;
import br.com.fiap.economed.dto.estado.AtualizacaoEstadoDTO;
import br.com.fiap.economed.dto.estado.CadastroEstadoDTO;
import br.com.fiap.economed.dto.estado.DetalhesEstadoDTO;
import br.com.fiap.economed.repository.EstadoRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesEstadoDTO> cadastrar(@RequestBody CadastroEstadoDTO dto,
            UriComponentsBuilder builder) {
        log.info("Cadastrando estado: {}", dto);
        var estado = new Estado(dto);
        estado = estadoRepository.save(estado);
        var uri = builder.path("/estados/{id}").buildAndExpand(estado.getId()).toUri();
        log.info("Estado cadastrado com sucesso: {}", estado);
        return ResponseEntity.created(uri).body(new DetalhesEstadoDTO(estado));
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesEstadoDTO>> pesquisar(Pageable pageable) {
        log.info("Listando estados");
        var page = estadoRepository.findAll(pageable).map(DetalhesEstadoDTO::new);
        log.info("Listagem de estados finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesEstadoDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando estado com ID: {}", id);
        var estado = new DetalhesEstadoDTO(estadoRepository.getReferenceById(id));
        log.info("Estado encontrado: {}", estado);
        return ResponseEntity.ok(estado);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetalhesEstadoDTO> atualizar(@PathVariable("id") Long id,
            @RequestBody AtualizacaoEstadoDTO dto) {
        log.info("Atualizando estado com ID: {}", id);
        var estado = estadoRepository.getReferenceById(id);
        estado.atualizar(dto);
        log.info("Estado atualizado com sucesso: {}", estado);
        return ResponseEntity.ok(new DetalhesEstadoDTO(estado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        log.info("Removendo estado com ID: {}", id);
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado com ID: " + id));
        estadoRepository.delete(estado);
        log.info("Estado removido com sucesso: {}", estado);
        return ResponseEntity.noContent().build();
    }

}
