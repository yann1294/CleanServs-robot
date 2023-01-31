package com.webservice.springService.controllers;

import com.webservice.springService.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*annotated with @RestController and @RequestMapping("/storage")
  to map it to the /storage endpoint
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    /*The StorageService class is injected into the controller using the @Autowired annotation*/
    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

//    @GetMapping("/check")
//    public ResponseEntity<String> checkStorage() {
//        String response = storageService.checkStorage();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//    @DeleteMapping("/cleanup")
//    public ResponseEntity<String> cleanupStorage() {
//        String response = storageService.cleanupStorage();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    /* logic for checking and cleaning up storage*/
    @GetMapping("/checkandclean")
    public ResponseEntity<String> checkAndCleanStorage(){
        String response = storageService.checkAndCleanStorage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*logic for getting the top folders.*/
    @GetMapping("/topfolders")
    public ResponseEntity<List<String>> topFolders() {
        List<String> topFolders = storageService.getTopTenFolders();
        return new ResponseEntity<>(topFolders, HttpStatus.OK);
    }
}
