package app.controller;

import app.controller.FileController;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileControllerTest {

    @Test
    public void testGetNumRow() throws Exception {
        FileController fileController = new FileController();
        int nbElements = 2;
        int nbColumns = 5;

        Assert.assertEquals(0, fileController.getNumRow(nbElements, nbColumns));

        nbElements = 5;
        Assert.assertEquals(0, fileController.getNumRow(nbElements, nbColumns));

        nbElements = 6;
        Assert.assertEquals(1, fileController.getNumRow(nbElements, nbColumns));

        nbElements = 11;
        Assert.assertEquals(2, fileController.getNumRow(nbElements, nbColumns));
    }

    @Test
    public void testGetNumCol() throws Exception {

    }
}