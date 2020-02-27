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
@Entity(name = "EXCHANGE_RATE")
@NoArgsConstructor
@RequiredArgsConstructor
public class ExchangeRate implements Serializable {

    private static final long serialVersionUID = -3371806866693392180L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NonNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NonNull
    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency currencyFrom;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Currency currencyTo;
}
