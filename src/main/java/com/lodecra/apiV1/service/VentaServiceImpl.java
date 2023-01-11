package com.lodecra.apiV1.service;

import com.lodecra.apiV1.mapstruct.mappers.VentaMapper;
import com.lodecra.apiV1.model.Venta;
import com.lodecra.apiV1.repository.port.VentaRepository;
import com.lodecra.apiV1.service.port.VentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class VentaServiceImpl implements VentaService {

    VentaRepository repository;

    VentaMapper mapper;

    public VentaServiceImpl(VentaRepository repository, VentaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public void hacerVentaRapida(Venta aVenderAhora) {
        repository.saveVenta(aVenderAhora);
    }
}
