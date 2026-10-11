<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <c:set var="modeModif" value="${not empty patient and patient.id gt 0}"/>
    <title>${modeModif ? 'Modifier' : 'Ajouter'} un patient</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="conteneur etroit">
    <h1>${modeModif ? 'Modifier le patient' : 'Nouveau patient'}</h1>

    <c:if test="${not empty erreur}">
        <p class="erreur"><c:out value="${erreur}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/patients" method="post" class="formulaire">
        <input type="hidden" name="id" value="${patient.id}">

        <label for="nom">Nom</label>
        <input type="text" id="nom" name="nom" value="<c:out value='${patient.nom}'/>" required>

        <label for="prenom">Prénom</label>
        <input type="text" id="prenom" name="prenom" value="<c:out value='${patient.prenom}'/>" required>

        <label for="masse">Masse (kg)</label>
        <input type="number" id="masse" name="masse" step="0.1" min="1"
               value="${patient.masse gt 0 ? patient.masse : ''}" required>

        <label for="taille">Taille (m)</label>
        <input type="number" id="taille" name="taille" step="0.01" min="0.3" max="3"
               value="${patient.taille gt 0 ? patient.taille : ''}" required>

        <%-- IMC et interprétation : calculés automatiquement (aperçu ici, recalculés par le Bean côté serveur) --%>
        <label for="imc">IMC</label>
        <input type="text" id="imc" name="imc" readonly
               value="<c:if test='${patient.imc gt 0}'><fmt:formatNumber value='${patient.imc}' maxFractionDigits='2'/></c:if>">

        <label for="interpretation">Interprétation</label>
        <input type="text" id="interpretation" name="interpretation" readonly
               value="<c:out value='${patient.interpretation}'/>">

        <div class="actions">
            <button type="submit" class="bouton">${modeModif ? 'Enregistrer' : 'Ajouter'}</button>
            <a class="lien" href="${pageContext.request.contextPath}/patients">Annuler</a>
        </div>
    </form>
</div>

<script>
    // Aperçu instantané de l'IMC pendant la saisie
    (function () {
        const masse = document.getElementById('masse');
        const taille = document.getElementById('taille');
        const imc = document.getElementById('imc');
        const interp = document.getElementById('interpretation');

        function interpreter(v) {
            if (v < 18.5) return 'Insuffisance pondérale';
            if (v < 25)   return 'Corpulence normale';
            if (v < 30)   return 'Surpoids';
            if (v < 35)   return 'Obésité modérée';
            if (v < 40)   return 'Obésité sévère';
            return 'Obésité morbide';
        }

        function maj() {
            const m = parseFloat(masse.value), t = parseFloat(taille.value);
            if (m > 0 && t > 0) {
                const v = m / (t * t);
                imc.value = v.toFixed(2).replace('.', ',');
                interp.value = interpreter(v);
            } else {
                imc.value = '';
                interp.value = '';
            }
        }
        masse.addEventListener('input', maj);
        taille.addEventListener('input', maj);
    })();
</script>
</body>
</html>
