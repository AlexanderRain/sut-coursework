package edu.coursework.controller;

import edu.coursework.model.*;
import edu.coursework.model.djikstra.Edge;
import edu.coursework.model.djikstra.Vertex;
import edu.coursework.utils.Dimensions;
import edu.coursework.view.panels.controls.ControlsPanel;
import edu.coursework.view.panels.controls.EventRowItem;
import edu.coursework.view.panels.map.MainMapPanel;
import edu.coursework.view.panels.map.djikstra.DjikstraConfigurator;
import edu.coursework.view.panels.map.djikstra.DjikstraPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainController implements MouseListener {

    private MainMapPanel mainMapPanel;
    private ControlsPanel controlsPanel;
    private List<BaseEvent> eventList = new ArrayList<>();

    private static int count = 0;
    private DjikstraConfigurator djikstraConfigurator;
    private List<Vertex> vertexList = new ArrayList<>();

    public void attachViews(MainMapPanel mainMapPanel, ControlsPanel controlsPanel) {
        this.mainMapPanel = mainMapPanel;
        this.controlsPanel = controlsPanel;
        mainMapPanel.getMapPanel().addMouseListener(this);
    }

    //process show event btn click
    public void onEventsShowClicked() {
        List<EventRowItem> eventRowItems = controlsPanel.getEventsPanel().getSelectedElements();
        if (controlsPanel.getEventsScalePanel().getIsFixedSize()) {
            //if event have fixed scale generate it
            for (EventRowItem eventRowItem : eventRowItems) {
                eventList.addAll(generateEvents(
                        eventRowItem.getFromTextField(),
                        eventRowItem.getToTextField(),
                        controlsPanel.getEventsScalePanel().getFixedSize(),
                        eventRowItem.getFigure()));
            }
        } else {
            //event needs to generate it scale in given range
            for (EventRowItem eventRowItem : eventRowItems) {
                eventList.addAll(generateEvents(
                        eventRowItem.getFromTextField(),
                        eventRowItem.getToTextField(),
                        controlsPanel.getEventsScalePanel().getFromText(),
                        controlsPanel.getEventsScalePanel().getToText(),
                        eventRowItem.getFigure()));
            }
        }
        mainMapPanel.getMapPanel().setEventList(eventList);
        calculateAmountStatistics();
        calculateScaleStatistics();
    }

    //generate random amount of events in given scale
    private List<BaseEvent> generateEvents(int fromAmount, int toAmount, int scale, Figure figure) {
        List<BaseEvent> eventsList = new ArrayList<>();
        int eventsAmount = randomInRange(fromAmount, toAmount);
        //create events in following amount
        for (int i = 0; i < eventsAmount; i++) {
            BaseEvent baseEvent = null;
            switch (figure) {
                case Rectangle:
                    baseEvent = new RectangleEvent(
                            scale,
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    );

                    break;
                case Triangle:
                    baseEvent = new TriangleEvent(
                            scale,
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    );
                    break;
                case Circle:
                    baseEvent = new CircleEvent(
                            scale,
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    );
                    break;
            }

            eventsList.add(baseEvent);
            createVertex(baseEvent);
        }
        return eventsList;
    }

    //generate random amount of events in ranged scale
    private List<BaseEvent> generateEvents(int fromAmount, int toAmount, int fromScale, int toScale, Figure figure) {
        List<BaseEvent> eventsList = new ArrayList<>();
        int eventsAmount = randomInRange(fromAmount, toAmount);
        for (int i = 0; i < eventsAmount; i++) {
            switch (figure) {
                case Rectangle:
                    eventsList.add(new RectangleEvent(
                            randomInRange(fromScale, toScale),
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    ));
                    break;
                case Triangle:
                    eventsList.add(new TriangleEvent(
                            randomInRange(fromScale, toScale),
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    ));
                    break;
                case Circle:
                    eventsList.add(new CircleEvent(
                            randomInRange(fromScale, toScale),
                            randomInRange(0, Dimensions.MAP_WIDTH),
                            randomInRange(0, Dimensions.MAP_HEIGHT),
                            figure
                    ));
                    break;
            }
        }
        return eventsList;
    }

    //generate random value in given range [from;to]
    private int randomInRange(int from, int to) {
        return (int) (Math.random() * (to - from) + 1 + from);
    }

    //clear map
    public void clearEvents() {
        eventList.clear();
        mainMapPanel.getMapPanel().setEventList(eventList);
        mainMapPanel.getMapPanel().setLines(new ArrayList<>());
        mainMapPanel.getMapPanel().repaint();
        controlsPanel.getAmountStatisticPanel().setStatistics(null);
        controlsPanel.getScaleStatisticPanel().setStatistics(null);
    }

    public void changeDrawingStatus(String status) {
        controlsPanel.getEventsButtonsPanel().setStatusText(status);
    }

    private void calculateAmountStatistics() {
        int[] amountStatistics = new int[3];
        //count events every type
        for (BaseEvent figure : eventList) {
            switch (figure.getFigure()) {
                case Triangle:
                    System.out.println("Triangle added");
                    amountStatistics[0]++;
                    break;
                case Rectangle:
                    System.out.println("Rectangle added");
                    amountStatistics[1]++;
                    break;
                case Circle:
                    System.out.println("Circle added");
                    amountStatistics[2]++;
                    break;

            }
        }
        //draw
        controlsPanel.getAmountStatisticPanel().setStatistics(amountStatistics);
    }

    private void calculateScaleStatistics() {
        int[] scaleStatistics = new int[3];

        //count max scale
        int maxScale = 0;
        for (BaseEvent figure : eventList) {
            if (figure.getScale() > maxScale) {
                maxScale = figure.getScale();
            }
        }

        //calculate 30% 60% form max
        int classA = (int) Math.round(maxScale * 0.3);
        int classB = (int) Math.round(maxScale * 0.6);

        //find out each class class
        for (BaseEvent figure : eventList) {
            if (figure.getScale() <= classA) {
                scaleStatistics[0]++;
            } else if (figure.getScale() > classA && figure.getScale() <= classB) {
                scaleStatistics[1]++;
            } else {
                scaleStatistics[2]++;
            }
        }
        //draw
        controlsPanel.getScaleStatisticPanel().setStatistics(scaleStatistics);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainMapPanel.getDjikstraPanel().isEnabled()) {
            Point point = e.getPoint();
            BaseEvent baseEvent = null;
            DjikstraPanel djikstraPanel = mainMapPanel.getDjikstraPanel();

            if (djikstraConfigurator == null) {
                djikstraConfigurator = new DjikstraConfigurator();
            }

            switch (djikstraPanel.getActionCommand()) {
                case "Triangle":
                    baseEvent = new TriangleEvent(
                            djikstraPanel.getScale(),
                            (int) point.getX(),
                            (int) point.getY(),
                            Figure.Djikstra
                    );
                    break;
                case "Circle":
                    baseEvent = new CircleEvent(
                            djikstraPanel.getScale(),
                            (int) point.getX(),
                            (int) point.getY(),
                            Figure.Djikstra
                    );
                    break;
                case "Rectangle":
                    baseEvent = new RectangleEvent(
                            djikstraPanel.getScale(),
                            (int) point.getX(),
                            (int) point.getY(),
                            Figure.Djikstra
                    );

                    break;
            }


            mainMapPanel.getMapPanel().addEventToList(baseEvent);

            findNearestWay();

            Vertex createdVertex = createVertex(baseEvent);
            if (djikstraConfigurator.getFirstVertex() == null) {
                djikstraConfigurator.setFirstVertex(createdVertex);
            } else if (djikstraConfigurator.getSecondVertex() == null) {
                djikstraConfigurator.setSecondVertex(createdVertex);
            }


            findNearestWay();
        }
    }

    private void findNearestWay() {
        if (djikstraConfigurator.isFull()) {
            System.out.println("FIRST " + djikstraConfigurator.getFirstVertex() + " SECOND " + djikstraConfigurator.getSecondVertex());
            djikstraConfigurator.computePaths(djikstraConfigurator.getFirstVertex());
            List<Vertex> path = djikstraConfigurator.getShortestPathTo(djikstraConfigurator.getSecondVertex());

            try {
                for (int i = 0; i < path.size(); i++) {
                    BaseEvent first = path.get(i).getEvent();
                    BaseEvent second = path.get(i + 1).getEvent();

                    mainMapPanel.getMapPanel().addLine(
                            new Line2D.Double(
                                    first.getPositionX(), first.getPositionY(), second.getPositionX(), second.getPositionY()
                            )
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mainMapPanel.getMapPanel().repaint();


            djikstraConfigurator = null;
        }
    }

    private Vertex createVertex(BaseEvent baseEvent) {
        Vertex vertex = new Vertex(baseEvent.getFigure() + " " + baseEvent.getPositionX() + " " + baseEvent.getPositionY() + " " + count, baseEvent);
        count++;
        vertexList.add(vertex);

        List<BaseEvent> eventsInArea = findNearestEvents(baseEvent);
        eventsInArea.forEach(temp -> System.out.println(temp.getFigure() + " " + temp.getPositionY() + " " + temp.getPositionX()));

        findDestination(vertex, baseEvent, eventsInArea);

        return vertex;
    }


    private void findDestination(Vertex mainVertex, BaseEvent baseEvent, List<BaseEvent> eventsInArea) {
        for (BaseEvent temp : eventsInArea) {
            Vertex vertex = vertexList.stream()
                    .filter(o -> o.getEvent() == temp)
                    .findFirst().orElse(null);

            if (vertex == null) {
                vertex = new Vertex(temp.getFigure() + " " + temp.getPositionX() + " " + temp.getPositionY() + " " + count, temp);
            }

            int distance = countDestination(baseEvent.getPositionX(), baseEvent.getPositionY(),
                    temp.getPositionX(), temp.getPositionY());

            mainVertex.addDestination(new Edge(vertex, distance));
            vertex.addDestination(new Edge(mainVertex, distance));


            count++;
        }

    }

    private List<BaseEvent> findNearestEvents(BaseEvent mainEvent) {
        int area = mainEvent.getScale() * 3;

        int firstX = mainEvent.getPositionX() - area;
        int secondX = mainEvent.getPositionX() + area;

        int firstY = mainEvent.getPositionY() - area;
        int secondY = mainEvent.getPositionY() + area;

        List<BaseEvent> nearestEvents = eventList.stream()
                .filter(temp -> temp.getPositionX() >= firstX && temp.getPositionX() <= secondX)
                .filter(temp -> temp.getPositionY() >= firstY && temp.getPositionY() <= secondY)
                .collect(Collectors.toList());
        if (nearestEvents.contains(mainEvent)) nearestEvents.remove(mainEvent);
        return nearestEvents;
    }

    private int countDestination(int x1, int y1, int x2, int y2) {
        int x = (int) (Math.pow(x2 - x1, 2));
        int y = (int) (Math.pow(y2 - y1, 2));
        return (int) Math.sqrt(x + y);
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
