package io.swagger.assert4j.support;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ImmutableMap;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ContentUtilsTest {
    @Test
    public void serializeContent() throws Exception {
        Map map = ImmutableMap.of( "test", "1", "test2", 2 );

        assertNull( ContentUtils.serializeContent(  null, null ));
        assertNull( ContentUtils.serializeContent(  null, "application/json" ));

        String content = ContentUtils.serializeContent( map, "application/json");
        assertNotNull(Json.mapper().reader().readTree( content ));

        content = ContentUtils.serializeContent( map, "application/yaml");
        assertNotNull(Yaml.mapper().reader().readTree( content ));

        content = ContentUtils.serializeContent( map, "text/xml");
        assertNotNull(new XmlMapper().reader().readTree(content));

        content = ContentUtils.serializeContent( map, "some/type");
        assertEquals( content, map.toString());
    }
}