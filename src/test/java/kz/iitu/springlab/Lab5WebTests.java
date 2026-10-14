package kz.iitu.springlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Lab5WebTests {

    @Autowired MockMvc mvc;

    @Test
    void restStatusCodes() throws Exception {
        mvc.perform(get("/api/books/1")).andExpect(status().isOk());
        mvc.perform(get("/api/books/999")).andExpect(status().isNotFound());
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"T\",\"author\":\"A\",\"year\":2020}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        mvc.perform(get("/api/books/abc")).andExpect(status().isBadRequest());
        mvc.perform(delete("/api/books")).andExpect(status().isMethodNotAllowed());
        mvc.perform(post("/api/books").contentType(MediaType.TEXT_PLAIN).content("x"))
                .andExpect(status().isUnsupportedMediaType());
        mvc.perform(get("/api/books").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void variant2ByYear() throws Exception {
        mvc.perform(get("/api/books/by-year/2018"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
        mvc.perform(get("/api/books/by-year/18")).andExpect(status().isNotFound());
        mvc.perform(get("/api/books/by-year/abcd")).andExpect(status().isNotFound());
    }

    @Test
    void postRedirectGet() throws Exception {
        mvc.perform(post("/books").param("title", "X").param("author", "Y").param("year", "2000"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("message"));
    }
}
