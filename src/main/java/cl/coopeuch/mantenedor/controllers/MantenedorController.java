package cl.coopeuch.mantenedor.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.StringUtils;
import cl.coopeuch.mantenedor.entities.Tarea;
import cl.coopeuch.mantenedor.services.TareaService;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:4200"})
@RestController
@RequestMapping("/api/mantenedor")
public class MantenedorController {

	@Autowired
	private TareaService tareaService;

	@ApiOperation(value = "Endpoint encargado de listar todas las Tareas", produces = "application/json")
	@GetMapping("/listTasks")
	public ResponseEntity<?> listTasks() {

		try {
			List<Tarea> tareaList = tareaService.findAll();
			return new ResponseEntity<List<Tarea>>(tareaList, HttpStatus.OK);
		} catch (DataAccessException e) {
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("message", "Se ha producido un error al listar Tareas");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Endpoint encargado de crear una Tarea", produces = "application/json")
	@PostMapping("/addTask")
	public ResponseEntity<?> addTask(@RequestBody Tarea tarea) {

		try {
			Tarea taskSaved = this.tareaService.saveTask(tarea);
			return new ResponseEntity<Tarea>(taskSaved, HttpStatus.CREATED);
		} catch (DataAccessException e) {
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("message", "Se ha producido un error al crear Tarea");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Endpoint encargado de remover una Tarea en base a su ID", produces = "application/json")
	@DeleteMapping("/removeTask/{idTask}")
	public ResponseEntity<?> deleteTask(@PathVariable Integer idTask) {
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			if (idTask != null && idTask > 0) {
				this.tareaService.deleteTask(idTask);
				response.put("message", "Tarea eliminada con éxito!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
			}else {
				response.put("message", String.format("Tarea con ID %s no encontrada!", idTask));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}			
		} catch (DataAccessException e) {
			response.put("message", "Se ha producido un error al crear Tarea");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Endpoint encargado de editar una Tarea en base a su ID", produces = "application/json")
	@PutMapping("/editTask/{idTask}")
	public ResponseEntity<?> updateTask(@PathVariable Integer idTask, @RequestBody Tarea taskUpdated) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			Tarea taskSaved = this.tareaService.editTask(idTask, taskUpdated);
			
			if (taskSaved != null) {
				
				return new ResponseEntity<Tarea>(taskSaved, HttpStatus.CREATED);
			} else {
				response.put("message", String.format("Tarea con ID %s no encontrada!", idTask));
				response.put("data", null);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}			
		} catch (DataAccessException e) {
			response.put("message", "Se ha producido un error al actualizar Tarea");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getTask/{idTask}")
	public ResponseEntity<?> getTaskById(@PathVariable Integer idTask) {
		
		try {
			Tarea task = this.tareaService.findById(idTask);
			
			return new ResponseEntity<Tarea>(task, HttpStatus.OK);
			
		} catch (DataAccessException e) {
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("message", String.format("Error al obtener Tarea con ID %s", idTask));
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
