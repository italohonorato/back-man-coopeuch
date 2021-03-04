package cl.coopeuch.mantenedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.coopeuch.mantenedor.entities.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {

}
