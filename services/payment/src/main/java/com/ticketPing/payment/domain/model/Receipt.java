package com.ticketPing.payment.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
public class Receipt {

    @Id
    private UUID receiptId;



    @PrePersist
    protected void createUUID(){
        if(receiptId == null) receiptId = UUID.randomUUID();
    }

}
