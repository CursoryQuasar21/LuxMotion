package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.service.ClienteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cliente}.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        log.debug("Request to save Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> partialUpdate(Cliente cliente) {
        log.debug("Request to partially update Cliente : {}", cliente);

        return clienteRepository
            .findById(cliente.getId())
            .map(
                existingCliente -> {
                    if (cliente.getNombre() != null) {
                        existingCliente.setNombre(cliente.getNombre());
                    }
                    if (cliente.getApellidos() != null) {
                        existingCliente.setApellidos(cliente.getApellidos());
                    }
                    if (cliente.getDni() != null) {
                        existingCliente.setDni(cliente.getDni());
                    }

                    return existingCliente;
                }
            )
            .map(clienteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id);
    }

    //Metodo Creado
    //Sirve para aplicar los filtros y obtener los resultados de la base de datos
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> getClientsByFilter(Long id, String nombre, String apellidos, String dni, Pageable pageable) {
        if (id == 0) return clienteRepository.getClientsByFilter(
            nombre,
            apellidos,
            dni,
            pageable
        ); else return clienteRepository.getClientsByFilter(id, nombre, apellidos, dni, pageable);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
