package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Moto;
import com.mycompany.myapp.repository.MotoRepository;
import com.mycompany.myapp.service.MotoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Moto}.
 */
@RestController
@RequestMapping("/api")
public class MotoResource {

    private final Logger log = LoggerFactory.getLogger(MotoResource.class);

    private static final String ENTITY_NAME = "moto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotoService motoService;

    private final MotoRepository motoRepository;

    public MotoResource(MotoService motoService, MotoRepository motoRepository) {
        this.motoService = motoService;
        this.motoRepository = motoRepository;
    }

    /**
     * {@code POST  /motos} : Create a new moto.
     *
     * @param moto the moto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moto, or with status {@code 400 (Bad Request)} if the moto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motos")
    public ResponseEntity<Moto> createMoto(@Valid @RequestBody Moto moto) throws URISyntaxException {
        log.debug("REST request to save Moto : {}", moto);
        if (moto.getId() != null) {
            throw new BadRequestAlertException("A new moto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Moto result = motoService.save(moto);
        return ResponseEntity
            .created(new URI("/api/motos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motos/:id} : Updates an existing moto.
     *
     * @param id the id of the moto to save.
     * @param moto the moto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moto,
     * or with status {@code 400 (Bad Request)} if the moto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motos/{id}")
    public ResponseEntity<Moto> updateMoto(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Moto moto)
        throws URISyntaxException {
        log.debug("REST request to update Moto : {}, {}", id, moto);
        if (moto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Moto result = motoService.save(moto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /motos/:id} : Partial updates given fields of an existing moto, field will ignore if it is null
     *
     * @param id the id of the moto to save.
     * @param moto the moto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moto,
     * or with status {@code 400 (Bad Request)} if the moto is not valid,
     * or with status {@code 404 (Not Found)} if the moto is not found,
     * or with status {@code 500 (Internal Server Error)} if the moto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/motos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Moto> partialUpdateMoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Moto moto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Moto partially : {}, {}", id, moto);
        if (moto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Moto> result = motoService.partialUpdate(moto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moto.getId().toString())
        );
    }

    /**
     * {@code GET  /motos} : get all the motos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motos in body.
     */
    @GetMapping("/motos")
    public ResponseEntity<List<Moto>> getAllMotos(Pageable pageable) {
        log.debug("REST request to get a page of Motos");
        Page<Moto> page = motoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /motos/:id} : get the "id" moto.
     *
     * @param id the id of the moto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motos/{id}")
    public ResponseEntity<Moto> getMoto(@PathVariable Long id) {
        log.debug("REST request to get Moto : {}", id);
        Optional<Moto> moto = motoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moto);
    }

    /**
     * {@code GET  /coches/:id&:color&:modelo&:marca&:precio&:venta} : get list coches.
     * @param id the id of the coches to retrieve.
     * @param color the color of the coches to retrieve.
     * @param modelo the modelo of the coches to retrieve.
     * @param marca the marca of the coches to retrieve.
     * @param precio the precio of the coches to retrieve.
     * @param venta the venta of the coches to retrieve.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coche, or with status {@code 400 (Bad Request)} if the coche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping(
        value = "/motos/get-motos-by-filter",
        params = { "id", "color", "modelo", "marca", "fechaI", "fechaF", "precioI", "precioF", "venta" }
    )
    public ResponseEntity<List<Moto>> getMotosByFilter(
        @RequestParam MultiValueMap<String, String> queryParams,
        UriComponentsBuilder uriBuilder,
        Pageable pageable,
        @RequestParam(value = "id", defaultValue = "0") String id,
        @RequestParam(value = "color", defaultValue = "") String color,
        @RequestParam(value = "modelo", defaultValue = "") String modelo,
        @RequestParam(value = "marca", defaultValue = "") String marca,
        @RequestParam(value = "fechaI") String fechaI,
        @RequestParam(value = "fechaF") String fechaF,
        @RequestParam(value = "precioI", defaultValue = "0") String precioI,
        @RequestParam(value = "precioF", defaultValue = "0") String precioF,
        @RequestParam(value = "venta", defaultValue = "0") String venta
    ) {
        log.debug("REST request to motos by filter: {}", id, color, modelo, marca, fechaI, fechaF, precioI, precioF, venta);
        final Page<Moto> page = motoService.getMotosByFilter(
            Long.parseLong(id),
            color,
            modelo,
            marca,
            Instant.parse(fechaI),
            Instant.parse(fechaF),
            Double.parseDouble(precioI),
            Double.parseDouble(precioF),
            Long.parseLong(venta),
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /motos/:id} : delete the "id" moto.
     *
     * @param id the id of the moto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motos/{id}")
    public ResponseEntity<Void> deleteMoto(@PathVariable Long id) {
        log.debug("REST request to delete Moto : {}", id);
        motoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
