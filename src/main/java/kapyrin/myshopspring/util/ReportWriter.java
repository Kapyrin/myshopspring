package kapyrin.myshopspring.util;

import jakarta.servlet.ServletContext;
import kapyrin.myshopspring.exception.SaveToFileException;
import kapyrin.myshopspring.util.utilinterface.SaveToFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class ReportWriter implements SaveToFile, ApplicationContextAware {

    private ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (applicationContext instanceof WebApplicationContext) {
            this.servletContext = ((WebApplicationContext) applicationContext).getServletContext();
        }
    }

    public File saveStringToFile(String content, String fileName) {
        if (servletContext == null) {
            throw new IllegalStateException("ServletContext is not available.");
        }

        String realPath = servletContext.getRealPath("/reports");
        String filePath = Paths.get(realPath, fileName + java.time.LocalDate.now()) + ".txt";
        System.out.println("File saving to " + filePath);

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("Saved string to file");
        } catch (IOException e) {
            throw new SaveToFileException("Could not save string to file", e);
        }
        return file;
    }
}
