# ListeIMC – Gestion des patients et de leur IMC (Servlet / JSP / JSTL)

## Structure

```
ListeIMC/
├── pom.xml
├── sql/ListeIMC.sql                       (bonus : base ListeIMC, table patient)
└── src/main/
    ├── java/fr/imc/
    │   ├── beans/MonIMC.java              Question 1 : le Bean
    │   ├── servlets/PatientServlet.java   Question 2 (init) + Question 3 (création)
    │   └── dao/PatientDAO.java            Bonus : accès JDBC
    └── webapp/
        ├── index.jsp                      redirige vers /patients
        ├── css/style.css
        └── WEB-INF/
            ├── web.xml
            ├── liste.jsp                  tableau + Supprimer / Modifier + bouton Ajouter
            └── formulaire.jsp             ajout ET modification d'un patient
```

## Correspondance avec les questions

| Question | Où |
|---|---|
| Q1 – Bean MonIMC (masse, taille, imc, interprétation, nom, prénom) | `MonIMC.java` – l'IMC et l'interprétation (normes OMS) sont calculés par `calculer()` |
| Q2 – Redéfinir `init()` avec une liste de MonIMC | `PatientServlet.init()` |
| Q2 bis – JSP qui affiche la liste + Supprimer / Modifier + bouton Ajouter | `liste.jsp` (`c:forEach`, `c:url`, `fmt:formatNumber`, EL) |
| Q3 – Page de création + servlet qui crée le Bean et l'ajoute à la liste | `formulaire.jsp` + `PatientServlet.doPost()` |

Les JSP n'utilisent aucun scriptlet : uniquement EL (`${...}`), balises JSP et JSTL (`c:` et `fmt:`).
Elles sont placées dans `WEB-INF` pour qu'on ne puisse y accéder qu'en passant par la servlet (MVC).

## Lancer le projet

1. Serveur : **Tomcat 10 ou 10.1** (API `jakarta.*`).
2. Importer le dossier dans Eclipse / IntelliJ comme projet Maven, puis *Run on Server*.
   Ou en ligne de commande : `mvn package`, puis copier `target/ListeIMC.war` dans `tomcat/webapps`.
3. Ouvrir `http://localhost:8080/ListeIMC/`

### Si votre serveur est Tomcat 9 (API `javax.*`)
- Dans les `.java` : remplacer `jakarta.servlet` par `javax.servlet`.
- Dans les JSP : `uri="http://java.sun.com/jsp/jstl/core"` et `uri="http://java.sun.com/jsp/jstl/fmt"`.
- Dans le `pom.xml` : `javax.servlet:javax.servlet-api:4.0.1` (provided) et `javax.servlet:jstl:1.2`.

## Bonus – remplacer la liste par la base de données

1. Exécuter `sql/ListeIMC.sql` dans MySQL (adapter l'utilisateur / mot de passe dans `PatientDAO`).
2. Dans `PatientServlet`, déclarer `private PatientDAO dao;`, l'initialiser dans `init()`
   (`dao = new PatientDAO();`) puis remplacer les accès à la liste :

| Liste en mémoire | Base de données |
|---|---|
| `patients` (affichage) | `dao.lister()` |
| `trouver(id)` | `dao.trouver(id)` |
| `patients.remove(p)` | `dao.supprimer(id)` |
| `patients.add(nouveau)` | `dao.ajouter(nouveau)` |
| mise à jour de `existant` | `dao.modifier(existant)` |

(Entourer les appels d'un `try/catch (SQLException e)` qui relance une `ServletException`.)
Les JSP restent inchangées.
