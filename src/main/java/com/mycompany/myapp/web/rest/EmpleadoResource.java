package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Empleado;
import com.mycompany.myapp.repository.EmpleadoRepository;
import com.mycompany.myapp.service.EmpleadoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Empleado}.
 */
@RestController
@RequestMapping("/api")
public class EmpleadoResource {

    private final Logger log = LoggerFactory.getLogger(EmpleadoResource.class);

    private static final String ENTITY_NAME = "empleado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadoService empleadoService;

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoResource(EmpleadoService empleadoService, EmpleadoRepository empleadoRepository) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * {@code POST  /empleados} : Create a new empleado.
     *
     * @param empleado the empleado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleado, or with status {@code 400 (Bad Request)} if the empleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/empleados")
    public ResponseEntity<Empleado> createEmpleado(@Valid @RequestBody Empleado empleado) throws URISyntaxException {
        log.debug("REST request to save Empleado : {}", empleado);
        if (empleado.getId() != null) {
            throw new BadRequestAlertException("A new empleado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Empleado result = empleadoService.save(empleado);
        return ResponseEntity
            .created(new URI("/api/empleados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /empleados/:id} : Updates an existing empleado.
     *
     * @param id the id of the empleado to save.
     * @param empleado the empleado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleado,
     * or with status {@code 400 (Bad Request)} if the empleado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> updateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Empleado empleado
    ) throws URISyntaxException {
        log.debug("REST request to update Empleado : {}, {}", id, empleado);
        if (empleado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Empleado result = empleadoService.save(empleado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /empleados/:id} : Partial updates given fields of an existing empleado, field will ignore if it is null
     *
     * @param id the id of the empleado to save.
     * @param empleado the empleado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleado,
     * or with status {@code 400 (Bad Request)} if the empleado is not valid,
     * or with status {@code 404 (Not Found)} if the empleado is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/empleados/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Empleado> partialUpdateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Empleado empleado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Empleado partially : {}, {}", id, empleado);
        if (empleado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Empleado> result = empleadoService.partialUpdate(empleado);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, empleado.getId().toString())
        );
    }

    /**
     * {@code GET  /empleados} : get all the empleados.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleados in body.
     */
    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getAllEmpleados(Pageable pageable) {
        log.debug("REST request to get a page of Empleados");
        Page<Empleado> page = empleadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /empleados/:id} : get the "id" empleado.
     *
     * @param id the id of the empleado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long id) {
        log.debug("REST request to get Empleado : {}", id);
        Optional<Empleado> empleado = empleadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empleado);
    }

    /**
     * {@code GET  /empleados/:id&:nombre&:apellidos&:dni} : get list empleados.
     * @param id the id of the empleado to retrieve.
     * @param nombre the nombre of the empleado to retrieve.
     * @param apellidos the apellidos of the empleado to retrieve.
     * @param dni the dni of the empleado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleado, or with status {@code 400 (Bad Request)} if the empleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping(value = "/empleados/get-employees-by-filter", params = { "id", "nombre", "apellidos", "dni" })
    public ResponseEntity<List<Empleado>> getEmployeesByFilter(
        @RequestParam MultiValueMap<String, String> queryParams,
        UriComponentsBuilder uriBuilder,
        Pageable pageable,
        @RequestParam(value = "id", defaultValue = "0") String id,
        @RequestParam(value = "nombre", defaultValue = "") String nombre,
        @RequestParam(value = "apellidos", defaultValue = "") String apellidos,
        @RequestParam(value = "dni", defaultValue = "") String dni
    ) {
        log.debug("REST request to employees by filter: {}", id, nombre, apellidos, dni);
        final Page<Empleado> page = empleadoService.getEmployeesByFilter(Long.parseLong(id), nombre, apellidos, dni, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /empleados/:id} : delete the "id" empleado.
     *
     * @param id the id of the empleado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        log.debug("REST request to delete Empleado : {}", id);
        empleadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
