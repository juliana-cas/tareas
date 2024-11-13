package com.ejemplo.tareas.controller;

import com.ejemplo.tareas.model.Tarea;
import com.ejemplo.tareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaRepository tareaRepository;

    @Autowired
    public TareaController(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    // Listar todas las tareas
    @GetMapping
    public List<Tarea> listarTareas() {
        return tareaRepository.findAll();
    }

    // Agregar una nueva tarea
    @PostMapping
    public ResponseEntity<String> agregarTarea(@RequestBody Tarea tarea) {
        tareaRepository.save(tarea);
        return ResponseEntity.ok("Tarea agregada con éxito.");
    }

    // Actualizar una tarea existente
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTarea(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        return tareaRepository.findById(id)
                .map(tarea -> {
                    tarea.setDescripcion(tareaActualizada.getDescripcion());
                    tareaRepository.save(tarea);
                    return ResponseEntity.ok("Tarea actualizada con éxito.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTarea(@PathVariable Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
            return ResponseEntity.ok("Tarea eliminada con éxito.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Contar el número de tareas
    @GetMapping("/contar")
    public ResponseEntity<Long> contarTareas() {
        Long count = tareaRepository.count();
        return ResponseEntity.ok(count);
    }
}