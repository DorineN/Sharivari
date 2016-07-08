package tests;

import org.junit.Test;
import sample.util.Fields;

import static org.junit.Assert.*;

public class FieldsTest {

    @Test
    public void testVerifyLogin() throws Exception {
        // "True" expected
        String login = "Azedine94";
        assertTrue("Majuscule + carac. numériques", Fields.verifyLogin(login));
        login = "Az-dine_94";
        assertTrue("Tirets + underscore", Fields.verifyLogin(login));
        login = "@zedine";

        // False expected
        assertFalse("Arobase", Fields.verifyLogin(login));
        login = "Az.dine";
        assertFalse("Point", Fields.verifyLogin(login));
        login = "Azédine";
        assertFalse("Carac. accentués", Fields.verifyLogin(login));
    }

    @Test
    public void testVerifyMail() throws Exception {
        // "True" expected
        String mail = "Azedine94@domaine.fr";
        assertTrue("Carac. Alphanumériques", Fields.verifyMail(mail));
        mail = "azedine@domaine.co.in";
        assertTrue("Nom de domaine composé (avec fin valide)", Fields.verifyMail(mail));
        mail = "user.name@domaine.com";
        assertTrue("Point", Fields.verifyMail(mail));
        mail = "user_na-me@domaine.fr";
        assertTrue("Underscore + tiret", Fields.verifyMail(mail));

        mail = ".azedine@domaine.fr";
        assertFalse("Point en début de chaine", Fields.verifyMail(mail));
        mail = "azedine@domaine.fr.";
        assertFalse("Point en fin de chaine", Fields.verifyMail(mail));
        mail = "az..dine.domaine.fr";
        assertFalse("Deux points consécutifs", Fields.verifyMail(mail));
        mail = "azedine@domaine.something";
        assertFalse("Nom de domaine composé (fin non valide)", Fields.verifyMail(mail));
    }
}