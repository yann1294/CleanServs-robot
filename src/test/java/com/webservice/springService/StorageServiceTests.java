package com.webservice.springService;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.webservice.springService.config.AppProperties;
import com.webservice.springService.service.StorageService;
import org.junit.jupiter.api.Test;


    class StorageServiceTests {

//        private AppProperties appProperties;
//        private StorageService storageService;

//        @BeforeEach
//        void setUp() {
//            appProperties = Mockito.mock(AppProperties.class);
//            storageService = new StorageService(appProperties);
//        }

        @Test
        public void testCheckAndCleanStorage() throws IOException {
            // setup mock objects
            Process process = mock(Process.class);
            BufferedReader bufferedReader = mock(BufferedReader.class);
            InputStream inputStream = mock(InputStream.class);
            when(inputStream.read()).thenReturn(-1);
            when(bufferedReader.readLine()).thenReturn("86");
            when(process.getInputStream()).thenReturn(inputStream);

            // setup AppProperties and StorageService objects
            AppProperties appProperties = new AppProperties();
            appProperties.setThreshold(85);
            List<AppProperties.Server> servers = new ArrayList<>();
            servers.add(new AppProperties.Server("EDFXTSTXES001", "edifecs/edifecs"));
            appProperties.setServers(servers);
            StorageService storageService = new StorageService(appProperties);

            // set up the mock to return the desired values when certain methods are called

//            doReturn(process).when(Runtime.getRuntime()).exec(eq("df -h edifecs/edifecs | awk '{print $5}' | tail -1 | tr -d '%'"));
            doReturn(process).when(Runtime.getRuntime()).exec(anyString());
//            doReturn(process).when(Runtime.getRuntime()).exec(eq("find edifecs/edifecs -type f -mtime +2 -exec rm -f {} +"));
            when(bufferedReader.readLine()).thenReturn("86");

            // call the checkAndCleanStorage method and verify the desired behavior
            storageService.checkAndCleanStorage();
            verify(Runtime.getRuntime(), times(2)).exec(anyString());
            verify(process, times(2)).getInputStream();
            verify(bufferedReader, times(1)).readLine();
        }
    }
