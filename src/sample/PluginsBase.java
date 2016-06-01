package sample;

/**
 * Created by Loïc on 28/05/2016.
 */


/**

 * Cette interface n'est destinée à être directement implémenté dans un plugins,
 * elle sert à définir un comportement commun à toutes les interfaces de plugins.
 *
 */

public interface PluginsBase {
    /**
     * Obtient le libellé à afficher dans les menu ou autre pour le plugins
     * @return Le libellé sous forme de String. Ce libellé doit être clair et compréhensible facilement
     */
    public String getLibelle();

    /**
     * Obtient la catégorie du plugins. Cette catégorie est celle dans laquelle le menu du plugins sera ajouté une fois chargé
     * @return
     */
    public int getCategorie();



}