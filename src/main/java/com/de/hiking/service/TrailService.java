package com.de.hiking.service;

import com.de.hiking.models.Trail;
import com.de.hiking.repository.TrailRepository;
import com.de.hiking.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service layer managing the <code>Trail</code> entity
 */
@Service
public class TrailService {

    Logger logger = LoggerFactory.getLogger(TrailService.class);

    @Autowired
    TrailRepository trailRepository;

    /**
     * Retrieves a <code>List</code> of all the <code>Trail</code>
     *
     * @return the <code>Trail</code> <code>List</code>
     */
    public List<Trail> getTrails() {
        LoggerUtil.logEnterMethod(logger, "");
        List<Trail> result = trailRepository.getTrails();
        LoggerUtil.logExitMethod(logger, result);
        return result;
    }

}
