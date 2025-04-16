package org.booking.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.booking.model.EventImpl;
import org.booking.model.TicketImpl;
import org.booking.model.UserImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonToObject {
    public static Map<String, Object> extractObjects(String jsonFilePath) throws IOException {
        Map<String, Object> storage = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();

            if (key.startsWith("event")) {
                EventImpl event = objectMapper.treeToValue(value, EventImpl.class);
                storage.put(key, event);
            } else if (key.startsWith("ticket")) {
                TicketImpl ticket = objectMapper.treeToValue(value, TicketImpl.class);
                storage.put(key, ticket);
            } else if (key.startsWith("user")) {
                UserImpl user = objectMapper.treeToValue(value, UserImpl.class);
                storage.put(key, user);
            }
        }
        return storage;
    }
}
