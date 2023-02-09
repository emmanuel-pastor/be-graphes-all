package org.insa.graphs.gui.simple;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * @author Emmanuel Pastor
 * Date: 25/03/2021
 */
public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame("BE Graphes - Launch");
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setSize(new Dimension(800, 600));
            frame.setContentPane(basicDrawing);
            frame.validate();
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // Visit these directory to see the list of available files on Commetud.
        final String mapName = System.getProperty("user.dir") + "/be-graphes-gui/src/main/resources/maps/test/haute-garonne.mapgr";
        final String pathName = System.getProperty("user.dir") + "/be-graphes-gui/src/main/resources/paths/test/path_fr31_roads_for_cars_test.path";

        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName)))
        );

        final Graph graph = reader.read();

        // Create the drawing:
        final Drawing drawing = createDrawing();

        drawing.drawGraph(graph);

        final PathReader pathReader = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(pathName)))
        );

        final Path path = pathReader.readPath(graph);

        drawing.drawPath(path, Color.GREEN);
    }

}
