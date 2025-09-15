package org.iplacex.proyectos.discografia.discos;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

    private final IDiscoRepository discoRepo;
    private final IArtistaRepository artistaRepo;

    @Autowired
    public DiscoController(IDiscoRepository discoRepo, IArtistaRepository artistaRepo) {
        this.discoRepo = discoRepo;
        this.artistaRepo = artistaRepo;
    }

    //Crear
    @PostMapping(
        value = "/discos", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandlePostDiscoRequest(@RequestBody Disco disco) {
        //Validacion previa: el artista existe?
        if (disco.idArtista == null || !artistaRepo.existsById(disco.idArtista)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El artista (idArtista) no existe. No se puede crear el disco.");
        }
        Disco saved = discoRepo.save(disco);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //Leer todos
    @GetMapping(
        value = "/discos", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscoRequest() {
        List<Disco> all = discoRepo.findAll();
        return ResponseEntity.ok(all);
    }

    //Leer uno
    @GetMapping(
        value = "/discos/{id}", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscoRequest(@PathVariable("id") String id) {
        Optional<Disco> found = discoRepo.findById(id);
        return found.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el disco con id: " + id));
    }

    //Leer por artista
    @GetMapping(
        value = "/artistas/{id}/discos", 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscosPorArtistaRequest(@PathVariable("id") String idArtista) {
        List<Disco> byArtist = discoRepo.findDiscosByIdArtista(idArtista);
        return ResponseEntity.ok(byArtist);        
    }

}
