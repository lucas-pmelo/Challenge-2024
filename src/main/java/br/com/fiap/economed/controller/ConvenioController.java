package br.com.fiap.economed.controller;

import br.com.fiap.economed.service.ConvenioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.economed.dto.convenio.AtualizacaoConvenioDTO;
import br.com.fiap.economed.dto.convenio.CadastroConvenioDTO;
import br.com.fiap.economed.dto.convenio.DetalhesConvenioDTO;
import br.com.fiap.economed.model.Convenio;

@Slf4j
@RestController
@RequestMapping("/convenios")
public class ConvenioController {

    private final ConvenioService convenioService;

    public ConvenioController(ConvenioService convenioService) {
        this.convenioService = convenioService;
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesConvenioDTO>> list(Pageable paginacao) {
        log.info("Iniciando listagem de convenios");
        var paginaConvenios = convenioService.listarConvenio(paginacao);
        log.info("Listagem de convenios finalizada com o seguinte resultado: {}", paginaConvenios);
        return ResponseEntity.ok(paginaConvenios);
    }

    @GetMapping("/{convenioId}")
    public ResponseEntity<DetalhesConvenioDTO> search(@PathVariable Long convenioId)
            throws EntityNotFoundException {
        log.info("Buscando convenio com ID: {}", convenioId);
        var convenio = convenioService.buscarConvenio(convenioId);
        log.info("Convenio encontrado: {}", convenio);
        return ResponseEntity.ok(convenio);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesConvenioDTO> create(@RequestBody CadastroConvenioDTO convenioDTO,
            UriComponentsBuilder uriBuilder) {
        log.info("Cadastrando convenio: {}", convenioDTO);
        var convenio = convenioService.cadastrarConvenio(convenioDTO);

        var uri = uriBuilder.path("/convenios/{id}").buildAndExpand(convenio.getId()).toUri();
        log.info("Convenio cadastrado com sucesso: {}", convenio);
        return ResponseEntity.created(uri).body(new DetalhesConvenioDTO(convenio));
    }

    @PutMapping("/{convenioId}")
    @Transactional
    public ResponseEntity<Convenio> update(@PathVariable("convenioId") Long convenioId,
            @RequestBody AtualizacaoConvenioDTO convenioDTO) throws EntityNotFoundException {
        log.info("Atualizando convenio com ID: {}", convenioId);
        var convenio = convenioService.atualizarConvenio(convenioId, convenioDTO);
        log.info("Convenio atualizado: {}", convenio);
        return ResponseEntity.ok(convenio);
    }

    @DeleteMapping("/{convenioId}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("convenioId") Long convenioId) throws EntityNotFoundException {
        log.info("Removendo convenio com ID: {}", convenioId);
        convenioService.removerConvenio(convenioId);
        log.info("Convenio removido com sucesso");
        return ResponseEntity.noContent().build();
    }
}