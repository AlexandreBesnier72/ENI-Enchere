<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Profil</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
  		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css">
  		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
 		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"></script>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/affichageProfil.css">
	</head>
<body >
	<header class="bg-info">
		<h1 class="p-3">
			<a class="text-white" href="${pageContext.request.contextPath}/">ENI - Enchères</a>
		</h1>
	</header>
	<section class="container mt-5">
		<div class="row">
			<p class="col-6 text-right">Pseudo : </p>
			<p class="col-6"><strong>${requestScope.utilisateur.pseudo}</strong></p>
			<p class="col-6 text-right">Nom :  </p>
			<p class="col-6">${requestScope.utilisateur.nom }</p>
			<p class="col-6 text-right">Prénom : </p>
			<p class="col-6">${requestScope.utilisateur.prenom }</p>
			<p class="col-6 text-right">Email : </p>
			<p class="col-6">${requestScope.utilisateur.email }</p>
			<p class="col-6 text-right">Téléphone : </p>
			<p class="col-6">${requestScope.utilisateur.telephone }</p>
			<p class="col-6 text-right">Rue : </p>
			<p class="col-6">${requestScope.utilisateur.rue }</p>
			<p class="col-6 text-right">Code Postal : </p>
			<p class="col-6">${requestScope.utilisateur.codePostal }</p>
			<p class="col-6 text-right">Ville : </p>
			<p class="col-6">${requestScope.utilisateur.ville }</p>
		</div>


		<section class="text-center">
			<c:if test="${sessionScope.pseudo eq requestScope.pseudo}">
				<form id="Form" method="POST" action="${pageContext.request.contextPath}/affichageProfil" accept-charset="UTF-8" autocomplete="on">
					<input class="btn btn-success mt-4" name="btnModifierProfil" type="submit" value="Modifier">
				</form>
			</c:if>
		</section>
	</section>


</body>
</html>