package cl.coopeuch.mantenedor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import cl.coopeuch.mantenedor.entities.Tarea;
import cl.coopeuch.mantenedor.repositories.TareaRepository;

@DataJpaTest
class RepositoryTests {
	
	@Autowired
	private TareaRepository repository;
	
	@Test
	void testCreateTarea() {
		Tarea taskSaved = repository.save(new  Tarea(1, "TASK 1", new Date(), true));
		
		assertNotNull(taskSaved);
		assertTrue(taskSaved.getId() > 0);
	}

	@Test
	@Sql("/test-data.sql")
	void testFindAllTasks() {
		List<Tarea> allTasks = repository.findAll();
		
		assertNotNull(allTasks);
		assertEquals(4, allTasks.size());
	}
	
	@Test
	@Sql("/test-data.sql")
	void testFindById() {
		Tarea task = repository.findById(3).orElse(null);
		
		assertNotNull(task);
		assertEquals("TASK 3", task.getDescripcion());
	}
	
	@Test
	@Sql("/test-data.sql")
	void testRemove() {		
		repository.deleteById(1);
		Tarea task = repository.findById(1).orElse(null);
		
		assertThat(task).isNull();
	}
	
}
