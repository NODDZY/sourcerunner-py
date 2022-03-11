package ntnu.stud.coderunner;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DockerService {
    Logger logger = LoggerFactory.getLogger(DockerService.class);

    public String runCode(String code) throws IOException {
        // Save received code to file p.py
        File file = new File("src/main/resources/p.py");
        PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
        writer.write(code);
        writer.close();

        // Check/Build Docker image
        String imageName = "python-image";
        if (!checkDockerImageExists(imageName)) {
            logger.info("No Docker image found...");
            // Build Docker image from resources/Dockerfile
            String [] comp = {"/bin/bash","-c","sudo docker build -t " +imageName+ ":latest -f Dockerfile ."};
            Runtime.getRuntime().exec(comp);
        }
        else {logger.debug("Docker image [" + imageName + "] already exists...");}

        // Run python-image
        // Copy /p.py
        // Run 'python p.py'
        // Close Docker container when done
        String [] args = {"/bin/bash","-c","sudo docker run --mount type=bind,source="+file.getAbsolutePath()+",target=/root/docker/p.py --rm python-image python p.py"};
        Process run = Runtime.getRuntime().exec(args);

        // Get output from terminal
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(run.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(run.getErrorStream()));

        StringBuilder output = new StringBuilder();
        try {
            run.waitFor();
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");}
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");}

            logger.info("Output:\n" + output);

        } catch (InterruptedException e) {logger.error(e.getMessage());}

        // Delete file
        // If p.py file is not deleted, it will be overwritten next time method is run
        file.delete();
        // Send output back to client
        return output.toString();
    }

    private boolean checkDockerImageExists(String imageName) throws IOException {
        String [] comp = {"/bin/bash","-c","sudo docker images --quiet " +imageName};
        Process process = Runtime.getRuntime().exec(comp);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
