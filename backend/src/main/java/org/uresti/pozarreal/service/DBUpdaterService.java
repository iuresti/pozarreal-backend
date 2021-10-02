package org.uresti.pozarreal.service;

import org.uresti.pozarreal.model.Chip;

import java.util.List;

public interface DBUpdaterService {
     void updateChips(List<Chip> chips, String path);
}
