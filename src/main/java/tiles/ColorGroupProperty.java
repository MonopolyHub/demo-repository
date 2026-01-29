package tiles;

import model.Player;
import tiles.data.PropertyData;

public class ColorGroupProperty {
    int size;
    private ColorGroup colorGroup;
    private PropertyData[] properties;
    private int capacity;

    public ColorGroupProperty(ColorGroup colorGroup, int capacity) {
        this.colorGroup = colorGroup;
        properties = new PropertyData[capacity];
        this.capacity = capacity;
        this.size = 0;
    }

    public Boolean propertyColorCheck(Player player)
    {
        for (PropertyData property : properties) {
            if (property.getOwner() != player) {
                return false;
            }
        }
        return true;
    }

    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup colorGroup) {
        this.colorGroup = colorGroup;
    }

    public PropertyData[] getProperties() {
        return properties;
    }

    public void setProperties(PropertyData[] properties) {
        this.properties = properties;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addProperty(PropertyData property) {
        if (size <= capacity) {
            properties[size] = property;
            size++;
        }
    }
}
