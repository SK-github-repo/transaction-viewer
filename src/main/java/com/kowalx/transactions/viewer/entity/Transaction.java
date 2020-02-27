package com.kowalx.transactions.viewer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 431209291335285625L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "transaction_id")
    private long transactionId;

    @NonNull
    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @NonNull
    @Column(name = "main_title")
    private String mainTitle;

    @NonNull
    private BigDecimal amount;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency currency;
}
