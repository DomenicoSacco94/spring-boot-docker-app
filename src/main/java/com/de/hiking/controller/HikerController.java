package com.de.hiking.controller;

import com.de.hiking.models.Hiker;
import com.de.hiking.models.Trail;
import com.de.hiking.service.HikerService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contains the controller methods managing the <code>Hiker</code> entity
 */
@Api(value = "/api/hiking/v1", tags = {"Hiking API v1"})
@RequestMapping("/api/hiking/v1")
@Controller
public class HikerController {

    Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    HikerService hikerService;

    @RequestMapping(value = "/hiker", method = RequestMethod.POST)
    @ApiOperation(value = "Hiker registration", notes = "Register a hiker")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Hiker.class)})
    @ResponseBody
    public ResponseEntity<Hiker> createHiker(@RequestBody Hiker hiker) {
        LoggerUtil.logEnterMethod(logger, hiker);
        Hiker result = hikerService.createHiker(hiker);
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/hiker/{hikerId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Hiker deletion", notes = "Delete a hiker")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Hiker not found")})
    @ResponseBody
    public ResponseEntity deleteHiker(@PathVariable UUID hikerId) {
        LoggerUtil.logEnterMethod(logger, hikerId);
        hikerService.deleteHiker(hikerId);
        ResponseEntity result = new ResponseEntity(HttpStatus.OK);
        LoggerUtil.logExitMethod(logger, result);
        return result;
    }

    @RequestMapping(value = "/hikers", method = RequestMethod.GET)
    @ApiOperation(value = "Hikers retrieval", notes = "retrieve all stored hikers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Hiker.class, responseContainer = "list")})
    @ResponseBody
    public ResponseEntity<List<Hiker>> getHikers() {
        LoggerUtil.logEnterMethod(logger);
        List<Hiker> result = hikerService.getHikers();
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/hiker//{hikerId}", method = RequestMethod.GET)
    @ApiOperation(value = "Hikers retrieval", notes = "retrieve all stored hikers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Hiker.class),
            @ApiResponse(code = 404, message = "Hiker not found")
    })
    @ResponseBody
    public ResponseEntity<Hiker> getHiker(@PathVariable UUID hikerId) {
        LoggerUtil.logEnterMethod(logger);
        Hiker result = hikerService.getHiker(hikerId);
        LoggerUtil.logExitMethod(logger, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
