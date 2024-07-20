package br.com.fiap.economed.controller;

import br.com.fiap.economed.model.Cidade;
import br.com.fiap.economed.dto.cidade.AtualizacaoCidadeDTO;
import br.com.fiap.economed.dto.cidade.CadastroCidadeDTO;
import br.com.fiap.economed.dto.cidade.DetalhesCidadeDTO;
import br.com.fiap.economed.repository.CidadeRepository;
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
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesCidadeDTO> cadastrar(@RequestBody CadastroCidadeDTO dto,
            UriComponentsBuilder builder) {
        log.info("Cadastrando cidade: {}", dto);
        var cidade = new Cidade(dto);
        var estado = estadoRepository.getReferenceById(dto.estadoId());
        cidade.setEstado(estado);
        cidade = cidadeRepository.save(cidade);
        var uri = builder.path("/cidades/{id}").buildAndExpand(cidade.getId()).toUri();
        log.info("Cidade cadastrada com sucesso: {}", cidade);
        return ResponseEntity.created(uri).body(new DetalhesCidadeDTO(cidade));
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesCidadeDTO>> pesquisar(Pageable pageable) {
        log.info("Listando cidades");
        var page = cidadeRepository.findAll(pageable).map(DetalhesCidadeDTO::new);
        log.info("Listagem de cidades finalizada com o seguinte resultado: {}", page);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesCidadeDTO> pesquisar(@PathVariable("id") Long id) {
        log.info("Buscando cidade com ID: {}", id);
        var cidade = new DetalhesCidadeDTO(cidadeRepository.getReferenceById(id));
        log.info("Cidade encontrada: {}", cidade);
        return ResponseEntity.ok(cidade);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DetalhesCidadeDTO> atualizar(@PathVariable("id") Long id,
            @RequestBody AtualizacaoCidadeDTO dto) {
        log.info("Atualizando cidade com ID: {}", id);
        var cidade = cidadeRepository.getReferenceById(id);
        cidade.atualizar(dto);
        log.info("Cidade atualizada com sucesso: {}", cidade);
        return ResponseEntity.ok(new DetalhesCidadeDTO(cidade));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
        Cidade estado = cidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com ID: " + id));
        log.info("Removendo cidade com ID: {}", id);
        cidadeRepository.delete(estado);
        log.info("Cidade removida com sucesso");
        return ResponseEntity.noContent().build();
    }

}
