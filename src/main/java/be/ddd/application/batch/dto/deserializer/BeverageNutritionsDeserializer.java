package be.ddd.application.batch.dto.deserializer;

import be.ddd.application.batch.dto.BeverageNutritionDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jackson deserializer that accepts both array- and map-shaped beverage nutrition payloads.
 *
 * <p>Lambda 응답이 {@code ["size": "..."]} 리스트이든 {@code {"TALL": {...}}} 맵이든 동일하게 파싱한다. 맵 형태일 경우 키를
 * size 값으로 주입한다.
 */
public class BeverageNutritionsDeserializer
        extends JsonDeserializer<Map<String, BeverageNutritionDto>> {

    @Override
    public Map<String, BeverageNutritionDto> deserialize(
            JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (node == null || node.isNull()) {
            return Map.of();
        }

        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        Map<String, BeverageNutritionDto> result = new LinkedHashMap<>();

        if (node.isArray()) {
            for (JsonNode element : node) {
                BeverageNutritionDto dto = mapper.treeToValue(element, BeverageNutritionDto.class);
                String sizeKey =
                        element.hasNonNull("size") ? element.get("size").asText() : dto.size();
                if (sizeKey == null || sizeKey.isBlank()) {
                    continue;
                }
                result.put(sizeKey, dto.withSize(sizeKey));
            }
        } else if (node.isObject()) {
            node.fields()
                    .forEachRemaining(
                            entry -> {
                                try {
                                    BeverageNutritionDto dto =
                                            mapper.treeToValue(
                                                    entry.getValue(), BeverageNutritionDto.class);
                                    result.put(entry.getKey(), dto.withSize(entry.getKey()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
        } else if (node.isValueNode() && node.asToken() == JsonToken.VALUE_NULL) {
            return Map.of();
        }

        return result;
    }
}
