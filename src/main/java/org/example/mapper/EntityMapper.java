package org.example.mapper;

/**
 * Base interface for entity-DTO mapping.
 *
 * @param <E> Entity type
 * @param <D> DTO type
 */
public interface EntityMapper<E, D> {
    
    /**
     * Converts DTO to Entity.
     *
     * @param dto the DTO to convert
     * @return the entity
     */
    E toEntity(D dto);
    
    /**
     * Converts Entity to DTO.
     *
     * @param entity the entity to convert
     * @return the DTO
     */
    D toDto(E entity);
}
