package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.PropertyDto;
import com.ibrahim.dubaiconciergerie.demo.dto.PropertyMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.repository.UserRepository;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepo;
    private final UserRepository userRepo;

    public PropertyServiceImpl(PropertyRepository propertyRepo, UserRepository userRepo) {
        this.propertyRepo = propertyRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<Property> getAll() {
        return propertyRepo.findAll();
    }

    @Override
    public Property create(PropertyDto dto) {

        Property property = PropertyMapper.toEntity(dto);

        // Récupération du propriétaire
        User owner = userRepo.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        property.setOwner(owner);

        return propertyRepo.save(property);
    }

    @Override
    public Property getById(Long id) {
        return propertyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public Property update(Long id, PropertyDto dto) {

        Property existing = getById(id);

        existing.setTitle(dto.getTitle());
        existing.setCity(dto.getCity());
        existing.setAddress(dto.getAddress());
        existing.setCapacity(dto.getCapacity());

        existing.setRentalType(Property.RentalType.valueOf(
                dto.getRentalType().toUpperCase()
        ));

        existing.setNightlyPrice(dto.getNightlyPrice());
        existing.setMonthlyPrice(dto.getMonthlyPrice());

        // changer de propriétaire (optionnel)
        if (dto.getOwnerId() != null) {
            User newOwner = userRepo.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existing.setOwner(newOwner);
        }

        return propertyRepo.save(existing);
    }

    @Override
    public void delete(Long id) {
        propertyRepo.deleteById(id);
    }

    @Override
    public List<Property> getByOwner(User owner) {
        return propertyRepo.findByOwner(owner);
    }

    @Override
    public List<Property> getAllPublicProperties() {
        return propertyRepo.findAll();
    }

    @Override
    public List<Property> getPropertiesForOwner(User owner) {
        return propertyRepo.findByOwner(owner);
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepo.save(property);
    }


}
