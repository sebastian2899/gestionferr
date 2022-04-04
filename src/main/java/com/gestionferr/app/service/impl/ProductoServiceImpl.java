package com.gestionferr.app.service.impl;

import com.gestionferr.app.domain.Producto;
import com.gestionferr.app.repository.ProductoRepository;
import com.gestionferr.app.service.ProductoService;
import com.gestionferr.app.service.dto.ProductoDTO;
import com.gestionferr.app.service.mapper.ProductoMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);

        if (producto.getIdCategoria() != null) {
            producto.setNombreCategoria(nombreCategoria(producto.getIdCategoria()));
        }

        producto = productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO) {
        log.debug("Request to partially update Producto : {}", productoDTO);

        return productoRepository
            .findById(productoDTO.getId())
            .map(existingProducto -> {
                productoMapper.partialUpdate(existingProducto, productoDTO);

                return existingProducto;
            })
            .map(productoRepository::save)
            .map(productoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        log.debug("Request to get all Productos");

        List<Producto> productos = productoRepository.findAll();

        for (Producto producto : productos) {
            if (producto.getIdCategoria() != null) {
                double precioProducto = producto.getPrecio().doubleValue();
                double precio = Math.round(precioProducto * (Math.pow(10, 0)) / Math.pow(10, 0));
                producto.setPrecio(BigDecimal.valueOf(precio));
                producto.setNombreCategoria(nombreCategoria(producto.getIdCategoria()));
            }
        }

        return productoMapper.toDto(productos);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id).map(productoMapper::toDto);
    }

    private String nombreCategoria(Long id) {
        String nombre = productoRepository.nombreCategoria(id);
        return nombre;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> productosPorCategoria(Long idCategoria) {
        log.debug("Request to get all products per category");

        List<Producto> productosCategoria = productoRepository.productosPorCategoria(idCategoria);

        for (Producto producto : productosCategoria) {
            producto.setNombreCategoria(nombreCategoria(producto.getIdCategoria()));
        }

        return productosCategoria.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> productosFiltroAutomatico(int codigo) {
        log.debug("Request to get all produtcs per code", codigo);

        List<Producto> productos = null;

        if (codigo == 1) {
            productos = productoRepository.productosOrderNombre();
        } else if (codigo == 2) {
            productos = productoRepository.productosOrderPrecio();
        }

        if (productos != null) {
            for (Producto producto : productos) {
                producto.setNombreCategoria(nombreCategoria(producto.getIdCategoria()));
            }
        }

        return productos.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> productosAgotados() {
        log.debug("Request to get all sold productos");
        List<Producto> productosAgotados = productoRepository.productosAgotados();
        return productosAgotados.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> productosCasiAgotados() {
        List<Producto> productosCasiAgotados = productoRepository.productosCasiAgotados();
        return productosCasiAgotados.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void actualizarPrecioProductos(String opcion, Long porcentaje) {
        log.debug("Request to change values of products");

        List<Producto> productos = productoRepository.findAll();

        switch (opcion) {
            case "aumento":
                for (Producto producto : productos) {
                    BigDecimal aumento = BigDecimal.valueOf(porcentaje);
                    BigDecimal porcentajeAumento = (producto.getPrecio().multiply(aumento)).divide(new BigDecimal(100));

                    producto.setPrecio(producto.getPrecio().add(porcentajeAumento));
                    productoRepository.save(producto);
                }

                break;
            case "decremento":
                for (Producto producto : productos) {
                    BigDecimal decremento = BigDecimal.valueOf(porcentaje);
                    BigDecimal valorDecrementar = (producto.getPrecio().multiply(decremento)).divide(new BigDecimal(100));

                    producto.setPrecio(producto.getPrecio().subtract(valorDecrementar));
                    productoRepository.save(producto);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public List<ProductoDTO> productosPorNombre(String nombre) {
        log.debug("Request toi get all products per name");

        List<Producto> productosPorNombre = null;

        if (nombre != null && !nombre.isEmpty()) {
            String nombreParametro = "%" + nombre.toUpperCase() + "%";
            productosPorNombre = productoRepository.productosPorNombre(nombreParametro);
        }

        return productosPorNombre.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
