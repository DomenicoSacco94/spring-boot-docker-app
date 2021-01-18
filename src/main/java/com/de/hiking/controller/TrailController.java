package com.de.hiking.controller;

import com.de.hiking.models.Trail;
import com.de.hiking.service.TrailService;
import com.de.hiking.utils.LoggerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Contains the controller methods managing the <code>Trail</code> entity
 */
@Api(value = "/api/hiking/v1", tags = {"Hiking API v1"})
@RequestMapping("/api/hiking/v1")
@Controller
public class TrailController {

    Logger logger = LoggerFactory.getLogger(TrailController.class);

    @Autowired
    TrailService trailService;

    @RequestMapping(value = "/trails", method = RequestMethod.GET)
    @ApiOperation(value = "Trails retrieval", notes = "retrieve all stored trails")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Trail.class, responseContainer = "list")})
    @ResponseBody
    public ResponseEntity<List<Trail>> getTrails() {
        LoggerUtil.logEnterMethod(logger);
        List<Trail> result = trailService.getTrails();
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
