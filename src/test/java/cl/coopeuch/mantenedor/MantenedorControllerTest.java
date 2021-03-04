package cl.coopeuch.mantenedor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.coopeuch.mantenedor.controllers.MantenedorController;
import cl.coopeuch.mantenedor.entities.Tarea;
import cl.coopeuch.mantenedor.services.TareaService;

@WebMvcTest(MantenedorController.class)
class MantenedorControllerTest {

	@Autowired
	private MantenedorController controller;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TareaService tareaService;
	
	@Test
	void testFindById() throws Exception {
		
		Tarea task = new Tarea(1, "TASK 1", new Date(), true);
		
		when(tareaService.findById(anyInt())).thenReturn(task);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/mantenedor/getTask/1"))
		.andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value("TASK 1"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.vigente").value(true))
		.andExpect(status().isOk());
	}

	@Test
	void testListTasks() throws Exception {
		List<Tarea> taskList = new ArrayList<>();
		Tarea task1 = new Tarea(1, "TASK 1", new Date(), true);
		Tarea task2 = new Tarea(2, "TASK 2", new Date(), true);
		Tarea task3 = new Tarea(3, "TASK 3", new Date(), true);
		
		taskList.add(task1);
		taskList.add(task2);
		taskList.add(task3);
		
		when(tareaService.findAll()).thenReturn(taskList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/mantenedor/listTasks"))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
	}
	
	@Test
	void testAddTask() throws Exception {
		Tarea task =  new Tarea();
		task.setId(1);
		task.setDescripcion("TASK 1");
		task.setVigente(true);
		
		when(tareaService.saveTask(any(Tarea.class))).thenReturn(task);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/mantenedor/addTask")
				.content(new ObjectMapper().writeValueAsString(task))
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value("TASK 1"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.vigente").value(true));
	}
	
	@Test
	void testUpdateTask() throws Exception {		
		Tarea taskUpdated =  new Tarea();
		taskUpdated.setId(1);
		taskUpdated.setDescripcion("TASK 1 UPDATED");
		taskUpdated.setVigente(false);
		
		when(tareaService.editTask(anyInt(), any(Tarea.class))).thenReturn(taskUpdated);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/mantenedor/editTask/{idTask}", 1)
				.content(new ObjectMapper().writeValueAsString(taskUpdated))
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value("TASK 1 UPDATED"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.vigente").value(false));
	}
	
	
	@Test
	void testDeleteTask() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/mantenedor/removeTask/{idTask}", 1).contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isNoContent());
	}
}
