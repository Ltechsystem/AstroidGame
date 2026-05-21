package dk.sdu.mmmi.cbse.scoreservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreRepository repository;

    @Test
    void getAllScores_returnsEmptyJsonArray_whenNoScores() throws Exception {
        when(repository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/scores"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllScores_returnsAllScores() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
                new Score("lars", 10),
                new Score("bo", 20)
        ));

        mockMvc.perform(get("/scores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getScore_returns200WithScore_whenPlayerExists() throws Exception {
        when(repository.findByName("bo")).thenReturn(new Score("bo", 42));

        mockMvc.perform(get("/scores/bo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("bo"))
                .andExpect(jsonPath("$.points").value(42));
    }

    @Test
    void getScore_returns404_whenPlayerNotFound() throws Exception {
        when(repository.findByName("unknown")).thenReturn(null);

        mockMvc.perform(get("/scores/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postScore_returns200WithSavedScore() throws Exception {
        when(repository.save("lars", 99)).thenReturn(new Score("lars", 99));

        mockMvc.perform(post("/scores/lars").param("points", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("lars"))
                .andExpect(jsonPath("$.points").value(99));
    }
}