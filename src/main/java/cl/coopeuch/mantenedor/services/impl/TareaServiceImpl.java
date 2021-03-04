package cl.coopeuch.mantenedor.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.coopeuch.mantenedor.entities.Tarea;
import cl.coopeuch.mantenedor.repositories.TareaRepository;
import cl.coopeuch.mantenedor.services.TareaService;

@Service
public class TareaServiceImpl implements TareaService {

	private TareaRepository repository;

	@Autowired
	public TareaServiceImpl(TareaRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Tarea saveTask(Tarea tarea) {

		return this.repository.save(tarea);
	}

	@Override
	@Transactional
	public void deleteTask(Integer idTask) {

		Optional<Tarea> taskToDelete = this.repository.findById(idTask);

		if (taskToDelete.isPresent()) {
			this.repository.delete(taskToDelete.get());
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<Tarea> findAll() {

		return this.repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Tarea findById(Integer idTask) {

		return this.repository.findById(idTask).orElse(null);
	}

	@Override
	public Tarea editTask(Integer idTask, Tarea taskUpdated) {
		Tarea originalTask = this.repository.findById(idTask).orElse(null);
		
		if (originalTask != null) {
			originalTask.setDescripcion(taskUpdated.getDescripcion());
			originalTask.setVigente(taskUpdated.isVigente());
			return this.repository.save(originalTask);
		}
		
		return  null;
	}

}
