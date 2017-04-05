package elements;

import core.BaseAssert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.ReportUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SelectBox extends BaseElement {
    public SelectBox(String controlDescription, String xpath) {
        super(controlDescription, xpath);
    }

    private Select selectBox() {
        return new Select(this.waitElement());
    }

    private List<String> getAllOptions() {
        return selectBox().getOptions().stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void selectOption(String value) {
        ReportUtils.logAll("Select option [" + value + "] in [" + controlDescription + "]");
        selectBox().selectByVisibleText(value);
    }

    public String selectIndex(int index) {
        ReportUtils.logAll("Select index [" + index + "] in [" + controlDescription + "]");
        selectBox().selectByIndex(index);
        return this.getText();
    }

    public String selectRandomValue() {
        ReportUtils.logAll("Select a random option in [" + controlDescription + "]");
        Random rand = new Random();
        return selectIndex(rand.nextInt(selectBox().getOptions().size()));
    }

    public void shouldHaveOptions(String... items) {
        String description = "Check [" + controlDescription + "] contains options: " + Arrays.toString(items);
        List<String> missingOptions = new ArrayList<>();
        List<String> existingOptions = getAllOptions();

        for (String item : items)
            if (!existingOptions.contains(item)) missingOptions.add(item);

        if (missingOptions.size() == 0)
            BaseAssert.assertEquals(description, true, true);
        else
            BaseAssert.assertEquals(description, "Missing options: " + missingOptions.toString(), Arrays.toString(items));
    }

    public void shouldNotHaveOptions(String... items) {
        String description = "Check [" + controlDescription + "] does NOT contain options: " + Arrays.toString(items);
        List<String> existingOptions = new ArrayList<>();
        List<String> allOptions = getAllOptions();

        for (String item : items)
            if (allOptions.contains(item)) existingOptions.add(item);

        if (existingOptions.size() == 0)
            BaseAssert.assertEquals(description, true, true);
        else
            BaseAssert.assertEquals(description, "Still see options: " + existingOptions.toString(), Arrays.toString(items));
    }

    public void shouldHaveText(String text) {
        String description = "Check [" + controlDescription + "] displays [" + text + "]";
        BaseAssert.assertEquals(description, selectBox().getFirstSelectedOption().getText(), text);
    }
}