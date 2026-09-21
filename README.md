---
===
LABORATOIRE 3 - 420-930-MA - Ete 2026 - gr. 25604
===
---
   ---
   Nom du projet "Samflix"
---
   Cours : 420-930-MA — Algorithmes et modèles de programmation
   Session : Été 2026, groupe 25604
   Laboratoire : 3 (Application JavaFX v2)
   Date de remise : 20 septembre 2026, 23h59
   ---
Équipe
Nom complet 	Adresse courriel	
Samuel Lortie	samuellortie2001@gmail.com	
---
Sujet choisi
Numéro du sujet : 1
Nom du sujet : "Netflix Catalog"
---
🔗 Lien du dépôt GitHub PUBLIC
URL : https://github.com/Samy199L/LAB-NETFLIX.git

---
✅Migration CSV → PostgreSQL (pattern DAO) 

✅Opérations CRUD depuis l'interface

❌Bonus
---
````
netflix-catalog-lab2/
├── pom.xml
├──schema.sql
├──donnees.sql
├── src/main/
│   ├── java/
│   │   ├── module-info.java
│   │   └── org/example/NetflixLab/
│   │       ├── MainFx.java
│   │       ├── model/
│   │       │   ├── Media.java
│   │       │   ├── Film.java
│   │       │   ├── Serie.java
│   │       │   ├── Genre.java
│   │       │   ├── StatutSerie.java
│   │       │   └── Watchlist.java
│   │       ├── service/
│   │       │   ├── Donnees.java
│   │       │   ├── ServiceFiltrage.java
│   │       │   ├── FiltreCriteres.java
│   │       │   ├── TypeFilter.java
│   │       │   ├── ServicePagination.java
│   │       │   ├── ServiceTri.java
│   │       │   ├── ServiceBenchmark.java
│   │       │   └── ResultatMesure.java
│   │       ├── dao/
│   │       │   ├── Connexion.java
│   │       │   ├── MediaDAO.java
│   │       │   └── MediaDAOPostgres.java
│   │       ├── algorithmes/
│   │       │   ├── Algorithme.java
│   │       │   └── tri/
│   │       │       ├── TriBulle.java
│   │       │       ├── TriSelection.java
│   │       │       └── TriInsertion.java
│   │       ├── controller/
│   │       │   └── Controller.java
│   │       └── util/
│   │           └── LecteurCSV.java
│   └── resources/
│       ├── vues/
│       │   └── vue-principale.fxml
│       ├── css/
│       │   └── style.css
│       │   data/
│       ├──  └── mediasKaggleMod.csv
        │   
        ├──database.properties.example
````
---
====
Instructions pour lancer le projet
====
Prérequis
JDK 21
Maven 3
(optionnel) IntelliJ IDEA 
Étapes
```bash
# 1. Cloner le dépôt
git clone https://github.com/Samy199L/LAB-NETFLIX.git
cd LAB-NETFLIX
 
# 2. Compiler
mvn clean compile
 
# 3. Creer Base De Donnee (netflixlab)

# 4. Lancer les scripts SQL
psql -U postgres -d netflixlab -f schema.sql
psql -U postgres -d netflixlab -f donnees.sql

#5 Enlevez .example de database.properties.example et remplacer les valeurs fictives
 
#6 . Lancer l'application
mvn clean javafx:run
```

Répartition du travail (auto-évaluation)
Membre	% contribution estimée	
Samuel Lortie 100%
---
Notes pour le correcteur 
---
le travail a été realiser seul 
---
Captures d'écran (fortement recommandé)

```markdown
### Ajout Lab 3
![Écran principal](screenshots/Catalogue.png)
```

