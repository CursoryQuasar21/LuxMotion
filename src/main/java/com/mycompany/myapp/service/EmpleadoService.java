package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Empleado;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Empleado}.
 */
public interface EmpleadoService {
    /**
     * Save a empleado.
     *
     * @param empleado the entity to save.
     * @return the persisted entity.
     */
    Empleado save(Empleado empleado);

    /**
     * Partially updates a empleado.
     *
     * @param empleado the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Empleado> partialUpdate(Empleado empleado);

    /**
     * Get all the empleados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Empleado> findAll(Pageable pageable);

    /**
     * Get the "id" empleado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Empleado> findOne(Long id);

    /**
     * Get empleado by filter.
     * @param id the id of the entity.
     * @param nombre the nombre of the entity.
     * @param apellidos the apellidos of the entity.
     * @param dni the dni of the entity.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Empleado> getEmployeesByFilter(Long id, String nombre, String apellidos, String dni, Pageable pageable);

    /**
     * Delete the "id" empleado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
