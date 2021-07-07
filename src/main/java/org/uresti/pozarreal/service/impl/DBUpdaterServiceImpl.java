package org.uresti.pozarreal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.model.Chip;
import org.uresti.pozarreal.service.DBUpdaterService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DBUpdaterServiceImpl implements DBUpdaterService {

    private static final int EMPLOYEE_ID_ACTIVE = 1;
    private static final int EMPLOYEE_ID_INACTIVE = 0;
    public static final String PATH_TO_DB_PLACEHOLDER = "PATH_TO_DB";

    private final PozarrealConfig pozarrealConfig;

    public DBUpdaterServiceImpl(PozarrealConfig pozarrealConfig) {
        this.pozarrealConfig = pozarrealConfig;
    }

    @Override
    public void updateChips(List<Chip> chips, String path) {
        //"C:/Users/Ivan.Uresti/Desktop/database/Property2.mdb"
        String query = "UPDATE Employee as e SET e.employeesId = ?  " +
                " WHERE e.employeeId IN (SELECT c.employeeId FROM Card c WHERE c.cardLow = ?) ";
        String url = pozarrealConfig.getDatasourceUrl().replace(PATH_TO_DB_PLACEHOLDER, path);

        log.info("Inicia proceso de actualización de chips");
        Instant start = Instant.now();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement updateStatement = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            for (Chip chip : chips) {

                updateStatement.setInt(1, chip.isValid() ? EMPLOYEE_ID_ACTIVE : EMPLOYEE_ID_INACTIVE);
                updateStatement.setInt(2, Integer.parseInt(chip.getCode()));
                updateStatement.addBatch();
                log.info("Actualizando chip {} -> {} ", chip.getCode(), chip.isValid() ? "Activo" : "Inactivo");
            }

            int[] updates = updateStatement.executeBatch();

            conn.commit();

            Instant finish = Instant.now();

            int result = Arrays.stream(updates).reduce(0, Integer::sum);

            log.info("Se actualizaron {} chips en {} segundos", result, Duration.between(start, finish).toMillis() / 1000.0);

        } catch (SQLException ex) {
            log.error("No fue posible actualizar los chips", ex);
            throw new PozarrealSystemException("Error updating chips", "ERROR_UPDATING_CHIPS", ex);
        }
    }
}
