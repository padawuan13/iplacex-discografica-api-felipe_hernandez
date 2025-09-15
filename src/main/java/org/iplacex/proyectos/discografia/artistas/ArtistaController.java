package org.iplacex.proyectos.discografia.artistas;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class ArtistaController {

    private final IArtistaRepository repo;

    @Autowired
    public ArtistaController(IArtistaRepository repo) {
        this.repo = repo;
    }

    // Métodos para manejar las peticiones HTTP
    @PostMapping(
        value = "/artistas",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista artista) {
        Artista saved = repo.save(artista);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(
        value = "/artistas",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Artista>> HandleGetArtistasRequest() {
        List<Artista> all = repo.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping(
        value = "/artistas/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetArtistaRequest(@PathVariable("id") String id) {
        Optional<Artista> found = repo.findById(id);
        return found.<ResponseEntity<Object>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el artista con id: " + id));
    }

    @PutMapping(
        value = "/artistas/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleUpdateArtistaRequest(@PathVariable("id") String id, @RequestBody Artista artista) {

        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe artista con id: " + id);
        }
        artista._id = id;
        Artista updated = repo.save(artista);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(
        value = "/artistas/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable("id") String id) {
        Optional<Artista> found = repo.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe artista con id: " + id);
        }
        repo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}    
