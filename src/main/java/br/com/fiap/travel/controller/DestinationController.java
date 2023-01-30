package br.com.fiap.travel.controller;

import br.com.fiap.travel.dto.DestinationCreateUpdateDTO;
import br.com.fiap.travel.dto.DestinationDTO;
import br.com.fiap.travel.dto.DestinationPriceDTO;
import br.com.fiap.travel.dto.DestinationSimpleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("destinations")
public class DestinationController {

    private List<DestinationDTO> destinationDTOList = new ArrayList<>();

    public DestinationController() {
        destinationDTOList.addAll(
            Arrays.asList(
                new DestinationDTO(1L, "Disney", "Orlando", "EUA", "Terra do Mickey", BigDecimal.valueOf(3_000L), "BRL"),
                new DestinationDTO(2L, "Porto Seguro", "Orlando", "Brasil", "Terra do Mickey", BigDecimal.valueOf(3_000L), "BRL"),
                new DestinationDTO(3L, "Santiago", "Orlando", "Chile", "Terra do Mickey", BigDecimal.valueOf(3_000L), "BRL"),
                new DestinationDTO(4L, "Bangkok", "Orlando", "Tailândia", "Terra do Mickey", BigDecimal.valueOf(3_000L), "BRL")
            )
        );
    }

    @GetMapping
    public List<DestinationSimpleDTO> listAll(@RequestParam(required = false) String country) {
        return destinationDTOList.stream()
                .filter(destinationDTO -> country == null || destinationDTO.country().equals(country))
                .map(destinationDTO -> new DestinationSimpleDTO(destinationDTO.id(), destinationDTO.name(), destinationDTO.country()))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public DestinationDTO findById(@PathVariable Long id) {
        return destinationDTOList.stream()
                .filter(destinationDTO -> destinationDTO.id() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DestinationDTO create(@RequestBody DestinationCreateUpdateDTO destinationCreateUpdateDTO) {
        Long id = destinationDTOList.get(destinationDTOList.size() - 1).id() + 1L;
        DestinationDTO newDestination = new DestinationDTO(id, destinationCreateUpdateDTO.name(), destinationCreateUpdateDTO.airport(), destinationCreateUpdateDTO.country(), destinationCreateUpdateDTO.description(), destinationCreateUpdateDTO.price(), "BRL");
        destinationDTOList.add(newDestination);
        return newDestination;
    }

    @PutMapping("{id}")
    public DestinationDTO update(@PathVariable Long id,
                                 @RequestBody DestinationCreateUpdateDTO destinationCreateUpdateDTO) {
        DestinationDTO destinationDTO = findById(id);
        destinationDTOList.remove(destinationDTO);
        DestinationDTO updatedDestination = new DestinationDTO(
                destinationDTO.id(),
                destinationCreateUpdateDTO.name(),
                destinationCreateUpdateDTO.airport(),
                destinationCreateUpdateDTO.country(),
                destinationCreateUpdateDTO.description(),
                destinationCreateUpdateDTO.price(),
                destinationDTO.currency());

        destinationDTOList.add(updatedDestination);

        return updatedDestination;
    }

    @PatchMapping("{id}")
    public DestinationDTO updatePrice(@PathVariable Long id,
                                      @RequestBody DestinationPriceDTO destinationPriceDTO){
        DestinationDTO destinationDTO = findById(id);
        destinationDTOList.remove(destinationDTO);
        DestinationDTO updatedDestination = new DestinationDTO(
                destinationDTO.id(),
                destinationDTO.name(),
                destinationDTO.airport(),
                destinationDTO.country(),
                destinationDTO.description(),
                destinationPriceDTO.price(),
                destinationDTO.currency());
        destinationDTOList.add(updatedDestination);
        return updatedDestination;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        DestinationDTO destinationDTO = findById(id);
        destinationDTOList.remove(destinationDTO);
    }
}
