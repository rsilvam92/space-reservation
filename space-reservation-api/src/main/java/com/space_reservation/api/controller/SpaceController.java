package com.space_reservation.api.controller;

import com.space_reservation.api.dto.request.SpaceRequestDTO;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.service.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public Space create(@Valid @RequestBody SpaceRequestDTO dto) {
        return spaceService.createSpace(dto);
    }

    @PutMapping("/{id}")
    public Space update(
            @PathVariable Long id,
            @Valid @RequestBody SpaceRequestDTO dto
    ) {
        return spaceService.updateSpace(id, dto);
    }

    @GetMapping
    public List<Space> getAll() {
        return spaceService.getAll();
    }

    @GetMapping("/type/{type}")
    public List<Space> getByType(@PathVariable SpaceType type) {
        return spaceService.getByType(type);
    }
}
