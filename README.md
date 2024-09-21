Der BankTransactionService ist ein REST-basierter Microservice, der es ermöglicht, Banktransaktionen eines Kunden von der Bank (Kreissparkasse, Volksbank oder ähnliche) abzurufen und an Drittsysteme weiterzugeben. 
Er kommuniziert mit der Bank über eine zwischen API (PSD2-konforme) und bietet Funktionen zum Abrufen, Filtern und Sortieren von Transaktionen.
<br>Start:
<br>mvn clean package
<br>docker build -t bankservice .
<br>docker compose up
