package br.borbi.ots.pojo;

/**
 * Created by Gabriela on 28/05/2015.
 */
public class Tag {

    String resourceName;

    @Override
    public String toString() {
        return "Tag{" +
                "resourceName='" + resourceName + '\'' +
                '}';
    }

    public String getResourceName() { return resourceName;}

    public void setResourceName(String resourceName) { this.resourceName = resourceName;}

    public Tag(String resourceName) {

        this.resourceName = resourceName;
    }
}
