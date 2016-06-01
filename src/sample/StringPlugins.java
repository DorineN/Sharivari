package sample;

/**
 * Created by Loïc on 28/05/2016.
 */

/**
 * Cette interface est strictement destinée à l'utilisation par des plugins et en aucun cas par des classes internes à notre application
 *
 */

public interface StringPlugins extends PluginsBase {

    /**
     * Fonction de traitement principale du plugins de manipulation de String
     * @param ini La chaine initiale
     * @return La chaine traitée
     */
    public String actionOnString(String ini);

}