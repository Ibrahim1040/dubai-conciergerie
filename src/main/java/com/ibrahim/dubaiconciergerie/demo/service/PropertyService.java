package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.PropertyDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

import java.util.List;

public interface PropertyService {

    List<Property> getAll();

    Property create(PropertyDto dto);

    Property getById(Long id);

    Property update(Long id, PropertyDto dto);

    void delete(Long id);

    List<Property> getByOwner(User owner);

    List<Property> getAllPublicProperties();

    Property createProperty(Property property);

    List<Property> getPropertiesForOwner(User owner);

}
