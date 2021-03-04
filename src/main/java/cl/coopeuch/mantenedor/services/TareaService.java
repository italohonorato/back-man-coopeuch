package cl.coopeuch.mantenedor.services;

import java.util.List;

import cl.coopeuch.mantenedor.entities.Tarea;

public interface TareaService {

	public Tarea saveTask(Tarea tarea);
	public Tarea editTask(Integer idTask, Tarea taskUpdated);
	public void deleteTask(Integer idTask);
	public List<Tarea> findAll();
	public Tarea findById(Integer idTask);
}
