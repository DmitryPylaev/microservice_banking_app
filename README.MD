[![codecov](https://codecov.io/gh/PylaevDmitry/microservice_banking_project/graph/badge.svg?token=EG9B6BUKB7)](https://codecov.io/gh/PylaevDmitry/microservice_banking_project)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=bugs)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=PylaevDmitry_microservice_banking_project&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=PylaevDmitry_microservice_banking_project)

Приложение с микросервисной архитектурой - прототип банка, разработал Пылаев Дмитрий. 
Выполнено с приминением технологий и инструментов:
Java, SpringBoot, PostgreSQL, JPA, Swagger, Kafka, JUnit, Lombok.

Микросервис conveyor - реализация кредитного конвейра.
Микросервис deal - работа с БД, запросы к КК.
Микросервис application - работа с заявками на прескоринг.
Микросервис dossier - асинхронная отправка электронных писем клиенту - уведомлений о ходе сделки.
Микросервис gateway - клиентский API, реализация паттерна API-Gateway.

Описание API:
Микросервис conveyor
POST: /conveyor/offers - расчёт возможных условий кредита

Пример запроса:
    body json:
    {
    "amount": 300000,
    "term": 18,
    "firstName": "Vasiliy",
    "lastName": "Petrov",
    "middleName": "",
    "email": "vas@gmail.com",
    "birthdate": "1977-08-16",
    "passportSeries": "5766",
    "passportNumber": "576687"
    }

Пример ответа:
    [
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18644.92,
    "rate": 14.5,
    "isInsuranceEnabled": false,
    "isSalaryClient": true
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18574.55,
    "rate": 14,
    "isInsuranceEnabled": true,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18504.34,
    "rate": 13.5,
    "isInsuranceEnabled": true,
    "isSalaryClient": true
    }
    ]

POST: /conveyor/calculation - валидация присланных данных + полный расчет параметров кредита

Пример запроса:
    body:
    {
    "gender": "MALE",
    "maritalStatus": "MARRIED",
    "dependentAmount": 1,
    "passportIssueDate": "2023-09-05",
    "passportIssueBranch": "UFMS",
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12
    },
    "account": "account"
    }

Пример ответа:
    {
    "gender": "MALE",
    "maritalStatus": "MARRIED",
    "dependentAmount": 1,
    "passportIssueDate": "2023-09-05",
    "passportIssueBranch": "UFMS",
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12
    },
    "account": "account"
    }
    
    {
    "amount": 300000,
    "term": 18,
    "monthlyPayment": 18294.62,
    "rate": 12,
    "psk": 6.5118,
    "isInsuranceEnabled": false,
    "isSalaryClient": false,
    "paymentSchedule": [
    {
    "number": 1,
    "date": "2023-10-05",
    "totalPayment": 18294.62,
    "interestPayment": 3000,
    "debtPayment": 15294.62,
    "remainingDebt": 284705.38
    },
    {
    "number": 2,
    "date": "2023-11-05",
    "totalPayment": 18294.62,
    "interestPayment": 2847.054,
    "debtPayment": 15447.57,
    "remainingDebt": 269257.81
    },
    {
    "number": 3,
    "date": "2023-12-05",
    "totalPayment": 18294.62,
    "interestPayment": 2692.578,
    "debtPayment": 15602.04,
    "remainingDebt": 253655.77
    },
    {
    "number": 4,
    "date": "2024-01-05",
    "totalPayment": 18294.62,
    "interestPayment": 2536.558,
    "debtPayment": 15758.06,
    "remainingDebt": 237897.71
    },
    {
    "number": 5,
    "date": "2024-02-05",
    "totalPayment": 18294.62,
    "interestPayment": 2378.977,
    "debtPayment": 15915.64,
    "remainingDebt": 221982.07
    },
    {
    "number": 6,
    "date": "2024-03-05",
    "totalPayment": 18294.62,
    "interestPayment": 2219.821,
    "debtPayment": 16074.8,
    "remainingDebt": 205907.27
    },
    {
    "number": 7,
    "date": "2024-04-05",
    "totalPayment": 18294.62,
    "interestPayment": 2059.073,
    "debtPayment": 16235.55,
    "remainingDebt": 189671.72
    },
    {
    "number": 8,
    "date": "2024-05-05",
    "totalPayment": 18294.62,
    "interestPayment": 1896.717,
    "debtPayment": 16397.9,
    "remainingDebt": 173273.82
    },
    {
    "number": 9,
    "date": "2024-06-05",
    "totalPayment": 18294.62,
    "interestPayment": 1732.738,
    "debtPayment": 16561.88,
    "remainingDebt": 156711.94
    },
    {
    "number": 10,
    "date": "2024-07-05",
    "totalPayment": 18294.62,
    "interestPayment": 1567.119,
    "debtPayment": 16727.5,
    "remainingDebt": 139984.44
    },
    {
    "number": 11,
    "date": "2024-08-05",
    "totalPayment": 18294.62,
    "interestPayment": 1399.844,
    "debtPayment": 16894.78,
    "remainingDebt": 123089.66
    },
    {
    "number": 12,
    "date": "2024-09-05",
    "totalPayment": 18294.62,
    "interestPayment": 1230.897,
    "debtPayment": 17063.72,
    "remainingDebt": 106025.94
    },
    {
    "number": 13,
    "date": "2024-10-05",
    "totalPayment": 18294.62,
    "interestPayment": 1060.259,
    "debtPayment": 17234.36,
    "remainingDebt": 88791.58
    },
    {
    "number": 14,
    "date": "2024-11-05",
    "totalPayment": 18294.62,
    "interestPayment": 887.9158,
    "debtPayment": 17406.7,
    "remainingDebt": 71384.88
    },
    {
    "number": 15,
    "date": "2024-12-05",
    "totalPayment": 18294.62,
    "interestPayment": 713.8488,
    "debtPayment": 17580.77,
    "remainingDebt": 53804.11
    },
    {
    "number": 16,
    "date": "2025-01-05",
    "totalPayment": 18294.62,
    "interestPayment": 538.0411,
    "debtPayment": 17756.58,
    "remainingDebt": 36047.53
    },
    {
    "number": 17,
    "date": "2025-02-05",
    "totalPayment": 18294.62,
    "interestPayment": 360.4753,
    "debtPayment": 17934.14,
    "remainingDebt": 18113.39
    },
    {
    "number": 18,
    "date": "2025-03-05",
    "totalPayment": 18294.62,
    "interestPayment": 181.1339,
    "debtPayment": 18113.49,
    "remainingDebt": -0.1
    }
    ]
    }

Микросервис deal
POST: /deal/application - расчёт возможных условий кредита

Пример запроса:
    body json:
    {
    "amount": 300000,
    "term": 18,
    "firstName": "Vasiliy",
    "lastName": "Petrov",
    "middleName": "",
    "email": "vas@gmail.com",
    "birthdate": "1977-08-16",
    "passportSeries": "5766",
    "passportNumber": "576687"
    }

Пример ответа:
    [
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18644.92,
    "rate": 14.5,
    "isInsuranceEnabled": false,
    "isSalaryClient": true
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18574.55,
    "rate": 14,
    "isInsuranceEnabled": true,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18504.34,
    "rate": 13.5,
    "isInsuranceEnabled": true,
    "isSalaryClient": true
    }
    ]

PUT: /deal/offer - выбор одного из предложений

Пример запроса:
    body json:
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    }

Пример ответа:
    {
    "id": 1,
    "client": {
    "id": 1,
    "lastName": "Petrov",
    "firstName": "Vasiliy",
    "middleName": "",
    "birthDate": "1977-08-16",
    "email": "vas@gmail.com",
    "gender": null,
    "maritalStatus": null,
    "dependentAmount": null,
    "passport": {
    "series": "5766",
    "number": "576687",
    "issueBranch": null,
    "issueDate": null
    },
    "employment": null,
    "account": null
    },
    "credit": null,
    "status": "APPROVED",
    "creationDate": "2023-09-05T15:21:18",
    "appliedOffer": {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    "signDate": null,
    "sesCode": null,
    "statusHistory": [
    {
    "status": "PREAPPROVAL",
    "time": "2023-09-05T15:21:18",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "APPROVED",
    "time": "2023-09-05T15:22:35",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "APPROVED",
    "time": "2023-09-05T15:23:46",
    "changeType": "AUTOMATIC"
    }
    ]
    }

PUT: /deal/calculate/{applicationId} -  полный расчет параметров кредита
Пример запроса:
    param: id=1
    body:
    {
    "gender": "MALE",
    "maritalStatus": "MARRIED",
    "dependentAmount": 1,
    "passportIssueDate": "2023-09-05",
    "passportIssueBranch": "UFMS",
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12
    },
    "account": "account"
    }

Пример ответа:
    {
    "gender": "MALE",
    "maritalStatus": "MARRIED",
    "dependentAmount": 1,
    "passportIssueDate": "2023-09-05",
    "passportIssueBranch": "UFMS",
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12,
    "account": "account"
    }
    
    {
    "amount": 300000,
    "term": 18,
    "monthlyPayment": 18294.62,
    "rate": 12,
    "psk": 6.5118,
    "isInsuranceEnabled": false,
    "isSalaryClient": false,
    "paymentSchedule": [
    {
    "number": 1,
    "date": "2023-10-05",
    "totalPayment": 18294.62,
    "interestPayment": 3000,
    "debtPayment": 15294.62,
    "remainingDebt": 284705.38
    },
    {
    "number": 2,
    "date": "2023-11-05",
    "totalPayment": 18294.62,
    "interestPayment": 2847.054,
    "debtPayment": 15447.57,
    "remainingDebt": 269257.81
    },
    {
    "number": 3,
    "date": "2023-12-05",
    "totalPayment": 18294.62,
    "interestPayment": 2692.578,
    "debtPayment": 15602.04,
    "remainingDebt": 253655.77
    },
    {
    "number": 4,
    "date": "2024-01-05",
    "totalPayment": 18294.62,
    "interestPayment": 2536.558,
    "debtPayment": 15758.06,
    "remainingDebt": 237897.71
    },
    {
    "number": 5,
    "date": "2024-02-05",
    "totalPayment": 18294.62,
    "interestPayment": 2378.977,
    "debtPayment": 15915.64,
    "remainingDebt": 221982.07
    },
    {
    "number": 6,
    "date": "2024-03-05",
    "totalPayment": 18294.62,
    "interestPayment": 2219.821,
    "debtPayment": 16074.8,
    "remainingDebt": 205907.27
    },
    {
    "number": 7,
    "date": "2024-04-05",
    "totalPayment": 18294.62,
    "interestPayment": 2059.073,
    "debtPayment": 16235.55,
    "remainingDebt": 189671.72
    },
    {
    "number": 8,
    "date": "2024-05-05",
    "totalPayment": 18294.62,
    "interestPayment": 1896.717,
    "debtPayment": 16397.9,
    "remainingDebt": 173273.82
    },
    {
    "number": 9,
    "date": "2024-06-05",
    "totalPayment": 18294.62,
    "interestPayment": 1732.738,
    "debtPayment": 16561.88,
    "remainingDebt": 156711.94
    },
    {
    "number": 10,
    "date": "2024-07-05",
    "totalPayment": 18294.62,
    "interestPayment": 1567.119,
    "debtPayment": 16727.5,
    "remainingDebt": 139984.44
    },
    {
    "number": 11,
    "date": "2024-08-05",
    "totalPayment": 18294.62,
    "interestPayment": 1399.844,
    "debtPayment": 16894.78,
    "remainingDebt": 123089.66
    },
    {
    "number": 12,
    "date": "2024-09-05",
    "totalPayment": 18294.62,
    "interestPayment": 1230.897,
    "debtPayment": 17063.72,
    "remainingDebt": 106025.94
    },
    {
    "number": 13,
    "date": "2024-10-05",
    "totalPayment": 18294.62,
    "interestPayment": 1060.259,
    "debtPayment": 17234.36,
    "remainingDebt": 88791.58
    },
    {
    "number": 14,
    "date": "2024-11-05",
    "totalPayment": 18294.62,
    "interestPayment": 887.9158,
    "debtPayment": 17406.7,
    "remainingDebt": 71384.88
    },
    {
    "number": 15,
    "date": "2024-12-05",
    "totalPayment": 18294.62,
    "interestPayment": 713.8488,
    "debtPayment": 17580.77,
    "remainingDebt": 53804.11
    },
    {
    "number": 16,
    "date": "2025-01-05",
    "totalPayment": 18294.62,
    "interestPayment": 538.0411,
    "debtPayment": 17756.58,
    "remainingDebt": 36047.53
    },
    {
    "number": 17,
    "date": "2025-02-05",
    "totalPayment": 18294.62,
    "interestPayment": 360.4753,
    "debtPayment": 17934.14,
    "remainingDebt": 18113.39
    },
    {
    "number": 18,
    "date": "2025-03-05",
    "totalPayment": 18294.62,
    "interestPayment": 181.1339,
    "debtPayment": 18113.49,
    "remainingDebt": -0.1
    }
    ]
    }

POST: /deal/document/{applicationId}/send - запрос на отправку документов
Пример запроса:
    param: id=1

Пример ответа:
    http code 200

POST: /deal/document/{applicationId}/sign - запрос на подписание документов
Пример запроса:
    param: id=1

Пример ответа:
    http code 200

POST: /deal/document/{applicationId}/code - подписание документов
Пример запроса:
    param: id=1

Пример ответа:
    http code 200

GET: /deal/admin/application/{applicationId} - получить заявку по id
Пример запроса:
    param: id=1

Пример ответа:
    {
    "id": 1,
    "client": {
    "id": 1,
    "lastName": "Petrov",
    "firstName": "Vasiliy",
    "middleName": "",
    "birthDate": "1977-08-16",
    "email": "vas@gmail.com",
    "gender": null,
    "maritalStatus": null,
    "dependentAmount": null,
    "passport": {
    "series": "5766",
    "number": "576687",
    "issueBranch": null,
    "issueDate": null
    },
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12
    },
    "account": null
    },
    "credit": {
    "id": 1,
    "amount": 300000,
    "term": 18,
    "monthlyPayment": 18856.93,
    "rate": 16,
    "psk": 8.761067,
    "paymentSchedule": [
    {
    "number": 1,
    "date": "2023-10-08",
    "totalPayment": 18856.93,
    "interestPayment": 3999.999,
    "debtPayment": 14856.93,
    "remainingDebt": 285143.07
    },
    {
    "number": 2,
    "date": "2023-11-08",
    "totalPayment": 18856.93,
    "interestPayment": 3801.907,
    "debtPayment": 15055.02,
    "remainingDebt": 270088.05
    },
    {
    "number": 3,
    "date": "2023-12-08",
    "totalPayment": 18856.93,
    "interestPayment": 3601.173,
    "debtPayment": 15255.76,
    "remainingDebt": 254832.29
    },
    {
    "number": 4,
    "date": "2024-01-08",
    "totalPayment": 18856.93,
    "interestPayment": 3397.763,
    "debtPayment": 15459.17,
    "remainingDebt": 239373.12
    },
    {
    "number": 5,
    "date": "2024-02-08",
    "totalPayment": 18856.93,
    "interestPayment": 3191.641,
    "debtPayment": 15665.29,
    "remainingDebt": 223707.83
    },
    {
    "number": 6,
    "date": "2024-03-08",
    "totalPayment": 18856.93,
    "interestPayment": 2982.77,
    "debtPayment": 15874.16,
    "remainingDebt": 207833.67
    },
    {
    "number": 7,
    "date": "2024-04-08",
    "totalPayment": 18856.93,
    "interestPayment": 2771.115,
    "debtPayment": 16085.82,
    "remainingDebt": 191747.85
    },
    {
    "number": 8,
    "date": "2024-05-08",
    "totalPayment": 18856.93,
    "interestPayment": 2556.637,
    "debtPayment": 16300.29,
    "remainingDebt": 175447.56
    },
    {
    "number": 9,
    "date": "2024-06-08",
    "totalPayment": 18856.93,
    "interestPayment": 2339.3,
    "debtPayment": 16517.63,
    "remainingDebt": 158929.93
    },
    {
    "number": 10,
    "date": "2024-07-08",
    "totalPayment": 18856.93,
    "interestPayment": 2119.065,
    "debtPayment": 16737.87,
    "remainingDebt": 142192.06
    },
    {
    "number": 11,
    "date": "2024-08-08",
    "totalPayment": 18856.93,
    "interestPayment": 1895.894,
    "debtPayment": 16961.04,
    "remainingDebt": 125231.02
    },
    {
    "number": 12,
    "date": "2024-09-08",
    "totalPayment": 18856.93,
    "interestPayment": 1669.747,
    "debtPayment": 17187.18,
    "remainingDebt": 108043.84
    },
    {
    "number": 13,
    "date": "2024-10-08",
    "totalPayment": 18856.93,
    "interestPayment": 1440.584,
    "debtPayment": 17416.35,
    "remainingDebt": 90627.49
    },
    {
    "number": 14,
    "date": "2024-11-08",
    "totalPayment": 18856.93,
    "interestPayment": 1208.366,
    "debtPayment": 17648.56,
    "remainingDebt": 72978.93
    },
    {
    "number": 15,
    "date": "2024-12-08",
    "totalPayment": 18856.93,
    "interestPayment": 973.0522,
    "debtPayment": 17883.88,
    "remainingDebt": 55095.05
    },
    {
    "number": 16,
    "date": "2025-01-08",
    "totalPayment": 18856.93,
    "interestPayment": 734.6005,
    "debtPayment": 18122.33,
    "remainingDebt": 36972.72
    },
    {
    "number": 17,
    "date": "2025-02-08",
    "totalPayment": 18856.93,
    "interestPayment": 492.9695,
    "debtPayment": 18363.96,
    "remainingDebt": 18608.76
    },
    {
    "number": 18,
    "date": "2025-03-08",
    "totalPayment": 18856.93,
    "interestPayment": 248.1167,
    "debtPayment": 18608.81,
    "remainingDebt": -0.05
    }
    ],
    "isInsuranceEnabled": false,
    "isSalaryClient": false,
    "creditStatus": "ISSUED"
    },
    "status": "CREDIT_ISSUED",
    "creationDate": "2023-09-08T12:32:32",
    "appliedOffer": {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    "signDate": "2023-09-08T12:48:53.685987",
    "sesCode": null,
    "statusHistory": [
    {
    "status": "PREAPPROVAL",
    "time": "2023-09-08T12:32:32",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "APPROVED",
    "time": "2023-09-08T12:33:00",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "CC_APPROVED",
    "time": "2023-09-08T12:48:23",
    "changeType": "AUTOMATIC"
    }
    ]
    }

GET: /deal/admin/application/ - получить все заявки

Пример ответа:
    [
    {
    "id": 1,
    "client": {
    "id": 1,
    "lastName": "Petrov",
    "firstName": "Vasiliy",
    "middleName": "",
    "birthDate": "1977-08-16",
    "email": "vas@gmail.com",
    "gender": null,
    "maritalStatus": null,
    "dependentAmount": null,
    "passport": {
    "series": "5766",
    "number": "576687",
    "issueBranch": null,
    "issueDate": null
    },
    "employment": {
    "employmentStatus": "EMPLOYED",
    "employerInn": "1234567890",
    "salary": 230000,
    "position": "WORKER",
    "workExperienceTotal": 12,
    "workExperienceCurrent": 12
    },
    "account": null
    },
    "credit": {
    "id": 1,
    "amount": 300000,
    "term": 18,
    "monthlyPayment": 18856.93,
    "rate": 16,
    "psk": 8.761067,
    "paymentSchedule": [
    {
    "number": 1,
    "date": "2023-10-08",
    "totalPayment": 18856.93,
    "interestPayment": 3999.999,
    "debtPayment": 14856.93,
    "remainingDebt": 285143.07
    },
    {
    "number": 2,
    "date": "2023-11-08",
    "totalPayment": 18856.93,
    "interestPayment": 3801.907,
    "debtPayment": 15055.02,
    "remainingDebt": 270088.05
    },
    {
    "number": 3,
    "date": "2023-12-08",
    "totalPayment": 18856.93,
    "interestPayment": 3601.173,
    "debtPayment": 15255.76,
    "remainingDebt": 254832.29
    },
    {
    "number": 4,
    "date": "2024-01-08",
    "totalPayment": 18856.93,
    "interestPayment": 3397.763,
    "debtPayment": 15459.17,
    "remainingDebt": 239373.12
    },
    {
    "number": 5,
    "date": "2024-02-08",
    "totalPayment": 18856.93,
    "interestPayment": 3191.641,
    "debtPayment": 15665.29,
    "remainingDebt": 223707.83
    },
    {
    "number": 6,
    "date": "2024-03-08",
    "totalPayment": 18856.93,
    "interestPayment": 2982.77,
    "debtPayment": 15874.16,
    "remainingDebt": 207833.67
    },
    {
    "number": 7,
    "date": "2024-04-08",
    "totalPayment": 18856.93,
    "interestPayment": 2771.115,
    "debtPayment": 16085.82,
    "remainingDebt": 191747.85
    },
    {
    "number": 8,
    "date": "2024-05-08",
    "totalPayment": 18856.93,
    "interestPayment": 2556.637,
    "debtPayment": 16300.29,
    "remainingDebt": 175447.56
    },
    {
    "number": 9,
    "date": "2024-06-08",
    "totalPayment": 18856.93,
    "interestPayment": 2339.3,
    "debtPayment": 16517.63,
    "remainingDebt": 158929.93
    },
    {
    "number": 10,
    "date": "2024-07-08",
    "totalPayment": 18856.93,
    "interestPayment": 2119.065,
    "debtPayment": 16737.87,
    "remainingDebt": 142192.06
    },
    {
    "number": 11,
    "date": "2024-08-08",
    "totalPayment": 18856.93,
    "interestPayment": 1895.894,
    "debtPayment": 16961.04,
    "remainingDebt": 125231.02
    },
    {
    "number": 12,
    "date": "2024-09-08",
    "totalPayment": 18856.93,
    "interestPayment": 1669.747,
    "debtPayment": 17187.18,
    "remainingDebt": 108043.84
    },
    {
    "number": 13,
    "date": "2024-10-08",
    "totalPayment": 18856.93,
    "interestPayment": 1440.584,
    "debtPayment": 17416.35,
    "remainingDebt": 90627.49
    },
    {
    "number": 14,
    "date": "2024-11-08",
    "totalPayment": 18856.93,
    "interestPayment": 1208.366,
    "debtPayment": 17648.56,
    "remainingDebt": 72978.93
    },
    {
    "number": 15,
    "date": "2024-12-08",
    "totalPayment": 18856.93,
    "interestPayment": 973.0522,
    "debtPayment": 17883.88,
    "remainingDebt": 55095.05
    },
    {
    "number": 16,
    "date": "2025-01-08",
    "totalPayment": 18856.93,
    "interestPayment": 734.6005,
    "debtPayment": 18122.33,
    "remainingDebt": 36972.72
    },
    {
    "number": 17,
    "date": "2025-02-08",
    "totalPayment": 18856.93,
    "interestPayment": 492.9695,
    "debtPayment": 18363.96,
    "remainingDebt": 18608.76
    },
    {
    "number": 18,
    "date": "2025-03-08",
    "totalPayment": 18856.93,
    "interestPayment": 248.1167,
    "debtPayment": 18608.81,
    "remainingDebt": -0.05
    }
    ],
    "isInsuranceEnabled": false,
    "isSalaryClient": false,
    "creditStatus": "ISSUED"
    },
    "status": "CREDIT_ISSUED",
    "creationDate": "2023-09-08T12:32:32",
    "appliedOffer": {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    "signDate": "2023-09-08T12:48:53.685987",
    "sesCode": null,
    "statusHistory": [
    {
    "status": "PREAPPROVAL",
    "time": "2023-09-08T12:32:32",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "APPROVED",
    "time": "2023-09-08T12:33:00",
    "changeType": "AUTOMATIC"
    },
    {
    "status": "CC_APPROVED",
    "time": "2023-09-08T12:48:23",
    "changeType": "AUTOMATIC"
    }
    ]
    },
    {
    "id": 2,
    "client": {
    "id": 2,
    "lastName": "Petrov",
    "firstName": "Vasiliy",
    "middleName": "",
    "birthDate": "1977-08-16",
    "email": "vas@gmail.com",
    "gender": null,
    "maritalStatus": null,
    "dependentAmount": null,
    "passport": {
    "series": "5766",
    "number": "576687",
    "issueBranch": null,
    "issueDate": null
    },
    "employment": null,
    "account": null
    },
    "credit": null,
    "status": "PREAPPROVAL",
    "creationDate": "2023-09-08T12:50:06",
    "appliedOffer": null,
    "signDate": null,
    "sesCode": null,
    "statusHistory": [
    {
    "status": "PREAPPROVAL",
    "time": "2023-09-08T12:50:06",
    "changeType": "AUTOMATIC"
    }
    ]
    }
    ]

Микросервис application
POST: /application - первичная валидация заявки, создание заявки

Пример запроса:
    body json:
    {
    "amount": 300000,
    "term": 18,
    "firstName": "Vasiliy",
    "lastName": "Petrov",
    "middleName": "",
    "email": "vas@gmail.com",
    "birthdate": "1977-08-16",
    "passportSeries": "5766",
    "passportNumber": "576687"
    }

Пример ответа:
    [
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18644.92,
    "rate": 14.5,
    "isInsuranceEnabled": false,
    "isSalaryClient": true
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18574.55,
    "rate": 14,
    "isInsuranceEnabled": true,
    "isSalaryClient": false
    },
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 290000,
    "term": 18,
    "monthlyPayment": 18504.34,
    "rate": 13.5,
    "isInsuranceEnabled": true,
    "isSalaryClient": true
    }
    ]

POST: /application/offer - выбор одного из предложений

Пример запроса:
    body json:
    {
    "applicationId": 1,
    "requestedAmount": 300000,
    "totalAmount": 300000,
    "term": 18,
    "monthlyPayment": 18715.44,
    "rate": 15,
    "isInsuranceEnabled": false,
    "isSalaryClient": false
    }
    
Пример ответа:
    http code 200

Микросервис gateway
POST: /conveyor/offers - расчёт возможных условий кредита
PUT: /deal/offer - выбор одного из предложений
PUT: /deal/calculate/{applicationId} -  полный расчет параметров кредита
POST: /deal/document/{applicationId}/send - запрос на отправку документов
POST: /deal/document/{applicationId}/sign - запрос на подписание документов
POST: /deal/document/{applicationId}/code - подписание документов
GET: /deal/admin/application/{applicationId} - получить заявку по id
PUT: /deal/admin/application/{applicationId}/status - обновить статус заявки

Запросы и ответы аналогичны описанным выше