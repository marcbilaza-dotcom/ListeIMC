<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="fr_FR"/>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des patients - IMC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="conteneur">
    <h1>Liste des patients</h1>

    <c:choose>
        <c:when test="${empty patients}">
            <p class="vide">Aucun patient enregistré.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Masse (kg)</th>
                    <th>Taille (m)</th>
                    <th>IMC</th>
                    <th>Interprétation</th>
                    <th colspan="2">Opérations</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${patients}">
                    <tr>
                        <td><c:out value="${p.nom}"/></td>
                        <td><c:out value="${p.prenom}"/></td>
                        <td><fmt:formatNumber value="${p.masse}" maxFractionDigits="1"/></td>
                        <td><fmt:formatNumber value="${p.taille}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><fmt:formatNumber value="${p.imc}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>
                            <c:set var="classe" value="${p.imc lt 18.5 ? 'maigre'
                                                     : p.imc lt 25   ? 'normal'
                                                     : p.imc lt 30   ? 'surpoids' : 'obese'}"/>
                            <span class="badge ${classe}"><c:out value="${p.interpretation}"/></span>
                        </td>
                        <td>
                            <c:url var="urlSuppr" value="/patients">
                                <c:param name="action" value="supprimer"/>
                                <c:param name="id" value="${p.id}"/>
                            </c:url>
                            <a class="lien suppr" href="${urlSuppr}"
                               onclick="return confirm('Supprimer ${p.nom} ${p.prenom} ?');">Supprimer</a>
                        </td>
                        <td>
                            <c:url var="urlModif" value="/patients">
                                <c:param name="action" value="modifier"/>
                                <c:param name="id" value="${p.id}"/>
                            </c:url>
                            <a class="lien modif" href="${urlModif}">Modifier</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p class="total">${patients.size()} patient(s)</p>
        </c:otherwise>
    </c:choose>

    <form action="${pageContext.request.contextPath}/patients" method="get">
        <input type="hidden" name="action" value="ajouter">
        <button type="submit" class="bouton">Ajouter un patient</button>
    </form>
</div>
</body>
</html>
