package com.ejemplo.tareas;

import com.ejemplo.tareas.controller.TareaController;
import com.ejemplo.tareas.model.Tarea;
import com.ejemplo.tareas.repository.TareaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TareaControllerTest {

	@InjectMocks
	private TareaController tareaController;

	@Mock
	private TareaRepository tareaRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarTareas() {
		// Arrange
		Tarea tarea1 = new Tarea();
		tarea1.setId(1L);
		tarea1.setDescripcion("Tarea 1");

		Tarea tarea2 = new Tarea();
		tarea2.setId(2L);
		tarea2.setDescripcion("Tarea 2");

		when(tareaRepository.findAll()).thenReturn(Arrays.asList(tarea1, tarea2));

		// Act
		List<Tarea> tareas = tareaController.listarTareas();

		// Assert
		assertEquals(2, tareas.size());
		assertEquals("Tarea 1", tareas.get(0).getDescripcion());
		assertEquals("Tarea 2", tareas.get(1).getDescripcion());
	}

	@Test
	void testAgregarTarea() {
		// Arrange
		Tarea nuevaTarea = new Tarea();
		nuevaTarea.setDescripcion("Nueva tarea");

		when(tareaRepository.save(any(Tarea.class))).thenReturn(nuevaTarea);

		// Act
		ResponseEntity<String> response = tareaController.agregarTarea(nuevaTarea);

		// Assert
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Tarea agregada con éxito.", response.getBody());

		ArgumentCaptor<Tarea> tareaCaptor = ArgumentCaptor.forClass(Tarea.class);
		verify(tareaRepository, times(1)).save(tareaCaptor.capture());
		assertEquals("Nueva tarea", tareaCaptor.getValue().getDescripcion());
	}

	@Test
	void testActualizarTarea() {
		// Arrange
		Tarea tareaExistente = new Tarea();
		tareaExistente.setId(1L);
		tareaExistente.setDescripcion("Tarea existente");

		when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaExistente));

		Tarea tareaActualizada = new Tarea();
		tareaActualizada.setId(1L);
		tareaActualizada.setDescripcion("Tarea actualizada");

		when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaActualizada);

		// Act
		ResponseEntity<String> response = tareaController.actualizarTarea(1L, tareaActualizada);

		// Assert
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Tarea actualizada con éxito.", response.getBody());

		ArgumentCaptor<Tarea> tareaCaptor = ArgumentCaptor.forClass(Tarea.class);
		verify(tareaRepository, times(1)).save(tareaCaptor.capture());
		assertEquals("Tarea actualizada", tareaCaptor.getValue().getDescripcion());
	}

	@Test
	void testEliminarTarea() {
		// Arrange
		Long tareaId = 1L;
		when(tareaRepository.existsById(tareaId)).thenReturn(true);
		doNothing().when(tareaRepository).deleteById(tareaId);

		// Act
		ResponseEntity<String> response = tareaController.eliminarTarea(tareaId);

		// Assert
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Tarea eliminada con éxito.", response.getBody());

		verify(tareaRepository, times(1)).deleteById(tareaId);
	}

	@Test
	void testEliminarTareaNoExistente() {
		// Arrange
		Long tareaId = 1L;
		when(tareaRepository.existsById(tareaId)).thenReturn(false);

		// Act
		ResponseEntity<String> response = tareaController.eliminarTarea(tareaId);

		// Assert
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	void testContarTareas() {
		// Arrange
		when(tareaRepository.count()).thenReturn(3L);

		// Act
		ResponseEntity<Long> response = tareaController.contarTareas();

		// Assert
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(3L, response.getBody());
	}
}