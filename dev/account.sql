--계좌정보(account_type: 1: 수시입출금, 2: 예적금)
INSERT INTO account(account_alias,account_num,account_type,
                    bank_code_std,bank_code_sub,bank_name,
                    fintech_use_num,main,user_id)
VALUES('달달하나',00111111111105,2,081,001,'하나은행',1,true,1),
      ('트래블로그',00122222222811,2,081,020,'하나은행',2,false,1);

--입출금 내역(in_out_type: 0:입금, 1:출금
INSERT INTO transfer(after_balance_amt,branch_name,in_out_type
                    ,printed_content,tran_amt,tran_time,account_id)
VALUES (4500000,'성수역 지점',0,'월급',4500000,'2024-12-20 10:00',1),
    (4450000,'본점',1,'공과금 납부',50000,'2024-12-24 11:30',1),
      (4000000,'성수역 지점',1,'트래블로그 적금',450000,'2024-12-25 13:10',1),
      (450000,'성수역 지점',0,'트래블로그 적금',450000,'2024-12-25 13:10',2);