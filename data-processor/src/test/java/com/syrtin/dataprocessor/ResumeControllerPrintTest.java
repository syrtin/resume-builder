package com.syrtin.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syrtin.dataprocessor.config.RabbitMQConfig;
import com.syrtin.dataprocessor.dto.*;
import com.syrtin.dataprocessor.model.Photo;
import com.syrtin.dataprocessor.model.Resume;
import com.syrtin.dataprocessor.model.Template;
import com.syrtin.dataprocessor.model.User;
import com.syrtin.dataprocessor.repository.PhotoRepository;
import com.syrtin.dataprocessor.repository.ResumeRepository;
import com.syrtin.dataprocessor.repository.TemplateRepository;
import com.syrtin.dataprocessor.repository.UserRepository;
import com.syrtin.dataprocessor.service.ResumeService;
import com.syrtin.dataprocessor.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.syrtin.dataprocessor.model.User.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class ResumeControllerPrintTest {

    private static final String RESUME_NAME = "Ivan Ivanov";

    private static final String RESUME_EMAIL = "john.doe@example.com";

    private static final String RESUME_SKILLS = "Java, Spring Boot";

    private static final String RESUME_OBJECTIVE = "To become a software engineer";

    private static final String TEMPLATE_NAME = "Template_01_Test";

    private static final String TEMPLATE_DESCRIPTION = "The test one";

    private static final String EDUCATION_DEGREE = "Bachelor of IT";

    private static final String EDUCATION_INSTITUTE = "IT University";

    private static final int EDUCATION_GRADUATION_YEAR = 2020;

    private static final String WORK_COMPANY = "IT Company";

    private static final String WORK_POSITION = "IT Engineer";

    private static final LocalDate WORK_BEGIN_DATE = LocalDate.of(2020, 1, 1);

    private static final LocalDate WORK_END_DATE = LocalDate.of(2020, 10, 7);

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeService resumeService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
            .withUsername("usr")
            .withPassword("pwd")
            .withDatabaseName("testDb");

    @Container
    public static RabbitMQContainer rabbitContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management-alpine"))
            .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    static void containerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitContainer.getMappedPort(5672).toString());
        registry.add("spring.rabbitmq.username", () -> "guest");
        registry.add("spring.rabbitmq.password", () -> "guest");
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        photoRepository.deleteAll();
        templateRepository.deleteAll();
        resumeRepository.deleteAll();
    }

    @Test
    public void testPrintResume() throws Exception {
        User user = new User("usr", "pwd", ADMIN);
        User newUser = userRepository.save(user);

        Photo photo = new Photo();
        photo.setPhotoData("photoData".getBytes());
        photo.setUser(newUser);
        Photo savedPhoto = photoRepository.save(photo);

        Template template = new Template();
        template.setName(TEMPLATE_NAME);
        template.setDescription(TEMPLATE_DESCRIPTION);
        template.setImageData("templateData".getBytes());
        Template savedTemplate = templateRepository.save(template);

        Resume resume = new Resume();
        resume.setFullName(RESUME_NAME);
        resume.setEmail(RESUME_EMAIL);
        resume.setSkills(RESUME_SKILLS);
        resume.setObjective(RESUME_OBJECTIVE);
        resume.setPhoto(savedPhoto);
        resume.setTemplate(savedTemplate);
        resume.setCreatedAt(LocalDateTime.now());
        resume.setModifiedAt(LocalDateTime.now());
        resume.setUser(newUser);
        Resume savedResume = resumeRepository.save(resume);

        ResumePrintResponse mockResponse = new ResumePrintResponse();
        mockResponse.setStatusCode(0L);
        mockResponse.setMessage("expectedMessage");
        mockResponse.setLink("expectedValue");

        given(rabbitTemplate.convertSendAndReceive(any(ResumePrintRequest.class))).willReturn(mockResponse);

        mockMvc.perform(get("/api/resumes/" + savedResume.getId() + "/print")
                        .with(SecurityMockMvcRequestPostProcessors.user("usr").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldName").value("expectedValue"));
    }
}
