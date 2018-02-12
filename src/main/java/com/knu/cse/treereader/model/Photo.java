package com.knu.cse.treereader.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//모델은 담아놓는애

@Data // lombok
@Entity
public class Photo {
    @Id
    @GeneratedValue
    private Integer id;

    private String photoPath;

    private String result;
}
