package com.de.hiking.service;

import com.de.hiking.models.Hiker;
import com.de.hiking.repository.HikerRepository;
import com.de.hiking.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


/**
 * Service layer managing the <code>Hiker</code> entity
 */
@Service
public class HikerService {

    Logger logger = LoggerFactory.getLogger(HikerService.class);

    @Autowired
    HikerRepository hikerRepository;

    /**
     * Creates a <code>Hiker</code> entity
     *
     * @param hiker
     * @return the created <code>Hiker</code>
     */
    public Hiker createHiker(Hiker hiker) {
        LoggerUtil.logEnterMethod(logger, hiker);
        Hiker result = hikerRepository.createHiker(hiker);
        LoggerUtil.logExitMethod(logger, result);
        return result;
    }

    /**
     * Deletes the <code>Hiker</code> entity identified by that <code>hikerId</code>
     *
     * @param hikerId
     */
    public void deleteHiker(UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, hikerId);
        hikerRepository.deleteHiker(hikerId);
        LoggerUtil.logExitMethod(logger, "");
    }

    /**
     * Retrieves a <code>List</code> of all the registered <code>Hiker</code>
     *
     * @return the <code>Hiker</code> <code>List</code>
     */
    public List<Hiker> getHikers() {
        LoggerUtil.logEnterMethod(logger);
        List<Hiker> result = hikerRepository.getHikers();
        LoggerUtil.logExitMethod(logger, "");
        return result;
    }

    /**
     * Retrieves a <code>Hiker</code> associated to a specific <code>UUID</code>
     *
     * @return the <code>Hiker</code>
     */
    public Hiker getHiker(UUID hikerId) {
        LoggerUtil.logEnterMethod(logger);
        Hiker result = hikerRepository.getHiker(hikerId);
        if (result == null) {
            ResponseStatusException re = new ResponseStatusException(HttpStatus.NOT_FOUND, "Hiker inside the booking not found");
            LoggerUtil.logException(logger, re);
            throw re;
        }
        LoggerUtil.logExitMethod(logger, "");
        return result;
    }

}
