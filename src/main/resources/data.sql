insert into TESTDB.PUBLIC.CURRENCY (NAME, SYMBOL)
values ('Euro', 'EUR');
insert into TESTDB.PUBLIC.CURRENCY (NAME, SYMBOL)
values ('Złoty', 'PLN');

insert into TESTDB.PUBLIC.EXCHANGE_RATE (START_DATE, END_DATE, EXCHANGE_RATE, CURRENCY_FROM_ID, CURRENCY_TO_ID)
values ('2019-10-20', '2019-11-11', '4.38', 1, 2);
insert into TESTDB.PUBLIC.EXCHANGE_RATE (START_DATE, END_DATE, EXCHANGE_RATE, CURRENCY_FROM_ID, CURRENCY_TO_ID)
values ('2019-10-20', '2019-11-11', '0.2283105', 2, 1);

INSERT INTO TRANSACTION (TRANSACTION_ID, BOOKING_DATE, MAIN_TITLE, AMOUNT, CURRENCY_ID)
VALUES (110, '2019-10-22', 'Paliwo', 100.2222, 2);

