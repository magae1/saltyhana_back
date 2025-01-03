--계좌정보(account_type: 1: 수시입출금, 2: 예적금)
INSERT INTO account(account_alias,account_num,account_type,
                    bank_code_std,bank_code_sub,bank_name,
                    fintech_use_num,main,user_id)
VALUES('달달하나',00111111111105,2,081,001,'하나은행',1,true,1),
      ('트래블로그',00122222222811,2,081,020,'하나은행',2,false,1),
      ('수퍼플러스',00133333333305,1,081,001,'하나은행',3,false,1),
      ('하나의 정기예금',00144444444405,2,081,020,'하나은행',4,false,1)
      ;
--입출금 내역(in_out_type: 0:입금, 1:출금
INSERT INTO transfer(after_balance_amt,branch_name,in_out_type
                    ,printed_content,tran_amt,tran_time,account_id)
VALUES (4500000,'성수역 지점',0,'월급',4500000,'2024-12-20 10:00',1),
    (4450000,'본점',1,'공과금 납부',50000,'2024-12-24 11:30',1),
      (4000000,'성수역 지점',1,'트래블로그 적금',450000,'2024-12-25 13:10',1),
      (450000,'성수역 지점',0,'트래블로그 적금',450000,'2024-12-25 13:10',2),
        (4000000,null,1,'수퍼플러스',500000,'2024-12-25 14:00',1),
        (500000,null,0,'수퍼플러스',500000,'2024-12-25 14:00',3),
        (430000,null,1,'트래블로그 연회비 인출',20000,'2024-12-25 14:27',2),
        (3000000,null,1,'하나의 정기예금',1000000,'2024-12-25 15:55',1),
        (1000000,null,0,'하나의 정기예금',1000000,'2024-12-25 15:55',4),
        (2850000,null,1,'안유진 콘서트 티켓 예매',150000,'2024-12-25 15:58',1),
        (3000000,null,0,'안유진 콘서트 티켓 환불',150000,'2024-12-26 09:15',1),
        (3675000,null,0,'연말정산',675000,'2024-12-26 14:00',1),
        (3300000,null,1,'무신사 결제',375000,'2024-12-26 14:37',1),
        (3187000,null,1,'핸드폰 요금 납부',113000,'2024-12-26 15:13',1),
        (0,null,1,'수퍼플러스 전액 출금',500000,'2024-12-27 11:11',3),
        (930000,null,0,'수퍼플러스 전액 입금',500000,'2024-12-27 11:11',2),
        (3117000,null,1,'트래블로그 적금',70000,'2024-12-27 14:15',1),
        (1000000,null,0,'트래블로그 적금',70000,'2024-12-27 14:15',2),
        (2797000,null,1,'애플 에어팟 결제',320000,'2024-12-27 15:47',1),
        (2647000,null,1,'안유진 콘서트 티켓 예매',150000,'2024-12-28 09:17',1),
        (2600000,null,1,'응원봉 구매',47000,'2024-12-28 09:43',1),
        (4600000,null,0,'교통사고 합의금',2000000,'2024-12-28 14:18',1),
        (3600000,null,1,'수퍼플러스',1000000,'2024-12-28 15:49',1),
        (1000000,null,0,'수퍼플러스',1000000,'2024-12-28 15:49',3),
        (3450000,null,1,'교통대금 결제',150000,'2024-12-29 11:23',1)
;