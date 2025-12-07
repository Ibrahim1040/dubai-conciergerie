package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public List<Property> getPropertiesForOwner(User owner) {
        return propertyRepository.findAll()
                .stream()
                .filter(p -> p.getOwner() != null && p.getOwner().getId().equals(owner.getId()))
                .toList();
    }

    @Override
    public List<Property> getAll() {
        return List.of();
    }

    // ---------------------------------------------------------------------
    // CRÉATION D’UN LOGEMENT
    // ---------------------------------------------------------------------
    @Override
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property getById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Property not found with id " + id));
    }

    @Override
    public List<Property> getAllPublicProperties() {
        return List.of();
    }

    @Override
    public Property getPropertyForOwner(Long propertyId, User owner) {
        return null;
    }

    // ---------------------------------------------------------------------
    // RÉCUPÉRATION D’UN LOGEMENT PARTICULIER
    // ---------------------------------------------------------------------
    @Override
    public Property getOwnerProperty(Long ownerId, Long propertyId) {
        Property property = getById(propertyId);
        if (property.getOwner() == null ||
                !property.getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException(
                    "Property " + propertyId + " not found for owner " + ownerId
            );
        }
        return property;
    }


    // ---------------------------------------------------------------------
    // MISE À JOUR D’UN LOGEMENT
    // ---------------------------------------------------------------------
    @Override
    public Property updateOwnerProperty(Long ownerId, Long propertyId, Property updates) {
        Property existing = getOwnerProperty(ownerId, propertyId);

        existing.setTitle(updates.getTitle());
        existing.setCity(updates.getCity());
        existing.setAddress(updates.getAddress());
        existing.setCapacity(updates.getCapacity());
        existing.setRentalType(updates.getRentalType());
        existing.setNightlyPrice(updates.getNightlyPrice());
        existing.setMonthlyPrice(updates.getMonthlyPrice());
        // on ne change pas l’owner ici

        return propertyRepository.save(existing);
    }

      // ---------------------------- //
     // SUPPRESSION D’UN LOGEMENT    //
    // ---------------------------  //
    @Override
    public void deleteOwnerProperty(Long ownerId, Long propertyId) {
        Property existing = getOwnerProperty(ownerId, propertyId);
        propertyRepository.delete(existing);
    }
}
