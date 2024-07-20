package br.com.fiap.economed.controller;

import br.com.fiap.economed.service.EmpresaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.economed.dto.empresa.AtualizacaoEmpresaDTO;
import br.com.fiap.economed.dto.empresa.CadastroEmpresaDTO;
import br.com.fiap.economed.dto.empresa.DetalhesEmpresaDTO;
import br.com.fiap.economed.model.Empresa;

@Slf4j
@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    public ResponseEntity<Page<DetalhesEmpresaDTO>> list(Pageable paginacao) {
        log.info("Iniciando listagem de empresas");
        Page<DetalhesEmpresaDTO> paginaEmpresas = empresaService.listarEmpresas(paginacao);
        return ResponseEntity.ok(paginaEmpresas);
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<DetalhesEmpresaDTO> find(@PathVariable Long empresaId) {
        log.info("Buscando empresa com ID: {}", empresaId);
        DetalhesEmpresaDTO empresa = empresaService.buscarEmpresa(empresaId);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DetalhesEmpresaDTO> create(@RequestBody CadastroEmpresaDTO empresaDTO,
            UriComponentsBuilder uriBuilder) {
        log.info("Cadastrando empresa: {}", empresaDTO);
        Empresa empresa = empresaService.cadastrarEmpresa(empresaDTO);

        var uri = uriBuilder.path("/empresas/{empresaId}").buildAndExpand(empresa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalhesEmpresaDTO(empresa));
    }

    @PutMapping("/{empresaId}")
    @Transactional
    public ResponseEntity<DetalhesEmpresaDTO> update(@PathVariable Long empresaId,
                                                     @Valid @RequestBody AtualizacaoEmpresaDTO empresaDTO) {
        log.info("Atualizando empresa com ID: {}", empresaId);
        Empresa empresa = empresaService.atualizarEmpresa(empresaId, empresaDTO);
        return ResponseEntity.ok(new DetalhesEmpresaDTO(empresa));
    }

    @DeleteMapping("/{empresaId}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long empresaId) {
        log.info("Removendo empresa com ID: {}", empresaId);
        empresaService.removerEmpresa(empresaId);
        return ResponseEntity.noContent().build();
    }
}
