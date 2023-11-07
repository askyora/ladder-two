package com.yora.ladder.controller;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.yora.ladder.entity.Client;
import com.yora.ladder.entity.Step;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
          properties = {"spring.config.location=classpath:application-steps-it-tests.yml"})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StepControllerTest extends BaseControllerTest {

     private MockMvc mvc;

     @Autowired
     private WebApplicationContext context;

     private Client c = null;
     private Step s = null;

     @BeforeEach
     public void setUp() {
          c = newClient("Yora", "YORA", "Yora INC.");
          s = newStep(c, "sun", "sun", true, true, "sun description");
          mvc = MockMvcBuilders.webAppContextSetup(context).build();
     }

     @Test
     @DisplayName("new step created when client exist")
     public void test_create_new_step() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/step"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getStepsContent("YORA", "world", "world description", "sun"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(201))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name", is("world")))
                    .andExpect(jsonPath("$.description", is("world description")))
                    .andExpect(jsonPath("$.inheritable", is(true)))
                    .andExpect(jsonPath("$.overridable", is(false)))
                    .andExpect(jsonPath("$.address", is("sun.world")))
                    .andExpect(jsonPath("$.parent.name", is("sun")));
     }

     @Test
     @DisplayName("new step creation fail when parent code not found")
     public void test_create_new_step_fail_when_parent_code_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/step"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getStepsContent("YORA", "jupiter", "jupiter description", "alpha"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.detail", is("400 Step address not found ! : alpha")))
                    .andExpect(jsonPath("$.status", is(400)));
     }

     @Test
     @DisplayName("step creation fail when client code not found")
     public void test_create_new_step_fail_when_no_client_found() throws Exception {

          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/step"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getStepsContent("UNK", "world", "world description", null))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.detail", is("400 Client code not found ! : UNK")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));
     }

     @Test
     @DisplayName("step creation fail when step already existing")
     public void test_create_new_step_fail_when_step_already_existing() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/step"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getStepsContent("YORA", "sun", "sun description", null))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(409))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.detail", is(
                              "409 Conflict with existing : Step(name=sun, description=sun description, address=sun, client=Client(code=YORA, name=Yora, description=Yora INC.), inheritable=true, overridable=true, parent=null)")))
                    .andExpect(jsonPath("$.title", is("Conflict")));
     }


}
