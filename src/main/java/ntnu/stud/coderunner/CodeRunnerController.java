package ntnu.stud.coderunner;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CodeRunnerController {
    private final DockerService docker;
    Logger logger = LoggerFactory.getLogger(CodeRunnerController.class);

    @Autowired
    public CodeRunnerController(DockerService docker) {
        this.docker = docker;
    }

    @GetMapping("/run")
    public String getCodeOutput(@RequestParam(value = "q") String code) throws IOException {
        logger.info("Input received:\n" + code);
        // FIXME: Will run into error if Linux system does not have Bash or Docker
        return docker.runCode(code);
    }
}
