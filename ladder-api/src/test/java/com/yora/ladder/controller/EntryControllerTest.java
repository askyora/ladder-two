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
import com.yora.ladder.entity.Entry;
import com.yora.ladder.entity.Step;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
          properties = {"spring.config.location=classpath:application-entry-it-tests.yml"})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EntryControllerTest extends BaseControllerTest {

     private MockMvc mvc;

     @Autowired
     private WebApplicationContext context;

     private Client c = null;
     private Step w, am, eu, au, as, af, lk = null;

     @BeforeEach
     public void setUp() {
          c = newClient("Yora", "YORA", "Yora INC.");
          w = newStep(c, "world", "world", true, true, "sun description");
          am = newStep(c, "america", "world.america", true, true, "america description", w);
          eu = newStep(c, "europe", "world.europe", true, true, "europe description", w);
          au = newStep(c, "australia", "world.australia", false, true, "australia description", w);
          as = newStep(c, "asia", "world.asia", true, true, "asia description", w);
          lk = newStep(c, "lk", "world.asia.lk", true, true, "sri lanka", as);
          af = newStep(c, "africa", "world.africa", true, false, "africa description", w);
          mvc = MockMvcBuilders.webAppContextSetup(context).build();
     }


     @Test
     @DisplayName("entry creation failed when step not found")
     public void test_entry_creation_fail_when_step_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/entry"))

                    .content(getEntryRequestDto("YORA", "world.arctic", "language", "us-en",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail",
                              is("400 Step address not found ! : world.arctic")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));

     }


     @Test
     @DisplayName("entry creation failed when client id not found")
     public void test_entry_creation_fail_when_client_id_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/entry"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("APPLE", "world.arctic", "language", "us-en",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail", is("400 Client code not found ! : APPLE")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));

     }


     @Test
     @DisplayName("create new entry successfully")
     public void test_create_new_entry() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/entry"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world", "language", "us-en", "STRING",
                              "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(201))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.key", is("language")))
                    .andExpect(jsonPath("$.stepAddress", is("world")))
                    .andExpect(jsonPath("$.value", is("us-en")))
                    .andExpect(jsonPath("$.type", is("STRING")))
                    .andExpect(jsonPath("$.section", is("default-settings")));

     }

     @Test
     @DisplayName("when create duplicate entry for same step throws 409 ")
     public void test_duplicate_entries_for_same_step() throws Exception {
          // Given
          Entry existingEntry = newEntry("background-colour", "green", "world.background-colour",
                    "default-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/entry"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world", "background-colour", "Blue",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(409))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.detail", is(
                              "409 Entry already exist ! address : world.background-colour clientId : YORA")))
                    .andExpect(jsonPath("$.title", is("Conflict")))
                    .andExpect(jsonPath("$.status", is(409)));

     }


     @Test
     @DisplayName("create duplicate entries for different steps")
     public void test_duplicate_entries_for_different_steps() throws Exception {
          // Given
          Entry existingEntry = newEntry("background-colour", "green", "world.background-colour",
                    "default-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v1.0.0/entry"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world.asia", "background-colour", "Blue",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(201))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.key", is("background-colour")))
                    .andExpect(jsonPath("$.stepAddress", is("world.asia")))
                    .andExpect(jsonPath("$.value", is("Blue")))
                    .andExpect(jsonPath("$.type", is("STRING")))
                    .andExpect(jsonPath("$.section", is("default-settings")));

     }



     @Test
     @DisplayName("entry update failed when client id not found")
     public void test_entry_update_fail_when_client_id_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("APPLE", "world.arctic", "language", "us-en",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail", is("400 Client code not found ! : APPLE")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));

     }


     @Test
     @DisplayName("entry update failed when step not found")
     public void test_entry_update_fail_when_step_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world.arctic", "language", "us-en",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail",
                              is("400 Step address not found ! : world.arctic")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));

     }


     @Test
     @DisplayName("entry update failed when new step not found")
     public void test_entry_update_fail_when_new_step_not_found() throws Exception {
          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/world.arctic"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world.europe", "language", "us-en",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(400))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.detail",
                              is("400 Step address not found ! : world.arctic")))
                    .andExpect(jsonPath("$.title", is("Bad Request")));

     }


     @Test
     @DisplayName("update new entry successfully with step")
     public void test_update_entry_with_step() throws Exception {
          // Given
          Entry existingEntry = newEntry("background-colour", "green", "world.background-colour",
                    "default-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/world.europe"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world", "background-colour", "white",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(200))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.key", is("background-colour")))
                    .andExpect(jsonPath("$.stepAddress", is("world.europe")))
                    .andExpect(jsonPath("$.value", is("white")))
                    .andExpect(jsonPath("$.type", is("STRING")))
                    .andExpect(jsonPath("$.section", is("default-settings")));

     }


     @Test
     @DisplayName("update new entry successfully")
     public void test_update_new_entry() throws Exception {
          // Given
          Entry existingEntry = newEntry("background-colour", "green", "world.background-colour",
                    "default-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world", "background-colour", "white",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(200))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.key", is("background-colour")))
                    .andExpect(jsonPath("$.stepAddress", is("world")))
                    .andExpect(jsonPath("$.value", is("white")))
                    .andExpect(jsonPath("$.type", is("STRING")))
                    .andExpect(jsonPath("$.section", is("default-settings")));

     }


     @Test
     @DisplayName("when entry not found")
     public void test_entry_not_found() throws Exception {
          // Given
          Entry existingEntry = newEntry("background-colour", "green", "world.background-colour",
                    "default-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders.put(URI.create("/api/v1.0.0/entry/"))
                    // .with(httpBasic("yora", "S3cret"))
                    .content(getEntryRequestDto("YORA", "world.asia", "background-colour", "Blue",
                              "STRING", "default-settings"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(404))
                    .andExpect(content()
                              .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.detail", is(
                              "404 Entry not found ! address : world.asia.background-colour clientId : YORA")))
                    .andExpect(jsonPath("$.title", is("Not Found")))
                    .andExpect(jsonPath("$.status", is(404)));

     }


     @Test
     @DisplayName("when entry found from upper")
     public void test_entry_found() throws Exception {
          // Given
          Entry existingEntry =
                    newEntry("language", "eng", "world.language", "iso-settings", w, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders
                    .get(URI.create("/api/v1.0.0/entry/YORA/world.asia.lk.language"))
                    // .with(httpBasic("yora", "S3cret"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(200))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].key", is("language")))
                    .andExpect(jsonPath("$[0].stepAddress", is("world")))
                    .andExpect(jsonPath("$[0].value", is("eng")))
                    .andExpect(jsonPath("$[0].type", is("STRING")))
                    .andExpect(jsonPath("$[0].section", is("iso-settings")));

     }

     @Test
     @DisplayName("when get all entries found")
     public void test_get_all_entries() throws Exception {
          // Given
          Entry existingEntry =
                    newEntry("language", "eng", "world.language", "iso-settings", w, "STRING");

          Entry existingEntry2 = newEntry("language", "chi", "world.asia.language", "iso-settings",
                    as, "STRING");

          // When /Then
          mvc.perform(MockMvcRequestBuilders
                    .get(URI.create("/api/v1.0.0/entry/YORA/world.asia.lk.language"))
                    .header("retrieve-all", "true")
                    // .with(httpBasic("yora", "S3cret"))
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                    .andExpect(status().is(200))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].key", is("language")))
                    .andExpect(jsonPath("$[0].stepAddress", is("world.asia")))
                    .andExpect(jsonPath("$[0].value", is("chi")))
                    .andExpect(jsonPath("$[0].type", is("STRING")))
                    .andExpect(jsonPath("$[0].section", is("iso-settings")))
                    .andExpect(jsonPath("$[1].key", is("language")))
                    .andExpect(jsonPath("$[1].stepAddress", is("world")))
                    .andExpect(jsonPath("$[1].value", is("eng")))
                    .andExpect(jsonPath("$[1].type", is("STRING")))
                    .andExpect(jsonPath("$[1].section", is("iso-settings")));

     }

     @Test
     @DisplayName("when entries not found 404 status return")
     public void test_entries_not_found() throws Exception {

          // Given /When /Then
          mvc.perform(MockMvcRequestBuilders
                    .get(URI.create("/api/v1.0.0/entry/YORA/jupitor.europa.language"))
                    .header("retrieve-all", "true"))
                    // .with(httpBasic("yora", "S3cret")))
                    .andDo(print()).andExpect(status().is(404));

     }

}
