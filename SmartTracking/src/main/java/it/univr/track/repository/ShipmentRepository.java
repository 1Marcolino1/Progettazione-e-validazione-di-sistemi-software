package it.univr.track.repository;
import java.util.List;
import java.util.Optional;

import it.univr.track.entity.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
}
