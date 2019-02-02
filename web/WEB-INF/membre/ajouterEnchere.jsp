<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
	<head>
		<title>Ajouter une enchère</title>
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

		<section>
			<h2 class="text-center my-4">Nouvelle vente</h2>

			<div class="container">
				<div class="row">
					<section class="col-xs-12 col-sm-12 col-md-4 mb-3">
						<img class="img-fluid"
							src="${pageContext.request.contextPath}/images/encheres/gris.jpg"
							alt="enchere">
					</section>

					<section class="col-xs-12 col-sm-12 col-md-8">
						<form
							action="${pageContext.request.contextPath}/membre/ajouterEnchere"
							method="post">
							<div class="container">
								<div class="row">

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="nomArticle">Article
												:<span class="text-danger">*</span>
											</label>
										</div>
										<input id="nomArticle" class="form-control" name="nomArticle"
											type="text" required>
									</section>

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="description">Description
												:<span class="text-danger">*</span>
											</label>
										</div>
										<textarea class="form-control" name="description"
											id="description" required></textarea>
									</section>

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="noCategorie">Catégorie
												:<span class="text-danger">*</span>
											</label>
										</div>
										<select id="noCategorie" class="form-control"
											name="noCategorie" required>
											<option value="0">Sélectionner une catégorie</option>
											<c:forEach items="${sessionScope.categories}" var="categorie">
												<option value="${categorie.noCategorie}">${categorie.libelle}</option>
											</c:forEach>
										</select>
									</section>

									<section class="form-group mb-3">
										<label for="photo">Photo de l'article :</label> <input
											id="photo" class="form-control-file" name="photo" type="file"
											accept="image/png, image/jpeg" value="uploader">
									</section>

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="prixInitial">Mise
												à prix :<span class="text-danger">*</span>
											</label>
										</div>
										<input id="prixInitial" class="form-control" name="prixInitial"
											type="number" min="0" value="0">
									</section>

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="dateDebutEncheres">Début
												de l'enchère :<span class="text-danger">*</span>
											</label>
										</div>
										<input id="dateDebutEncheres" class="form-control"
											name="dateDebutEncheres" type="date" required>
									</section>

									<section class="input-group mb-3">
										<div class="input-group-prepend">
											<label class="input-group-text" for="dateFinEncheres">Fin
												de l'enchère :<span class="text-danger">*</span>
											</label>
										</div>
										<input id="dateFinEncheres" class="form-control"
											name="dateFinEncheres" type="date" required>
									</section>

									<fieldset class="col-12">
										<legend>Retrait</legend>

										<section class="input-group mb-3">
											<div class="input-group-prepend">
												<label class="input-group-text" for="rue">Rue :<span
													class="text-danger">*</span></label>
											</div>
											<input id="rue" class="form-control" name="rue" type="text"
												value="${requestScope.retrait.rue}" required>
										</section>

										<section class="input-group mb-3">
											<div class="input-group-prepend">
												<label class="input-group-text" for="codePostal">Code
													postal :<span class="text-danger">*</span>
												</label>
											</div>
											<input id="codePostal" class="form-control" name="codePostal"
												type="text" value="${requestScope.retrait.codePostal}"
												required>
										</section>

										<section class="input-group mb-3">
											<div class="input-group-prepend">
												<label class="input-group-text" for="ville">Ville :<span
													class="text-danger">*</span></label>
											</div>
											<input id="ville" class="form-control" name="ville"
												type="text" value="${requestScope.retrait.ville}" required>
										</section>
									</fieldset>


									<section class="col-12 text-center">
										<p class="text-danger text-right">* Champs obligatoire</p>
										<p class="text-danger text-left">${sessionScope.erreur}</p>
										<p class="text-success text-left">${sessionScope.succes}</p>
										<input class="btn btn-success" type="submit"
											name="creerEnchere" value="Enregistrer"> 
											<a
											class="btn btn-danger"
											href="${pageContext.request.contextPath}/">Annuler</a>
									</section>
								</div>
							</div>
						</form>

					</section>
				</div>
			</div>
		</section>
	</body>
</html>
