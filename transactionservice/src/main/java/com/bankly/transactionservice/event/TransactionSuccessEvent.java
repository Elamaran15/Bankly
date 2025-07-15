package com.bankly.transactionservice.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionSuccessEvent {

    private UUID transactionId;
    private String transactionStatus;
}
