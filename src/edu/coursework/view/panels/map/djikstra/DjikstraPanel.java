package edu.coursework.view.panels.map.djikstra;

import edu.coursework.model.Figure;
import edu.coursework.view.panels.BasePanel;
import edu.coursework.view.panels.controls.EventRowItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DjikstraPanel extends BasePanel {

    private JLabel title;
    private List<DjikstraEventRowItem> eventRowItemList = new ArrayList<>();
    private ButtonGroup radioButtonGroup;
    private Button button;
    private boolean enabled = false;

    public DjikstraPanel(int width, int height, Border border) {
        super(width, height, border, new FlowLayout(FlowLayout.LEFT, 10, 5));

        radioButtonGroup = new ButtonGroup();
        title = new JLabel("Dijkstra's algorithm");
        add(title);

        DjikstraEventRowItem firstRow = new DjikstraEventRowItem("Triangle",
                new FlowLayout(FlowLayout.LEFT, 5, 3), Figure.Triangle);
        eventRowItemList.add(firstRow);
        add(firstRow);
        radioButtonGroup.add(firstRow.getRadioButton());

        DjikstraEventRowItem secondRow = new DjikstraEventRowItem("Rectangle",
                new FlowLayout(FlowLayout.LEFT, 5, 3), Figure.Rectangle);
        eventRowItemList.add(secondRow);
        add(secondRow);
        radioButtonGroup.add(secondRow.getRadioButton());

        DjikstraEventRowItem thirdRow = new DjikstraEventRowItem("Circle",
                new FlowLayout(FlowLayout.LEFT, 5, 3), Figure.Circle);
        eventRowItemList.add(thirdRow);
        add(thirdRow);
        radioButtonGroup.add(thirdRow.getRadioButton());

        button = new Button("Add event");
        button.addActionListener((event) -> changeLabel());
        add(button);



    }

    private void changeLabel(){
        enabled = !enabled;
        title.setText(enabled ? "Select place on map" : "Dijkstra's algorithm");
    }


}
