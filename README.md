# EasyMoneyMap

## Ziel des Projekts
Die App soll es ermöglichen, innerhalb von Benutzergruppen Kosten zu erfassen und fair zu verteilen. In sogenannten Events (wie z. B. Reisen, gemeinsamen Aktivitäten oder Ausflügen) können Benutzer Käufe hinzufügen und festlegen, wie diese Ausgaben auf die Teilnehmer verteilt werden sollen – abhängig davon, wer wie viel bezahlt hat und wer welchen Anteil der Kosten übernehmen muss. Am Ende zeigt das System genau an, wer wem wie viel Geld schuldet, sodass jeder seinen Anteil ausgeglichen hat. Die App bietet eine übersichtliche Rückverfolgung der Ausgaben und Schulden innerhalb der Gruppe.

Die App soll sowohl für iOS als auch für Android verfügbar sein, weshalb das Frontend mit Flutter entwickelt wird.

## Hauptfunktionen (Features)
- **Benutzerverwaltung**: Benutzer können sich registrieren und sicher einloggen. Die Authentifizierung erfolgt über ein JWT-basiertes System (JSON Web Token), das eine sichere und 
  effiziente Autorisierung ermöglicht.
  
- **Event-Verwaltung**: Benutzer können Events erstellen (z. B. Gruppenreisen, gemeinsame Aktivitäten) und andere Teilnehmer zu diesen Events hinzufügen. Jedes Event dient als 
  Rahmen für die Erfassung und Verwaltung der geteilten Kosten.
  
- **Kostenaufteilung**: Innerhalb eines Events können Benutzer Käufe oder Ausgaben erfassen und festlegen, wie diese Ausgaben auf die verschiedenen Teilnehmer verteilt werden 
  sollen. Dabei kann der Anteil jeder Person individuell angepasst werden, je nachdem, wer wie viel gezahlt hat oder wie viel jeder zahlen soll.
  
- **Schuldenübersicht**: Die App bietet eine detaillierte Übersicht, wer wem wie viel Geld schuldet. Sie zeigt an, welche Beträge noch ausgeglichen werden müssen, damit alle 
  Teilnehmer ihren korrekten Anteil bezahlt haben. So kann jeder genau nachvollziehen, wer Zahlungen an wen leisten muss, um die Kosten fair zu verteilen.

## Aktueller Entwicklungsstand
Momentan bin ich dabei, das **Backend** zu entwickeln. Die folgenden Komponenten sind bereits in Arbeit oder fertig:
- **Benutzerregistrierung** und **Login** mit JWT-basierten Tokens für Autorisierung.
- **Event-Erstellung/Verwaltung** im Backend.
- Ich verfolge einen iterativen Entwicklungsansatz, wobei ich Unit- und Integrationstests für jede Komponente durchführe, bevor ich zur nächsten übergehe.

## Technologien
- **Backend**: Java mit Spring Boot
- **Frontend**: Flutter (in Planung)
- **Datenbank**: Hibernate ORM für Datenbankverwaltung
- **Authentifizierung**: JWT (JSON Web Tokens) für Authentifizierung und Autorisierung
- **Datenbank**: Derzeit wird eine H2-Datenbank verwendet (später erweiterbar auf andere Datenbanken)
