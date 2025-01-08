package kapyrin.myshopspring.util;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.context.WebApplicationContext;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportWriterTest {
    private ReportWriter reportWriter;
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        reportWriter = new ReportWriter();

        servletContext = Mockito.mock(ServletContext.class);
        when(servletContext.getRealPath("/reports")).thenReturn(System.getProperty("java.io.tmpdir") + "/reports");

        WebApplicationContext applicationContext = Mockito.mock(WebApplicationContext.class);
        when(applicationContext.getServletContext()).thenReturn(servletContext);
        reportWriter.setApplicationContext(applicationContext);
    }


    @Test
    void saveStringToFile() {
        String content = "Test content";
        String fileName = "testReport" + LocalDate.now();

        File savedFile = reportWriter.saveStringToFile(content, fileName);

        assertTrue(savedFile.exists());

        try {
            String fileContent = Files.readString(Paths.get(savedFile.getPath()));
            assertEquals(content, fileContent);
        } catch (IOException e) {
            fail("Could not read the file content");
        } finally {
            savedFile.delete();
        }
    }
}