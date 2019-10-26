package seko.kafka.connect.transformer;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.util.Requirements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seko.kafka.connect.transformer.configs.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ScriptEngineTransformerTest {
    private ScriptEngineTransformer<SourceRecord> transformer = new ScriptEngineTransformer<>();
    private Map<String, Object> config;
    private SourceRecord record;

    @Before
    public void setUp() {
        config = new HashMap<>();
        Map<String, Object> event = new HashMap<>();
        event.put("created_when", "2019-05-31T00:17:00.188Z");
        record = new SourceRecord(null, null, "topic", 0, null, "key___", null, event);
    }

    @Test
    public void applyWithoutSchemaJs() {
        config.put(Configuration.SCRIP_ENGINE_NAME, "JavaScript");
        config.put(Configuration.KEY_SCRIPT_CONFIG, "function keyTransform(source){ return source + '123'; }");
        config.put(Configuration.VALUE_SCRIPT_CONFIG, "function valueTransform(source){ source.qweqweq = 12312312; return source;}");
        transformer.configure(config);

        SourceRecord transformed = transformer.apply(record);
        Map<String, Object> stringObjectMap = Requirements.requireMapOrNull(transformed.value(), "");
        Assert.assertEquals(12312312, stringObjectMap.get("qweqweq"));
        Assert.assertEquals(2, stringObjectMap.size());
        Assert.assertEquals("key___123", transformed.key());
    }

    @Test(expected = ConfigException.class)
    public void applyWithoutSchemaJsConfigFail() {
        config.put(Configuration.SCRIP_ENGINE_NAME, "JavaScript");
        config.put(Configuration.KEY_SCRIPT_CONFIG, "java.lang.System.exit(9);function keyTransform(source){ return source + '123'; }");
        config.put(Configuration.VALUE_SCRIPT_CONFIG, "function valueTransform(source){ source.qweqweq = 12312312; return source;}");
        transformer.configure(config);

        SourceRecord transformed = transformer.apply(record);
        Map<String, Object> stringObjectMap = Requirements.requireMapOrNull(transformed.value(), "");
        Assert.assertEquals(12312312, stringObjectMap.get("qweqweq"));
        Assert.assertEquals(2, stringObjectMap.size());
        Assert.assertEquals("key___123", transformed.key());
    }

    @Test
    public void applyWithoutSchemaJsFail() {
        config.put(Configuration.SCRIP_ENGINE_NAME, "JavaScript");
        config.put(Configuration.KEY_SCRIPT_CONFIG, "function keyTransform(source){ java.lang.System.exit(9); return source + '123'; }");
        config.put(Configuration.VALUE_SCRIPT_CONFIG, "function valueTransform(source){ source.qweqweq = 12312312; return source;}");
        transformer.configure(config);

        SourceRecord transformed = transformer.apply(record);
        Map<String, Object> stringObjectMap = Requirements.requireMapOrNull(transformed.value(), "");
        Assert.assertEquals(12312312, stringObjectMap.get("qweqweq"));
        Assert.assertEquals(2, stringObjectMap.size());
        Assert.assertEquals("key___", transformed.key());
    }

    /*@Test(expected = ConfigException.class)
    public void testSFConfigEmpty() {
        config.put(Configuration.KEY_SCRIPT_CONFIG, "");
        dateRouter.configure(config);
    }*/
}
