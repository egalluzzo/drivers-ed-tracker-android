package com.centricconsulting.driversedtracker.repository;

import com.centricconsulting.driversedtracker.model.Drive;

import java.util.List;

/**
 * Stores and retrieves {@link Drive}s.
 * Created by eric on 4/24/15.
 */
public interface DriveRepository {
    Drive findById(long id);
    List<Drive> findAll();
    Drive save(Drive drive);
}
