<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Enchère</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    </head>
    <body>
        <header class="bg-info">
            <h1 class="p-3">
                <a class="text-white" href="${pageContext.request.contextPath}/">ENI-Enchères</a>
            </h1>
        </header>

        <section class="container">
            <section class="row">

                <h2 class="text-center col-12">Détail vente</h2>

                <section class="col-xs-12 col-sm-12 col-md-4 mb-3">
                    <img class="img-fluid" src="${pageContext.request.contextPath}/images/encheres/gris.jpg" alt="enchere">
                </section>

                <section class="col-xs-12 col-sm-12 col-md-8">

                    <div class="container">
                        <div class="row">
                            <h3 class="col-12">${sessionScope.article.nomArticle}</h3>

                            <%-- description --%>
                            <p class="col-5">Description :</p>
                            <p class="col-7">${sessionScope.article.description}</p>

                            <%-- catégorie --%>
                            <p class="col-5">Catégorie :</p>
                            <p class="col-7">${sessionScope.article.categorie.libelle}</p>

                            <%-- meilleure offre --%>
                            <p class="col-5">Meilleure offre :</p>
                            <p class="col-7">${sessionScope.article.prixVente}</p>

                            <%-- Retrait --%>
                            <p class="col-5">retrait :</p>
                            <p class="col-7">
                                ${sessionScope.retrait.rue}, ${sessionScope.retrait.codePostal} ${sessionScope.retrait.ville}
                            </p>

                            <%-- vendeur --%>
                            <p class="col-5">Vendeur :</p>
                            <p class="col-7">${sessionScope.article.utilisateur.pseudo}</p>

                            <%-- proposition enchère --%>
                            <c:if test="${sessionScope.isConnecte == true}">
                                <c:if test="${(sessionScope.pseudo != sessionScope.article.utilisateur.pseudo) && requestScope.enchereEnCours}">
                                    <p class="col-5">Ma proposition</p>
                                    <form class="col-7" action="${pageContext.request.contextPath}/afficherEnchere" method="post">
                                        <input class="form-control mb-3" type="number" name="montantEnchere"
                                               min="${sessionScope.article.prixVente}" value="${sessionScope.article.prixVente}">

                                        <input class="float-right btn btn-success" type="submit" name="encherir" value="Enchérir">
                                    </form>
                                </c:if>
                            </c:if>
                            <c:if test="${sessionScope.isConnecte == true}">
                                <c:if test="${(sessionScope.pseudo == sessionScope.article.utilisateur.pseudo) && requestScope.enchereNonCommence}">
                                    <a class="btn btn-info" href="${pageContext.request.contextPath}/membre/modifierArticle">Modifier</a>
                                </c:if>
                            </c:if>

                            <p class="text-danger">${sessionScope.errreur}</p>
                        </div>
                    </div>
                </section>
            </section>
        </section>
    </body>
</html>
