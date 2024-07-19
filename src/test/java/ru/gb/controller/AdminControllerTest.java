package ru.gb.controller;

import ru.gb.constants.ErrorMessage;
import ru.gb.constants.Pages;
import ru.gb.constants.PathConstants;
import ru.gb.constants.SuccessMessage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.gb.util.TestConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithUserDetails(TestConstants.ADMIN_EMAIL)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-perfumes-before.sql", "/sql/create-user-before.sql", "/sql/create-orders-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-orders-after.sql", "/sql/create-user-after.sql", "/sql/create-perfumes-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /admin/perfumes - Get Perfumes")
    public void getPerfumes() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/perfumes"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PERFUMES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(12))));
    }

    @Test
    @DisplayName("[200] GET /admin/perfumes/search - Search Perfumes By perfumer")
    public void searchPerfumes_ByPerfumer() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/perfumes/search")
                        .param("searchType", "perfumer")
                        .param("text", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PERFUMES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /admin/perfumes/search - Search Perfumes By country")
    public void searchPerfumes_ByCountry() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/perfumes/search")
                        .param("searchType", "country")
                        .param("text", "Spain"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PERFUMES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/perfumes/search - Search Perfumes By perfumeTitle")
    public void searchPerfumes_PerfumeTitle() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/perfumes/search")
                        .param("searchType", "perfumeTitle")
                        .param("text", "Aventus"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_PERFUMES))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/perfumes/search - Get Users")
    public void getUsers() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(3))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By email")
    public void searchUsers_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "email")
                        .param("text", TestConstants.USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By First Name")
    public void searchUsers_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "firstName")
                        .param("text", TestConstants.USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By Last Name")
    public void searchUsers_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "lastName")
                        .param("text", TestConstants.USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/order/111 - Get Order")
    public void getOrder() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/order/{orderId}", 111))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDER))
                .andExpect(model().attribute("order", hasProperty("id", Matchers.is(TestConstants.ORDER_ID))))
                .andExpect(model().attribute("order", hasProperty("totalPrice", Matchers.is(TestConstants.ORDER_TOTAL_PRICE))))
                .andExpect(model().attribute("order", hasProperty("firstName", Matchers.is(TestConstants.ORDER_FIRST_NAME))))
                .andExpect(model().attribute("order", hasProperty("lastName", Matchers.is(TestConstants.ORDER_LAST_NAME))))
                .andExpect(model().attribute("order", hasProperty("city", Matchers.is(TestConstants.ORDER_CITY))))
                .andExpect(model().attribute("order", hasProperty("address", Matchers.is(TestConstants.ORDER_ADDRESS))))
                .andExpect(model().attribute("order", hasProperty("email", Matchers.is(TestConstants.ORDER_EMAIL))))
                .andExpect(model().attribute("order", hasProperty("phoneNumber", Matchers.is(TestConstants.ORDER_PHONE_NUMBER))))
                .andExpect(model().attribute("order", hasProperty("postIndex", Matchers.is(TestConstants.ORDER_POST_INDEX))))
                .andExpect(model().attribute("order", hasProperty("perfumes", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders - Get Orders")
    public void getOrders() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Email")
    public void searchOrders_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "email")
                        .param("text", TestConstants.USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders bt First Name")
    public void searchOrders_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "firstName")
                        .param("text", TestConstants.USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Last Name")
    public void searchOrders_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "lastName")
                        .param("text", TestConstants.USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/perfume/1 - Get Perfume")
    public void getPerfume() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.ADMIN + "/perfume/{perfumeId}", TestConstants.PERFUME_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_PERFUME))
                .andExpect(model().attribute("perfume", hasProperty("id", Matchers.is(TestConstants.PERFUME_ID))))
                .andExpect(model().attribute("perfume", hasProperty("perfumeTitle", Matchers.is(TestConstants.PERFUME_TITLE))))
                .andExpect(model().attribute("perfume", hasProperty("perfumer", Matchers.is(TestConstants.PERFUMER))))
                .andExpect(model().attribute("perfume", hasProperty("year", Matchers.is(TestConstants.YEAR))))
                .andExpect(model().attribute("perfume", hasProperty("country", Matchers.is(TestConstants.COUNTRY))))
                .andExpect(model().attribute("perfume", hasProperty("perfumeGender", Matchers.is(TestConstants.PERFUME_GENDER))))
                .andExpect(model().attribute("perfume", hasProperty("fragranceTopNotes", Matchers.is(TestConstants.FRAGRANCE_TOP_NOTES))))
                .andExpect(model().attribute("perfume", hasProperty("fragranceMiddleNotes", Matchers.is(TestConstants.FRAGRANCE_MIDDLE_NOTES))))
                .andExpect(model().attribute("perfume", hasProperty("fragranceBaseNotes", Matchers.is(TestConstants.FRAGRANCE_BASE_NOTES))))
                .andExpect(model().attribute("perfume", hasProperty("filename", Matchers.is(TestConstants.FILENAME))))
                .andExpect(model().attribute("perfume", hasProperty("price", Matchers.is(TestConstants.PRICE))))
                .andExpect(model().attribute("perfume", hasProperty("volume", Matchers.is(TestConstants.VOLUME))))
                .andExpect(model().attribute("perfume", hasProperty("type", Matchers.is(TestConstants.TYPE))));
    }

    @Test
    @DisplayName("[404] GET /admin/perfume/111 - Get Perfume Not Found")
    public void getPerfume_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/perfume/{perfumeId}", 111))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.PERFUME_NOT_FOUND));
    }

    @Test
    @DisplayName("[300] POST /admin/edit/perfume - Edit Perfume")
    public void editPerfume() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/perfume")
                        .file(mockFile())
                        .param("id", String.valueOf(TestConstants.PERFUME_ID))
                        .param("perfumeTitle", TestConstants.PERFUME_TITLE)
                        .param("perfumer", TestConstants.PERFUMER)
                        .param("year", String.valueOf(TestConstants.YEAR))
                        .param("country", TestConstants.COUNTRY)
                        .param("perfumeGender", TestConstants.PERFUME_GENDER)
                        .param("fragranceTopNotes", TestConstants.FRAGRANCE_TOP_NOTES)
                        .param("fragranceMiddleNotes", TestConstants.FRAGRANCE_MIDDLE_NOTES)
                        .param("fragranceBaseNotes", TestConstants.FRAGRANCE_BASE_NOTES)
                        .param("price", String.valueOf(TestConstants.PRICE))
                        .param("volume", TestConstants.VOLUME)
                        .param("type", TestConstants.TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/perfumes"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.PERFUME_EDITED));
    }

    @Test
    @DisplayName("[200] POST /admin/edit/perfume - Edit Perfume Return Input Errors")
    public void editPerfume_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/perfume")
                        .file(mockFile())
                        .param("id", String.valueOf(TestConstants.PERFUME_ID))
                        .param("perfumeTitle", "")
                        .param("perfumer", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("perfumeGender", "")
                        .param("fragranceTopNotes", "")
                        .param("fragranceMiddleNotes", "")
                        .param("fragranceBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_PERFUME))
                .andExpect(model().attribute("perfumeTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("perfumerError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("perfumeGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/add/perfume - Get Add Perfume Page")
    public void getAddPerfumePage() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/add/perfume"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_PERFUME));
    }

    @Test
    @DisplayName("[300] POST /admin/add/perfume - Add Perfume")
    public void addPerfume() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/perfume")
                        .file(mockFile())
                        .param("perfumeTitle", TestConstants.PERFUME_TITLE)
                        .param("perfumer", TestConstants.PERFUMER)
                        .param("year", String.valueOf(TestConstants.YEAR))
                        .param("country", TestConstants.COUNTRY)
                        .param("perfumeGender", TestConstants.PERFUME_GENDER)
                        .param("fragranceTopNotes", TestConstants.FRAGRANCE_TOP_NOTES)
                        .param("fragranceMiddleNotes", TestConstants.FRAGRANCE_MIDDLE_NOTES)
                        .param("fragranceBaseNotes", TestConstants.FRAGRANCE_BASE_NOTES)
                        .param("price", String.valueOf(TestConstants.PRICE))
                        .param("volume", TestConstants.VOLUME)
                        .param("type", TestConstants.TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/perfumes"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.PERFUME_ADDED));
    }

    @Test
    @DisplayName("[200] POST /admin/add/perfume - Add Perfume Return Input Errors")
    public void addPerfume_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/perfume")
                        .file(mockFile())
                        .param("perfumeTitle", "")
                        .param("perfumer", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("perfumeGender", "")
                        .param("fragranceTopNotes", "")
                        .param("fragranceMiddleNotes", "")
                        .param("fragranceBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_PERFUME))
                .andExpect(model().attribute("perfumeTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("perfumerError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("perfumeGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("fragranceBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/user/122 - Get User By Id")
    public void getUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.ADMIN + "/user/{perfumeId}", TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_USER_DETAIL))
                .andExpect(model().attribute("user", hasProperty("id", Matchers.is(TestConstants.USER_ID))))
                .andExpect(model().attribute("user", hasProperty("email", Matchers.is(TestConstants.USER_EMAIL))))
                .andExpect(model().attribute("user", hasProperty("firstName", Matchers.is(TestConstants.USER_FIRST_NAME))))
                .andExpect(model().attribute("user", hasProperty("lastName", Matchers.is(TestConstants.USER_LAST_NAME))))
                .andExpect(model().attribute("user", hasProperty("city", Matchers.is(TestConstants.USER_CITY))))
                .andExpect(model().attribute("user", hasProperty("address", Matchers.is(TestConstants.USER_ADDRESS))))
                .andExpect(model().attribute("user", hasProperty("phoneNumber", Matchers.is(TestConstants.USER_PHONE_NUMBER))))
                .andExpect(model().attribute("user", hasProperty("postIndex", Matchers.is(TestConstants.USER_POST_INDEX))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[404] GET /admin/user/123 - Get User By Id Not Found")
    public void getUserById_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/user/{perfumeId}", 123))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.USER_NOT_FOUND));
    }

    private MockMultipartFile mockFile() throws IOException {
        FileInputStream inputFile = new FileInputStream(new File(TestConstants.FILE_PATH));
        return new MockMultipartFile("file", TestConstants.FILE_NAME, MediaType.MULTIPART_FORM_DATA_VALUE, inputFile);
    }
}
