package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> getAllPublicProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public List<Property> getPropertiesForOwner(User owner) {
        return propertyRepository.findByOwner(owner);
    }

    @Override
    public Property updateProperty(Long id, Property updated) {
        Property existing = getPropertyById(id);
        existing.setTitle(updated.getTitle());
        existing.setCity(updated.getCity());
        existing.setAddress(updated.getAddress());
        existing.setCapacity(updated.getCapacity());
        existing.setRentalType(updated.getRentalType());
        existing.setNightlyPrice(updated.getNightlyPrice());
        existing.setMonthlyPrice(updated.getMonthlyPrice());
        return propertyRepository.save(existing);
    }

    @Override
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }


}
