$(document).ready(function () {
    // variables
    var radioAchat = $("#achats");
    var encheresOuvertes = $("#encheresOuvertes");
    var encheresEnCours = $("#encheresEnCours");
    var encheresRemportees = $("#encheresRemportees");

    var radioVente = $("#ventes");
    var ventesEnCours = $("#ventesEnCours");
    var ventesNonDebutees = $("#ventesNonDebutees");
    var ventesTerminees = $("#ventesTerminees");

    if (radioAchat.is(":checked"))
    {
        encheresOuvertes.attr("disabled", false);
        encheresEnCours.attr("disabled", false);
        encheresRemportees.attr("disabled", false);

        ventesEnCours.attr("disabled", true);
        ventesNonDebutees.attr("disabled", true);
        ventesTerminees.attr("disabled", true);
    }

    radioAchat.click(function () {
        encheresOuvertes.attr("disabled", false);
        encheresEnCours.attr("disabled", false);
        encheresRemportees.attr("disabled", false);

        ventesEnCours.attr("disabled", true);
        ventesNonDebutees.attr("disabled", true);
        ventesTerminees.attr("disabled", true);
    });

    radioVente.click(function () {
        encheresOuvertes.attr("disabled", true);
        encheresEnCours.attr("disabled", true);
        encheresRemportees.attr("disabled", true);

        ventesEnCours.attr("disabled", false);
        ventesNonDebutees.attr("disabled", false);
        ventesTerminees.attr("disabled", false);
    });
});