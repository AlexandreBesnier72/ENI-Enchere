<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Connexion</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
 		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
 		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
 		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

		<link rel="stylesheet" href="css/connexion.css">
	</head>
	<body>
		<h1><a class="text-white" href="${pageContext.request.contextPath}/">ENI - Enchères</a></h1>

		<section  class="d-flex justify-content-center">
			<form method="post" action="${pageContext.request.contextPath}/connexion">

				<h2 class="text-center">Se connecter</h2>

				<section class="input-group mb-3">
					<div class="input-group-prepend">
						<label class="input-group-text" for="identifiant">Identifiant : </label>
					</div>
					<input id="identifiant" class="form-control" name="identifiant" type="text" value="${requestScope.identifiant}" required>
				</section>

				<section class="input-group mb-3">
					<div class="input-group-prepend">
						<label class="input-group-text" for="motDePasse">Mot de pase : </label>
					</div>
					<input id="motDePasse" class="form-control" name="motDePasse" type="password" required>
				</section>

				<section class="input-group mb-3">
					<button class="btn btn-success" type="submit" name="connexion">Connexion</button>

					<label class="form-check-label ml-3" for="seSouvenirDeMoi">
						<input id="seSouvenirDeMoi" type="checkbox" class="for-check-input"> Se souvenir de moi
					</label>

					<p> &nbsp; <a class="motDePasseOublie" href="#">Mot de passe oublié</a></p>
				</section>

				<div class="text-danger">${sessionScope.erreur}</div>

				<a id="creer" class="btn btn-info" href="${pageContext.request.contextPath}/inscription">Créer un compte</a>
			</form>
		</section>

	</body>
</html>