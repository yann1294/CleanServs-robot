package com.webservice.springService.controllers;

import com.webservice.springService.config.ServerProperties;
import com.webservice.springService.service.CleaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage")
public class CleaningController {

    private final CleaningService cleanService;




    @Autowired
    public CleaningController(CleaningService cleanService) {
        this.cleanService = cleanService;
    }

    @GetMapping("/connect")
    public ResponseEntity<String> connect() throws Exception {
        boolean success = cleanService.connect();
        return success
                ? new ResponseEntity<>("Successfully connected to server.", HttpStatus.OK)
                : new ResponseEntity<>("Failed to connect to server.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scanStorage(@RequestBody ServerProperties request) throws Exception {
        boolean success = cleanService.scanStorage(request.getDirectory());
        return success
                ? new ResponseEntity<>("Successfully scanned storage.", HttpStatus.OK)
                : new ResponseEntity<>("Failed to scan storage.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFiles(@RequestBody ServerProperties request) throws  Exception{
        boolean success = cleanService.removeFiles(request.getDirectory(), request.getThreshold());
        return success
                ? new ResponseEntity<>("Successfully removed files.", HttpStatus.OK)
                : new ResponseEntity<>("Failed to remove files.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/top")
    public ResponseEntity<List<String>> getTopOccupyingDirectories(
            @RequestBody ServerProperties request) throws Exception {
        List<String> result =
                cleanService.findTop10(request.getDirectory());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
