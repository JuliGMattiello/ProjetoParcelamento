package bethaCode.javaspringideaparcelamentoonLine.resource;

import bethaCode.javaspringideaparcelamentoonLine.enterprise.ValidationException;
import bethaCode.javaspringideaparcelamentoonLine.model.Pais;
import bethaCode.javaspringideaparcelamentoonLine.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/paises")
public class PaisController extends AbstractResource{

    @Autowired
    private PaisRepository repository;

    @GetMapping
    public List<Pais> getPaises(){
        return repository.findAll();
    }
//    public List<PaisDTO> getPaises(){
//        return repository.findAll().stream().map(p-> PaisDTO.toDTO(p)).collect(Collectors.toList());
//    }

    @GetMapping("/{id}")
    public Pais getPaisesId(@PathVariable(value = "id") Long paisId, @RequestBody Pais pais) throws EntityNotFoundException {
        Pais paisFind = repository.findById(paisId)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado com ID: " + paisId));
        return paisFind;
    }
//    public PaisDTO getPaisesId(@PathVariable(value = "id") Long paisId) throws EntityNotFoundException {
//
//        Pais paisFind = repository.findById(paisId)
//                .orElseThrow(() -> new EntityNotFoundException("País não encontrado com ID: " + paisId));
//
//        return PaisDTO.toDTO(paisFind);
//    }

    @PostMapping
    public Pais create(@Valid @RequestBody Pais pais) throws ValidationException{

        List<Pais> byNome = repository.findByNome(pais.getNome());

        if (!byNome.isEmpty()){
            throw new ValidationException("Já existe um país com o mesmo nome!");
        }

        return repository.save(pais);
    }
//    public PaisDTO create(@Valid @RequestBody Pais pais){
//        return PaisDTO.toDTO(repository.save(pais));
//    }


    @PutMapping("/{id}")
    public Pais update(@PathVariable(value = "id") Long paisId,
                          @RequestBody Pais pais) throws EntityNotFoundException, ValidationException{
        Pais paisFind = repository.findById(paisId)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado com ID: " + paisId));
        paisFind.setId(pais.getId());
        paisFind.setNome(pais.getNome());

        List<Pais> byNome = repository.findByNome(pais.getNome());

        if (!byNome.isEmpty()){
            throw new ValidationException("Já existe um país com o mesmo nome!");
        }

        return repository.save(paisFind);

    }
//    public PaisDTO update(@PathVariable(value = "id") Long paisId,
//                          @RequestBody Pais pais) throws EntityNotFoundException {
//        Pais paisFind = repository.findById(paisId)
//                .orElseThrow(() -> new EntityNotFoundException("País não encontrado com ID: " + paisId));
//        paisFind.setId(pais.getId());
//        paisFind.setNome(pais.getNome());
//
//        return PaisDTO.toDTO(repository.save(paisFind));
//
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long paisId) throws EntityNotFoundException{
        Pais paisFind = repository.findById(paisId)
                .orElseThrow(() -> new EntityNotFoundException("País não encontrado com ID: " + paisId));

        repository.delete(paisFind);
        return ResponseEntity.noContent().build();
    }

}