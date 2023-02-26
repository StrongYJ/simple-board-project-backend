package com.myproject.simpleboard.domain.shared.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public abstract class File {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String savedName;
    protected String originalName;
}
