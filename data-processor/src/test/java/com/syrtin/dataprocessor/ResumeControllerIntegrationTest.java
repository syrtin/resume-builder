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
import com.syrtin.dataprocessor.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.wait.strategy.Wait;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.syrtin.dataprocessor.model.User.Role.ADMIN;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Import(RabbitMQConfig.class)
public class ResumeControllerIntegrationTest {
    
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
    public void testSaveResume() throws Exception {
        var user = new User("usr", "pwd", ADMIN);
        var newUser = userRepository.save(user);
        when(userService.getCurrentUser()).thenReturn(user);

        Photo photo = new Photo();
        photo.setPhotoData("dummyData".getBytes());
        photo.setUser(newUser);
        var savedPhoto = photoRepository.save(photo);

        Template template = new Template();
        template.setName(TEMPLATE_NAME);
        template.setDescription(TEMPLATE_DESCRIPTION);
        template.setImageData("templateData".getBytes());
        var savedTemplate = templateRepository.save(template);

        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setFullName(RESUME_NAME);
        resumeDto.setEmail(RESUME_EMAIL);
        resumeDto.setSkills(RESUME_SKILLS);
        resumeDto.setObjective(RESUME_OBJECTIVE);
        resumeDto.setEducations(Collections.singletonList(new EducationDto(null, EDUCATION_DEGREE,
                EDUCATION_INSTITUTE, EDUCATION_GRADUATION_YEAR)));
        resumeDto.setWorkExperiences(Collections.singletonList(new WorkExperienceDto(null,
                WORK_COMPANY, WORK_POSITION, WORK_BEGIN_DATE, WORK_END_DATE)));
        resumeDto.setTemplateId(savedPhoto.getId());
        resumeDto.setPhotoId(savedTemplate.getId());

        String resumeDtoJson = objectMapper.writeValueAsString(resumeDto);

        mockMvc.perform(post("/api/resumes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resumeDtoJson)
                        .with(SecurityMockMvcRequestPostProcessors.user("usr").roles("ADMIN")))
                .andExpect(status().isOk());

        var savedResumes = resumeRepository.findAll();
        assertThat(savedResumes).hasSize(1);
        Resume savedResume = savedResumes.get(0);
        assertThat(savedResume.getFullName()).isEqualTo(RESUME_NAME);
        assertThat(savedResume.getEmail()).isEqualTo(RESUME_EMAIL);
        assertThat(savedResume.getSkills()).isEqualTo(RESUME_SKILLS);
        assertThat(savedResume.getObjective()).isEqualTo(RESUME_OBJECTIVE);
        assertNull(savedResume.getInterests());
        assertThat(savedResume.getEducations()).hasSize(1);
        assertThat(savedResume.getWorkExperiences()).hasSize(1);
        assertThat(savedResume.getPhoto()).isNotNull();
        assertThat(savedResume.getTemplate()).isNotNull();
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

        Object mockResponse = new ResumePrintResponse();

        when(rabbitTemplate.convertSendAndReceive(any(ResumePrintRequest.class)))
                .thenReturn(mockResponse);


        mockMvc.perform(get("/api/resumes/" + savedResume.getId() + "/print")
                        .with(SecurityMockMvcRequestPostProcessors.user("usr").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fieldName").value("expectedValue"));
    }
}
