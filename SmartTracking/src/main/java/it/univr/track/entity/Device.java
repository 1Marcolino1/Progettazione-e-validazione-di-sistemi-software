package it.univr.track.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Device extends AbstractEntity {

    private String name;

    public Device(String name) {
        this.name = name;
    }

    @ManyToOne
    private Shipment shipment;
}
