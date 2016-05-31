package sample;

/**
 * Created by Loïc on 28/05/2016.
 */

/**
 * Interface de Plugins de manipulation de int
 * @author Lainé Vincent (dev01, http://vincentlaine.developpez.com/ )
 *
 */

public interface IntPlugins extends PluginsBase {

    /**
     * Traitement sur le int passé en argument
     * @param ini
     * @return
     */
    public int actionOnInt(int ini);

}