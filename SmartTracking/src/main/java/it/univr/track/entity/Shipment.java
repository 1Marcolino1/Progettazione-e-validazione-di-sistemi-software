package it.univr.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Shipment extends AbstractEntity {

    public Shipment(String shipmentName) {
        name = shipmentName;
    }

    @GeneratedValue(strategy=GenerationType.AUTO)
    private String name;
    private boolean isTracked;


    @OneToMany
    private List<Device> devices;
}
