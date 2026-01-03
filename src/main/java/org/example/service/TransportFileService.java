package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.TransportDto;
import org.example.exception.BusinessLogicException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service for saving and loading transport data to/from JSON files.
 * Requirement #8: Saving transport data in a file and ability to retrieve and display this data.
 */
public class TransportFileService {
    private static final Logger log = LogManager.getLogger(TransportFileService.class);
    private final ObjectMapper objectMapper;

    public TransportFileService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Save a list of transports to a JSON file.
     *
     * @param transports List of transport DTOs to save
     * @param filePath   Path to the output file
     */
    public void saveToFile(List<TransportDto> transports, String filePath) {
        try {
            File file = new File(filePath);
            objectMapper.writeValue(file, transports);
            log.info("Successfully saved {} transports to {}", transports.size(), filePath);
        } catch (IOException ex) {
            log.error("Failed to save transports to file {}", filePath, ex);
            throw new BusinessLogicException("Could not save transports to file: " + filePath, ex);
        }
    }

    /**
     * Load transports from a JSON file.
     *
     * @param filePath Path to the input file
     * @return List of transport DTOs loaded from file
     */
    public List<TransportDto> loadFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.warn("File does not exist: {}", filePath);
                return Collections.emptyList();
            }
            TransportDto[] transports = objectMapper.readValue(file, TransportDto[].class);
            log.info("Successfully loaded {} transports from {}", transports.length, filePath);
            return Arrays.asList(transports);
        } catch (IOException ex) {
            log.error("Failed to load transports from file {}", filePath, ex);
            throw new BusinessLogicException("Could not load transports from file: " + filePath, ex);
        }
    }

    /**
     * Save a single transport to a JSON file.
     *
     * @param transport Transport DTO to save
     * @param filePath  Path to the output file
     */
    public void saveSingleToFile(TransportDto transport, String filePath) {
        try {
            File file = new File(filePath);
            objectMapper.writeValue(file, transport);
            log.info("Successfully saved transport {} to {}", transport.id(), filePath);
        } catch (IOException ex) {
            log.error("Failed to save transport to file {}", filePath, ex);
            throw new BusinessLogicException("Could not save transport to file: " + filePath, ex);
        }
    }

    /**
     * Load a single transport from a JSON file.
     *
     * @param filePath Path to the input file
     * @return Transport DTO loaded from file
     */
    public TransportDto loadSingleFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.warn("File does not exist: {}", filePath);
                return null;
            }
            TransportDto transport = objectMapper.readValue(file, TransportDto.class);
            log.info("Successfully loaded transport from {}", filePath);
            return transport;
        } catch (IOException ex) {
            log.error("Failed to load transport from file {}", filePath, ex);
            throw new BusinessLogicException("Could not load transport from file: " + filePath, ex);
        }
    }
}
