package tieorange.com.campuswarsaw.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tieorange on 03/05/16.
 */
public class Events {
    public int offset;
    public List<Event> results = new ArrayList<Event>();
    public List<Object> cookies = new ArrayList<Object>();
    public String connectorVersionGuid;
    public String connectorGuid;
    public String pageUrl;
    public List<OutputProperty> outputProperties = new ArrayList<OutputProperty>();

}
