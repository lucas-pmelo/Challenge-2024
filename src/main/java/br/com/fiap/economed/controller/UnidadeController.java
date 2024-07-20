package br.com.fiap.economed.controller;

import br.com.fiap.economed.service.UnidadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.economed.dto.unidade.AtualizacaoUnidadeDTO;
import br.com.fiap.economed.dto.unidade.CadastroUnidadeDTO;
import br.com.fiap.economed.dto.unidade.DetalhesUnidadeDTO;
import br.com.fiap.economed.model.Unidade;

@Slf4j
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesUnidadeDTO>> listar(Pageable paginacao) {
        log.info("Iniciando listagem de unidades");
        var paginaUnidades = unidadeService.listarUnidades(paginacao);
        log.info("Listagem de unidades finalizada com o seguinte resultado: {}", paginaUnidades);
        return ResponseEntity.ok(paginaUnidades);
    }

    @GetMapping("/{unidadeId}")
    public ResponseEntity<DetalhesUnidadeDTO> buscar(@PathVariable("unidadeId") Long unidadeId)
            throws EntityNotFoundException {
        log.info("Buscando unidade com ID: {}", unidadeId);
        var unidade = unidadeService.buscarUnidade(unidadeId);
        log.info("Unidade encontrada: {}", unidade);
        return ResponseEntity.ok(unidade);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesUnidadeDTO> cadastrar(@RequestBody CadastroUnidadeDTO unidadeDTO,
            UriComponentsBuilder uriBuilder) {
        log.info("Cadastrando unidade: {}", unidadeDTO);
        var unidade = unidadeService.cadastrarUnidade(unidadeDTO);

        var uri = uriBuilder.path("/unidades/{id}").buildAndExpand(unidade.getId()).toUri();
        log.info("Unidade cadastrada com sucesso: {}", unidade);
        return ResponseEntity.created(uri).body(new DetalhesUnidadeDTO(unidade));
    }

    @PutMapping("/{unidadeId}")
    @Transactional
    public ResponseEntity<Unidade> atualizar(@PathVariable("unidadeId") Long unidadeId,
            @RequestBody AtualizacaoUnidadeDTO unidadeDTO) throws EntityNotFoundException {
        log.info("Atualizando unidade com ID: {}", unidadeId);
        var unidade = unidadeService.atualizarUnidade(unidadeId, unidadeDTO);
        log.info("Unidade atualizada: {}", unidade);
        return ResponseEntity.ok(unidade);
    }

    @DeleteMapping("/{unidadeId}")
    @Transactional
    public ResponseEntity<Void> remover(@PathVariable("unidadeId") Long unidadeId) throws EntityNotFoundException {
        log.info("Removendo unidade com ID: {}", unidadeId);
        unidadeService.removerUnidade(unidadeId);
        log.info("Unidade removida com sucesso: {}", unidadeId);
        return ResponseEntity.noContent().build();
    }
}
