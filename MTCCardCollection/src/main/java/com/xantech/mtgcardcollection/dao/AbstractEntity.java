package com.xantech.mtgcardcollection.dao;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
public class AbstractEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Date createdDate;
    private Date modifiedDate;

    public int compareTo(AbstractEntity abstractEntity) {
        return getCreatedDate().compareTo(abstractEntity.getCreatedDate());
    }
}
